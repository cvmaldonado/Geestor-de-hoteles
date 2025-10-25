/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.ServicioExtraDAO;
import com.hotel.model.ServicioExtra;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class ServicioExtraService {

    @Inject
    private ServicioExtraDAO servicioExtraDAO;

    public ServicioExtra crearServicio(ServicioExtra servicio) {
        validarServicio(servicio);
        
        // Validaciones específicas por tipo de servicio
        validarCamposPorTipo(servicio);
        
        servicioExtraDAO.guardar(servicio);
        return servicio;
    }

    public ServicioExtra actualizarServicio(Long id, ServicioExtra servicioActualizado) {
        ServicioExtra servicioExistente = servicioExtraDAO.buscarPorId(id);
        if (servicioExistente == null) {
            throw new IllegalArgumentException("Servicio extra no encontrado con ID: " + id);
        }

        validarServicio(servicioActualizado);
        validarCamposPorTipo(servicioActualizado);

        // Actualizar campos
        servicioExistente.setNombre(servicioActualizado.getNombre());
        servicioExistente.setDescripcion(servicioActualizado.getDescripcion());
        servicioExistente.setPrecio(servicioActualizado.getPrecio());
        servicioExistente.setTipoServicio(servicioActualizado.getTipoServicio());
        servicioExistente.setCategoriaGasto(servicioActualizado.getCategoriaGasto());
        servicioExistente.setProveedor(servicioActualizado.getProveedor());
        servicioExistente.setCategoriaCargo(servicioActualizado.getCategoriaCargo());
        servicioExistente.setAplicaHabitacion(servicioActualizado.getAplicaHabitacion());

        servicioExtraDAO.actualizar(servicioExistente);
        return servicioExistente;
    }

    public void eliminarServicio(Long id) {
        ServicioExtra servicio = servicioExtraDAO.buscarPorId(id);
        if (servicio == null) {
            throw new IllegalArgumentException("Servicio extra no encontrado con ID: " + id);
        }
        servicioExtraDAO.eliminar(id);
    }

    public ServicioExtra buscarPorId(Long id) {
        return servicioExtraDAO.buscarPorId(id);
    }

    public List<ServicioExtra> listarTodos() {
        return servicioExtraDAO.listarTodos();
    }

    public List<ServicioExtra> buscarPorTipo(String tipo) {
        if (!esTipoValido(tipo)) {
            throw new IllegalArgumentException("Tipo de servicio no válido: " + tipo);
        }
        return servicioExtraDAO.buscarPorTipo(tipo);
    }

    public List<ServicioExtra> buscarPorPrecioRango(BigDecimal precioMin, BigDecimal precioMax) {
        // Implementación adicional si necesitas búsqueda por rango de precios
        return servicioExtraDAO.listarTodos().stream()
                .filter(servicio -> servicio.getPrecio().compareTo(precioMin) >= 0 &&
                                   servicio.getPrecio().compareTo(precioMax) <= 0)
                .toList();
    }

    private void validarServicio(ServicioExtra servicio) {
        if (servicio.getNombre() == null || servicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio es obligatorio");
        }

        if (servicio.getPrecio() == null || servicio.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        if (servicio.getTipoServicio() == null || !esTipoValido(servicio.getTipoServicio())) {
            throw new IllegalArgumentException("Tipo de servicio no válido. Debe ser: gasto, anticipo o cargo");
        }
    }

    private void validarCamposPorTipo(ServicioExtra servicio) {
        String tipo = servicio.getTipoServicio().toLowerCase();
        
        switch (tipo) {
            case "gasto":
                if (servicio.getCategoriaGasto() == null || servicio.getCategoriaGasto().trim().isEmpty()) {
                    throw new IllegalArgumentException("La categoría de gasto es obligatoria para servicios de tipo 'gasto'");
                }
                // Limpiar campos no aplicables
                servicio.setCategoriaCargo(null);
                servicio.setAplicaHabitacion(null);
                break;
                
            case "cargo":
                if (servicio.getCategoriaCargo() == null || servicio.getCategoriaCargo().trim().isEmpty()) {
                    throw new IllegalArgumentException("La categoría de cargo es obligatoria para servicios de tipo 'cargo'");
                }
                // Aplica habitación no puede ser nulo para cargos
                if (servicio.getAplicaHabitacion() == null) {
                    servicio.setAplicaHabitacion(true);
                }
                // Limpiar campos no aplicables
                servicio.setCategoriaGasto(null);
                servicio.setProveedor(null);
                break;
                
            case "anticipo":
                // Para anticipos, limpiar campos específicos de otros tipos
                servicio.setCategoriaGasto(null);
                servicio.setProveedor(null);
                servicio.setCategoriaCargo(null);
                servicio.setAplicaHabitacion(null);
                break;
        }
    }

    private boolean esTipoValido(String tipo) {
        return tipo != null && 
               (tipo.equalsIgnoreCase("gasto") || 
                tipo.equalsIgnoreCase("anticipo") || 
                tipo.equalsIgnoreCase("cargo"));
    }
}