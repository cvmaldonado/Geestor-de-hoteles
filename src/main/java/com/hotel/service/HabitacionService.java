/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.HabitacionDAO;
import com.hotel.dao.HotelDAO;
import com.hotel.model.Habitacion;
import com.hotel.model.Hotel;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class HabitacionService {

    @Inject 
    private HabitacionDAO habitacionDAO;
    
    @Inject
    private HotelDAO hotelDAO;

    // Crear una nueva habitación
    public void crearHabitacion(Habitacion habitacion) {
        // Validaciones
        if (habitacion == null) {
            throw new IllegalArgumentException("Habitación no puede ser nula");
        }

        if (habitacion.getHotel() == null || habitacion.getHotel().getIdHotel() == null) {
            throw new IllegalArgumentException("Hotel es requerido");
        }

        if (habitacion.getNumero() == null || habitacion.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("Número de habitación no puede estar vacío");
        }

        if (habitacion.getTipoHabitacion() == null || habitacion.getTipoHabitacion().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de habitación no puede estar vacío");
        }

        if (habitacion.getCapacidad() == null || habitacion.getCapacidad() <= 0) {
            throw new IllegalArgumentException("Capacidad debe ser mayor a 0");
        }

        if (habitacion.getPrecioBase() == null || habitacion.getPrecioBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio base debe ser mayor a 0");
        }

        // Validar tipos de habitación permitidos
        if (!esTipoHabitacionValido(habitacion.getTipoHabitacion())) {
            throw new IllegalArgumentException("Tipo de habitación no válido. Debe ser: individual, doble, suite, familiar");
        }

        // Validar estados permitidos
        if (habitacion.getEstado() != null && !esEstadoValido(habitacion.getEstado())) {
            throw new IllegalArgumentException("Estado no válido. Debe ser: disponible, ocupada, mantenimiento, reservada");
        }

        // Verificar que el hotel existe
        Hotel hotel = hotelDAO.buscarPorId(habitacion.getHotel().getIdHotel());
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel no encontrado");
        }

        // Verificar que el número de habitación no existe
        Habitacion existente = habitacionDAO.buscarPorNumero(habitacion.getNumero());
        if (existente != null) {
            throw new IllegalArgumentException("El número de habitación ya existe");
        }

        habitacionDAO.crear(habitacion);
    }

    // Obtener habitación por ID
    public Habitacion obtenerHabitacionPorId(Long id) {
        return habitacionDAO.buscarPorId(id);
    }

    // Actualizar habitación
    public void actualizarHabitacion(Habitacion habitacion) {
        habitacionDAO.actualizar(habitacion);
    }

    // Eliminar habitación
    public void eliminarHabitacion(Long id) {
        Habitacion habitacion = habitacionDAO.buscarPorId(id);
        if (habitacion != null) {
            habitacionDAO.eliminar(id);
        }
    }

    // Cambiar estado de habitación
    public void cambiarEstadoHabitacion(Long id, String estado) {
        if (!esEstadoValido(estado)) {
            throw new IllegalArgumentException("Estado no válido");
        }

        Habitacion habitacion = habitacionDAO.buscarPorId(id);
        if (habitacion != null) {
            habitacion.setEstado(estado);
            habitacionDAO.actualizar(habitacion);
        }
    }

    // Listar todas las habitaciones
    public List<Habitacion> listarTodasLasHabitaciones() {
        return habitacionDAO.listarTodas();
    }

    // Listar habitaciones por hotel
    public List<Habitacion> listarHabitacionesPorHotel(Long idHotel) {
        Hotel hotel = hotelDAO.buscarPorId(idHotel);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel no encontrado");
        }
        return habitacionDAO.listarPorHotel(hotel);
    }

    // Listar habitaciones disponibles por hotel
    public List<Habitacion> listarHabitacionesDisponiblesPorHotel(Long idHotel) {
        Hotel hotel = hotelDAO.buscarPorId(idHotel);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel no encontrado");
        }
        return habitacionDAO.listarDisponiblesPorHotel(hotel);
    }

    // Listar habitaciones por estado
    public List<Habitacion> listarHabitacionesPorEstado(String estado) {
        if (!esEstadoValido(estado)) {
            throw new IllegalArgumentException("Estado no válido");
        }
        return habitacionDAO.listarPorEstado(estado);
    }

    // Buscar habitaciones por tipo
    public List<Habitacion> buscarHabitacionesPorTipo(String tipoHabitacion) {
        return habitacionDAO.buscarPorTipo(tipoHabitacion);
    }

    // Buscar habitaciones por rango de precio
    public List<Habitacion> buscarHabitacionesPorRangoPrecio(BigDecimal min, BigDecimal max) {
        if (min == null || max == null || min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Rango de precios no válido");
        }
        return habitacionDAO.buscarPorRangoPrecio(min, max);
    }

    // Métodos de validación
    private boolean esTipoHabitacionValido(String tipo) {
        return tipo != null && 
               (tipo.equalsIgnoreCase("individual") || 
                tipo.equalsIgnoreCase("doble") || 
                tipo.equalsIgnoreCase("suite") || 
                tipo.equalsIgnoreCase("familiar"));
    }

    private boolean esEstadoValido(String estado) {
        return estado != null && 
               (estado.equalsIgnoreCase("disponible") || 
                estado.equalsIgnoreCase("ocupada") || 
                estado.equalsIgnoreCase("mantenimiento") || 
                estado.equalsIgnoreCase("reservada"));
    }
}