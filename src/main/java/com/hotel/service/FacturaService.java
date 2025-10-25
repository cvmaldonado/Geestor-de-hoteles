/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.FacturaDAO;
import com.hotel.dao.ReservaDAO;
import com.hotel.dao.DetalleServiciosReservaDAO;
import com.hotel.model.Factura;
import com.hotel.model.Reserva;
import com.hotel.model.DetalleServiciosReserva;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class FacturaService {

    private static final BigDecimal IVA_PORCENTAJE = new BigDecimal("0.12"); // 12% IVA

    @Inject
    private FacturaDAO facturaDAO;

    @Inject
    private ReservaDAO reservaDAO;

    @Inject
    private DetalleServiciosReservaDAO detalleServiciosReservaDAO;

    public Factura crearFactura(Factura factura) {
        validarFactura(factura);
        
        // Verificar que la reserva existe
        Reserva reserva = reservaDAO.buscarPorId(factura.getReserva().getIdReserva());
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + factura.getReserva().getIdReserva());
        }
        factura.setReserva(reserva);
        
        // Verificar que el número de factura sea único
        if (factura.getNumeroFactura() != null && facturaDAO.existeNumeroFactura(factura.getNumeroFactura())) {
            throw new IllegalArgumentException("Ya existe una factura con el número: " + factura.getNumeroFactura());
        }
        
        // Generar número de factura automático si no se proporciona
        if (factura.getNumeroFactura() == null || factura.getNumeroFactura().trim().isEmpty()) {
            factura.setNumeroFactura(generarNumeroFactura());
        }
        
        // Establecer fecha de emisión actual
        factura.setFechaEmision(LocalDateTime.now());
        
        // Calcular automáticamente subtotal, IVA y total si no se proporcionan
        if (factura.getSubtotal() == null || factura.getIva() == null || factura.getTotal() == null) {
            calcularTotalesFactura(factura, reserva.getIdReserva());
        }
        
        // Validar que los cálculos sean consistentes
        validarCalculosFactura(factura);
        
        facturaDAO.guardar(factura);
        return factura;
    }

    public Factura actualizarFactura(Long id, Factura facturaActualizada) {
        Factura facturaExistente = facturaDAO.buscarPorId(id);
        if (facturaExistente == null) {
            throw new IllegalArgumentException("Factura no encontrada con ID: " + id);
        }

        // Validar que no se esté intentando modificar una factura pagada o anulada
        if ("pagada".equals(facturaExistente.getEstadoFactura()) || 
            "anulada".equals(facturaExistente.getEstadoFactura())) {
            throw new IllegalArgumentException("No se puede modificar una factura " + facturaExistente.getEstadoFactura());
        }

        // Actualizar campos permitidos
        if (facturaActualizada.getMetodoPago() != null) {
            facturaExistente.setMetodoPago(facturaActualizada.getMetodoPago());
        }
        
        if (facturaActualizada.getObservaciones() != null) {
            facturaExistente.setObservaciones(facturaActualizada.getObservaciones());
        }
        
        if (facturaActualizada.getEstadoFactura() != null) {
            facturaExistente.setEstadoFactura(facturaActualizada.getEstadoFactura());
        }

        facturaDAO.actualizar(facturaExistente);
        return facturaExistente;
    }

    public void anularFactura(Long id) {
        Factura factura = facturaDAO.buscarPorId(id);
        if (factura == null) {
            throw new IllegalArgumentException("Factura no encontrada con ID: " + id);
        }
        
        factura.setEstadoFactura("anulada");
        facturaDAO.actualizar(factura);
    }

    public void marcarComoPagada(Long id, String metodoPago) {
        Factura factura = facturaDAO.buscarPorId(id);
        if (factura == null) {
            throw new IllegalArgumentException("Factura no encontrada con ID: " + id);
        }
        
        factura.setEstadoFactura("pagada");
        if (metodoPago != null && !metodoPago.trim().isEmpty()) {
            factura.setMetodoPago(metodoPago);
        }
        
        facturaDAO.actualizar(factura);
    }

    public Factura buscarPorId(Long id) {
        return facturaDAO.buscarPorId(id);
    }

    public Factura buscarPorNumero(String numeroFactura) {
        return facturaDAO.buscarPorNumero(numeroFactura);
    }

    public List<Factura> listarTodas() {
        return facturaDAO.listarTodas();
    }

    public List<Factura> listarPorReserva(Long idReserva) {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }
        return facturaDAO.listarPorReserva(reserva);
    }

    public List<Factura> listarPorEstado(String estado) {
        if (!esEstadoValido(estado)) {
            throw new IllegalArgumentException("Estado de factura no válido: " + estado);
        }
        return facturaDAO.listarPorEstado(estado);
    }

    public Factura generarFacturaAutomatica(Long idReserva) {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }

        Factura factura = new Factura();
        factura.setReserva(reserva);
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setFechaEmision(LocalDateTime.now());
        factura.setEstadoFactura("pendiente");
        
        calcularTotalesFactura(factura, idReserva);
        
        facturaDAO.guardar(factura);
        return factura;
    }

    private void calcularTotalesFactura(Factura factura, Long idReserva) {
        // Calcular subtotal de servicios extra
        List<DetalleServiciosReserva> detalles = detalleServiciosReservaDAO.buscarPorReserva(
            reservaDAO.buscarPorId(idReserva)
        );
        
        BigDecimal subtotalServicios = detalles.stream()
                .map(DetalleServiciosReserva::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Aquí podrías agregar el cálculo del costo de la habitación
        BigDecimal subtotalHabitacion = reservaDAO.buscarPorId(idReserva).getHabitacion().getPrecioBase();
        
        // Subtotal total (servicios + habitación)
        BigDecimal subtotal = subtotalServicios.add(subtotalHabitacion);
        factura.setSubtotal(subtotal);
        
        // Calcular IVA
        BigDecimal iva = subtotal.multiply(IVA_PORCENTAJE).setScale(2, RoundingMode.HALF_UP);
        factura.setIva(iva);
        
        // Calcular total
        BigDecimal total = subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);
        factura.setTotal(total);
    }

    private String generarNumeroFactura() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "FACT-" + timestamp + "-" + random;
    }

    private void validarFactura(Factura factura) {
        if (factura.getReserva() == null || factura.getReserva().getIdReserva() == null) {
            throw new IllegalArgumentException("La reserva es obligatoria");
        }
    }

    private void validarCalculosFactura(Factura factura) {
        BigDecimal totalCalculado = factura.getSubtotal().add(factura.getIva());
        
        if (factura.getTotal().compareTo(totalCalculado) != 0) {
            throw new IllegalArgumentException("El total de la factura no coincide con los cálculos (subtotal + IVA)");
        }
    }

    private boolean esEstadoValido(String estado) {
        return estado != null && 
               (estado.equals("pendiente") || 
                estado.equals("pagada") || 
                estado.equals("anulada"));
    }
}