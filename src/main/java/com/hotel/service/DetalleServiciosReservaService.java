/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.DetalleServiciosReservaDAO;
import com.hotel.dao.ReservaDAO;
import com.hotel.dao.ServicioExtraDAO;
import com.hotel.model.DetalleServiciosReserva;
import com.hotel.model.Reserva;
import com.hotel.model.ServicioExtra;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class DetalleServiciosReservaService {

    @Inject
    private DetalleServiciosReservaDAO detalleServiciosReservaDAO;

    @Inject
    private ReservaDAO reservaDAO;

    @Inject
    private ServicioExtraDAO servicioExtraDAO;

    public DetalleServiciosReserva crearDetalle(DetalleServiciosReserva detalle) {
        validarDetalle(detalle);
        
        // Verificar que la reserva existe
        Long idReserva = detalle.getReserva().getIdReserva();
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }
        detalle.setReserva(reserva);
        
        // Verificar que el servicio existe
        Long idServicio = detalle.getServicio().getIdServicio();
        ServicioExtra servicio = servicioExtraDAO.buscarPorId(idServicio);
        if (servicio == null) {
            throw new IllegalArgumentException("Servicio extra no encontrado con ID: " + idServicio);
        }
        
        // Validar que el servicio tiene los datos requeridos
        if (servicio.getNombre() == null || servicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El servicio con ID " + idServicio + " no tiene nombre válido");
        }
        
        if (servicio.getPrecio() == null || servicio.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El servicio con ID " + idServicio + " no tiene precio válido");
        }
        
        detalle.setServicio(servicio);
        
        // Establecer precio unitario desde el servicio
        detalle.setPrecioUnitario(servicio.getPrecio());
        
        // Establecer fecha actual si no viene
        if (detalle.getFechaServicio() == null) {
            detalle.setFechaServicio(LocalDate.now());
        }
        
        // Establecer cantidad por defecto
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            detalle.setCantidad(1);
        }
        
        detalleServiciosReservaDAO.guardar(detalle);
        return detalle;
    }

    public DetalleServiciosReserva actualizarDetalle(Long id, DetalleServiciosReserva detalleActualizado) {
        DetalleServiciosReserva detalleExistente = detalleServiciosReservaDAO.buscarPorId(id);
        if (detalleExistente == null) {
            throw new IllegalArgumentException("Detalle de servicio no encontrado con ID: " + id);
        }

        validarDetalle(detalleActualizado);

        // Actualizar campos permitidos
        detalleExistente.setFechaServicio(detalleActualizado.getFechaServicio());
        detalleExistente.setCantidad(detalleActualizado.getCantidad());
        detalleExistente.setObservaciones(detalleActualizado.getObservaciones());

        // Si cambia el precio unitario, actualizarlo
        if (detalleActualizado.getPrecioUnitario() != null) {
            detalleExistente.setPrecioUnitario(detalleActualizado.getPrecioUnitario());
        }

        // Si cambia el servicio, validar que existe
        if (detalleActualizado.getServicio() != null && 
            detalleActualizado.getServicio().getIdServicio() != null &&
            !detalleExistente.getServicio().getIdServicio().equals(detalleActualizado.getServicio().getIdServicio())) {
            
            ServicioExtra servicio = servicioExtraDAO.buscarPorId(detalleActualizado.getServicio().getIdServicio());
            if (servicio == null) {
                throw new IllegalArgumentException("Servicio extra no encontrado con ID: " + detalleActualizado.getServicio().getIdServicio());
            }
            detalleExistente.setServicio(servicio);
            
            // Actualizar precio unitario si no se especificó uno manualmente
            if (detalleActualizado.getPrecioUnitario() == null) {
                detalleExistente.setPrecioUnitario(servicio.getPrecio());
            }
        }

        detalleServiciosReservaDAO.actualizar(detalleExistente);
        return detalleExistente;
    }

    public void eliminarDetalle(Long id) {
        DetalleServiciosReserva detalle = detalleServiciosReservaDAO.buscarPorId(id);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle de servicio no encontrado con ID: " + id);
        }
        detalleServiciosReservaDAO.eliminar(id);
    }

    public DetalleServiciosReserva buscarPorId(Long id) {
        return detalleServiciosReservaDAO.buscarPorId(id);
    }

    public List<DetalleServiciosReserva> listarTodos() {
        return detalleServiciosReservaDAO.listarTodos();
    }

    public List<DetalleServiciosReserva> buscarPorReserva(Long idReserva) {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }
        return detalleServiciosReservaDAO.buscarPorReserva(reserva);
    }

    public List<DetalleServiciosReserva> buscarPorServicio(Long idServicio) {
        ServicioExtra servicio = servicioExtraDAO.buscarPorId(idServicio);
        if (servicio == null) {
            throw new IllegalArgumentException("Servicio extra no encontrado con ID: " + idServicio);
        }
        return detalleServiciosReservaDAO.buscarPorServicio(servicio);
    }

    public BigDecimal calcularTotalPorReserva(Long idReserva) {
        List<DetalleServiciosReserva> detalles = buscarPorReserva(idReserva);
        return detalles.stream()
                .map(DetalleServiciosReserva::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validarDetalle(DetalleServiciosReserva detalle) {
        if (detalle.getReserva() == null || detalle.getReserva().getIdReserva() == null) {
            throw new IllegalArgumentException("La reserva es obligatoria");
        }

        if (detalle.getServicio() == null || detalle.getServicio().getIdServicio() == null) {
            throw new IllegalArgumentException("El servicio extra es obligatorio");
        }

    }
}