/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.web.bean;

import com.hotel.model.Habitacion;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class HabitacionBean implements Serializable {
    private static final Logger logger = Logger.getLogger(HabitacionBean.class.getName());
    
    private final String baseURL = "http://localhost:8080/HotelS/api";
    
    private List<Habitacion> habitaciones;
    private Habitacion habitacionSeleccionada;
    private boolean modoEdicion = false;
    
    // Método para cargar todas las habitaciones
    public void cargarHabitaciones() {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/habitaciones")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                this.habitaciones = response.readEntity(new GenericType<List<Habitacion>>() {});
                logger.log(Level.INFO, "Habitaciones cargadas: " + this.habitaciones.size());
            } else {
                logger.log(Level.WARNING, "Error al cargar habitaciones: " + response.getStatus());
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando habitaciones: " + e.getMessage(), e);
        }
    }
    
    // Método para preparar nueva habitación
    public void prepararNuevaHabitacion() {
        this.habitacionSeleccionada = new Habitacion();
        this.modoEdicion = true;
    }
    
    // Método para editar habitación
    public void editarHabitacion(Habitacion habitacion) {
        this.habitacionSeleccionada = habitacion;
        this.modoEdicion = true;
    }
    
    // Método para guardar habitación (crear o actualizar)
    public void guardarHabitacion() {
        try {
            Client client = ClientBuilder.newClient();
            Response response;
            
            if (habitacionSeleccionada.getIdHabitacion() == null) {
                // Crear nueva habitación
                response = client.target(baseURL + "/habitaciones")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(habitacionSeleccionada, MediaType.APPLICATION_JSON));
                
                if (response.getStatus() == 201) {
                    logger.log(Level.INFO, "Habitación creada exitosamente");
                }
            } else {
                // Actualizar habitación existente
                response = client.target(baseURL + "/habitaciones/" + habitacionSeleccionada.getIdHabitacion())
                        .request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity(habitacionSeleccionada, MediaType.APPLICATION_JSON));
                
                if (response.getStatus() == 200) {
                    logger.log(Level.INFO, "Habitación actualizada exitosamente");
                }
            }
            
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                cargarHabitaciones(); // Recargar la lista
                this.modoEdicion = false;
                this.habitacionSeleccionada = null;
            } else {
                logger.log(Level.WARNING, "Error en la respuesta del servidor: " + response.getStatus());
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error guardando habitación: " + e.getMessage(), e);
        }
    }
    
    // Método para eliminar habitación
    public void eliminarHabitacion(Habitacion habitacion) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/habitaciones/" + habitacion.getIdHabitacion())
                    .request(MediaType.APPLICATION_JSON)
                    .delete();
            
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, "Habitación eliminada exitosamente");
                cargarHabitaciones(); // Recargar la lista
            } else {
                logger.log(Level.WARNING, "Error al eliminar habitación: " + response.getStatus());
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error eliminando habitación: " + e.getMessage(), e);
        }
    }
    
    // Método para cambiar estado de habitación
    public void cambiarEstadoHabitacion(Habitacion habitacion, String nuevoEstado) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/habitaciones/" + habitacion.getIdHabitacion() + "/estado")
                    .queryParam("estado", nuevoEstado)
                    .request(MediaType.APPLICATION_JSON)
                    .method("PATCH");
            
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, "Estado de habitación actualizado a: " + nuevoEstado);
                cargarHabitaciones(); // Recargar la lista
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cambiando estado de habitación: " + e.getMessage(), e);
        }
    }
    
    // Método para cancelar edición
    public void cancelarEdicion() {
        this.modoEdicion = false;
        this.habitacionSeleccionada = null;
    }
    
    // Getters y Setters
    public List<Habitacion> getHabitaciones() {
        if (habitaciones == null) {
            cargarHabitaciones();
        }
        return habitaciones;
    }
    
    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }
    
    public Habitacion getHabitacionSeleccionada() {
        return habitacionSeleccionada;
    }
    
    public void setHabitacionSeleccionada(Habitacion habitacionSeleccionada) {
        this.habitacionSeleccionada = habitacionSeleccionada;
    }
    
    public boolean isModoEdicion() {
        return modoEdicion;
    }
    
    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }
}