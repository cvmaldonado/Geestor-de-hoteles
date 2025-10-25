/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.HuespedDAO;
import com.hotel.model.Huesped;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class HuespedService {

    @Inject 
    private HuespedDAO huespedDAO;

    // Crear un nuevo huésped
    public void crearHuesped(Huesped huesped) {
        
        if (huesped == null) {
            throw new IllegalArgumentException("Huésped no puede ser nulo");
        }

        if (huesped.getNombre() == null || huesped.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }

        if (huesped.getApellido() == null || huesped.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido no puede estar vacío");
        }

        if (huesped.getDocumentoIdentidad() == null || huesped.getDocumentoIdentidad().trim().isEmpty()) {
            throw new IllegalArgumentException("Documento de identidad no puede estar vacío");
        }

        if (huesped.getUsername() == null || huesped.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username no puede estar vacío");
        }

        if (huesped.getEmail() == null || huesped.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email no puede estar vacío");
        }

        if (huesped.getPasswordHash() == null || huesped.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password no puede estar vacío");
        }

        // Verificar unicidad del documento de identidad
        Huesped existentePorDocumento = huespedDAO.buscarPorDocumento(huesped.getDocumentoIdentidad());
        if (existentePorDocumento != null) {
            throw new IllegalArgumentException("El documento de identidad ya existe");
        }

        // Asegurar que el rol sea "huesped"
        huesped.setRol("huesped");

        huespedDAO.crear(huesped);
    }

    // Obtener huésped por ID
    public Huesped obtenerHuespedPorId(Long id) {
        return huespedDAO.buscarPorId(id);
    }

    // Actualizar huésped
    public void actualizarHuesped(Huesped huesped) {
        huespedDAO.actualizar(huesped);
    }

    // Desactivar huésped (eliminación lógica)
    public void desactivarHuesped(Long id) {
        Huesped huesped = huespedDAO.buscarPorId(id);
        if (huesped != null) {
            huesped.setActivo(false);
            huespedDAO.actualizar(huesped);
        }
    }

    // Listar todos los huéspedes
    public List<Huesped> listarTodosLosHuespedes() {
        return huespedDAO.listarTodos();
    }

    // Listar huéspedes activos
    public List<Huesped> listarHuespedesActivos() {
        return huespedDAO.listarActivos();
    }

    // Buscar huésped por documento de identidad
    public Huesped buscarPorDocumento(String documentoIdentidad) {
        return huespedDAO.buscarPorDocumento(documentoIdentidad);
    }

    // Buscar huéspedes por apellido
    public List<Huesped> buscarPorApellido(String apellido) {
        return huespedDAO.buscarPorApellido(apellido);
    }

    // Verificar si existe documento de identidad
    public boolean existeDocumentoIdentidad(String documentoIdentidad) {
        return huespedDAO.buscarPorDocumento(documentoIdentidad) != null;
    }
}
