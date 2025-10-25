/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.HotelDAO;
import com.hotel.model.Hotel;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class HotelService {

    @Inject 
    private HotelDAO hotelDAO;

    // Crear un nuevo hotel
    public void crearHotel(Hotel hotel) {
        // Validaciones
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel no puede ser nulo");
        }

        if (hotel.getNombre() == null || hotel.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre del hotel no puede estar vacío");
        }

        if (hotel.getDireccion() == null || hotel.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("Dirección del hotel no puede estar vacía");
        }

        if (hotel.getEstrellas() != null && (hotel.getEstrellas() < 1 || hotel.getEstrellas() > 5)) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5");
        }

        hotelDAO.crear(hotel);
    }

    public Hotel obtenerHotelPorId(Long id) {
        return hotelDAO.buscarPorId(id);
    }

    public void actualizarHotel(Hotel hotel) {
        hotelDAO.actualizar(hotel);
    }

    public void desactivarHotel(Long id) {
        Hotel hotel = hotelDAO.buscarPorId(id);
        if (hotel != null) {
            hotel.setActivo(false);
            hotelDAO.actualizar(hotel);
        }
    }

    public void activarHotel(Long id) {
        Hotel hotel = hotelDAO.buscarPorId(id);
        if (hotel != null) {
            hotel.setActivo(true);
            hotelDAO.actualizar(hotel);
        }
    }

    public List<Hotel> listarTodosLosHoteles() {
        return hotelDAO.listarTodos();
    }

    public List<Hotel> listarHotelesActivos() {
        return hotelDAO.listarActivos();
    }

    public List<Hotel> buscarPorNombre(String nombre) {
        return hotelDAO.buscarPorNombre(nombre);
    }
}