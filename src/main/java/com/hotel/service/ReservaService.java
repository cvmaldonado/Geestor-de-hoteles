/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.ReservaDAO;
import com.hotel.dao.HabitacionDAO;
import com.hotel.dao.HuespedDAO;
import com.hotel.model.Reserva;
import com.hotel.model.Habitacion;
import com.hotel.model.Huesped;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */


@Stateless
public class ReservaService {

    @Inject
    private ReservaDAO reservaDAO;

    @Inject
    private HabitacionDAO habitacionDAO;

    @Inject
    private HuespedDAO huespedDAO;

    public Reserva crearReserva(Reserva reserva) {
    // Validaciones básicas
    validarReserva(reserva);
    
     if (reserva.getHuesped() == null || reserva.getHuesped().getIdUsuario() == null) {
        throw new IllegalArgumentException("El huésped es obligatorio");
    }
     
    Huesped huesped = huespedDAO.buscarPorId(reserva.getHuesped().getIdUsuario());
    if (huesped == null) {
        throw new IllegalArgumentException("Huésped no encontrado con ID: " + reserva.getHuesped().getIdUsuario());
    }
    reserva.setHuesped(huesped);
    
    if (reserva.getHabitacion() == null || reserva.getHabitacion().getIdHabitacion() == null) {
        throw new IllegalArgumentException("La habitación es obligatoria");
    }
    
    Habitacion habitacion = habitacionDAO.buscarPorId(reserva.getHabitacion().getIdHabitacion());
    if (habitacion == null) {
        throw new IllegalArgumentException("Habitación no encontrada");
    }
    reserva.setHabitacion(habitacion);
    
    if (!estaDisponible(habitacion, reserva.getFechaEntrada(), reserva.getFechaSalida())) {
        throw new IllegalArgumentException("La habitación no está disponible para las fechas seleccionadas");
    }
    
    if (reserva.getNumeroHuespedes() > habitacion.getCapacidad()) {
        throw new IllegalArgumentException("La cantidad de huéspedes excede la capacidad de la habitación");
    }
    
    // Establecer fecha de reserva actual y estado por defecto
    reserva.setFechaReserva(LocalDateTime.now());
    if (reserva.getEstadoReserva() == null) {
        reserva.setEstadoReserva("pendiente");
    }
    
    reservaDAO.crear(reserva);
    return reserva;
}

    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaDAO.buscarPorId(id);
        if (reservaExistente == null) {
            throw new IllegalArgumentException("Reserva no encontrada");
        }

        // Validar si se cambian fechas o habitación
        if (!reservaExistente.getHabitacion().equals(reservaActualizada.getHabitacion()) ||
            !reservaExistente.getFechaEntrada().equals(reservaActualizada.getFechaEntrada()) ||
            !reservaExistente.getFechaSalida().equals(reservaActualizada.getFechaSalida())) {
            
            validarReserva(reservaActualizada);
            
            if (!estaDisponible(reservaActualizada.getHabitacion(), 
                              reservaActualizada.getFechaEntrada(), 
                              reservaActualizada.getFechaSalida(), 
                              id)) {
                throw new IllegalArgumentException("La habitación no está disponible para las nuevas fechas");
            }
        }

        // Actualizar campos
        reservaExistente.setHuesped(reservaActualizada.getHuesped());
        reservaExistente.setHabitacion(reservaActualizada.getHabitacion());
        reservaExistente.setFechaEntrada(reservaActualizada.getFechaEntrada());
        reservaExistente.setFechaSalida(reservaActualizada.getFechaSalida());
        reservaExistente.setNumeroHuespedes(reservaActualizada.getNumeroHuespedes());
        reservaExistente.setEstadoReserva(reservaActualizada.getEstadoReserva());
        reservaExistente.setObservaciones(reservaActualizada.getObservaciones());

        reservaDAO.actualizar(reservaExistente);
        return reservaExistente;
    }

    public void cancelarReserva(Long id) {
        Reserva reserva = reservaDAO.buscarPorId(id);
        if (reserva != null) {
            reserva.setEstadoReserva("cancelada");
            reservaDAO.actualizar(reserva);
        }
    }

    public void confirmarReserva(Long id) {
        Reserva reserva = reservaDAO.buscarPorId(id);
        if (reserva != null && "pendiente".equals(reserva.getEstadoReserva())) {
            reserva.setEstadoReserva("confirmada");
            reservaDAO.actualizar(reserva);
        }
    }

    public boolean estaDisponible(Habitacion habitacion, LocalDate entrada, LocalDate salida) {
        return !reservaDAO.existeReservaEnRango(habitacion, entrada, salida);
    }

    public boolean estaDisponible(Habitacion habitacion, LocalDate entrada, LocalDate salida, Long idReservaExcluir) {
        // Para actualizaciones, excluir la reserva actual
        List<Reserva> reservas = reservaDAO.listarPorHabitacion(habitacion);
        return reservas.stream()
                .filter(r -> !r.getIdReserva().equals(idReservaExcluir))
                .filter(r -> r.getEstadoReserva().equals("pendiente") || r.getEstadoReserva().equals("confirmada"))
                .noneMatch(r -> haySuperposicion(
                    r.getFechaEntrada(), r.getFechaSalida(), 
                    entrada, salida
                ));
    }

    private boolean haySuperposicion(LocalDate inicio1, LocalDate fin1, LocalDate inicio2, LocalDate fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    private void validarReserva(Reserva reserva) {
        if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null) {
            throw new IllegalArgumentException("Las fechas de entrada y salida son obligatorias");
        }

        if (!reserva.getFechaEntrada().isBefore(reserva.getFechaSalida())) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida");
        }

        if (reserva.getFechaEntrada().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser en el pasado");
        }

        if (reserva.getHuesped() == null || reserva.getHuesped().getIdUsuario() == null) {
            throw new IllegalArgumentException("El huésped es obligatorio");
        }

        if (reserva.getHabitacion() == null || reserva.getHabitacion().getIdHabitacion() == null) {
            throw new IllegalArgumentException("La habitación es obligatoria");
        }

        if (reserva.getNumeroHuespedes() == null || reserva.getNumeroHuespedes() <= 0) {
            throw new IllegalArgumentException("El número de huéspedes debe ser mayor a cero");
        }
    }

    // Métodos de consulta
    public List<Reserva> listarTodas() {
        return reservaDAO.listarTodas();
    }

    public List<Reserva> listarPorHuesped(Long idHuesped) {
    Huesped huesped = huespedDAO.buscarPorId(idHuesped);
    if (huesped == null) {
        throw new IllegalArgumentException("Huésped no encontrado");
    }
    return reservaDAO.listarPorHuesped(huesped);
    }

    public List<Reserva> listarPorEstado(String estado) {
        return reservaDAO.listarPorEstado(estado);
    }

    public List<Reserva> listarReservasActivas() {
        return reservaDAO.listarReservasActivas();
    }
    
    public Reserva buscarPorId(Long id) {
    return reservaDAO.buscarPorId(id);
    }
}