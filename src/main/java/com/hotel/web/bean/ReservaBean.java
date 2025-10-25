/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.web.bean;

import com.hotel.model.Reserva;
import com.hotel.model.Huesped;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class ReservaBean implements Serializable {
    private static final Logger logger = Logger.getLogger(ReservaBean.class.getName());
    
    private final String baseURL = "http://localhost:8080/HotelS/api";
    
    private List<Reserva> reservas;
    private List<Huesped> huespedes;
    private List<Habitacion> habitaciones;
    private Reserva reservaSeleccionada;
    private boolean modoEdicion = false;
    
    // Método para cargar todas las reservas
    public void cargarReservas() {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/reservas")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                this.reservas = response.readEntity(new GenericType<List<Reserva>>() {});
                logger.log(Level.INFO, "Reservas cargadas: " + this.reservas.size());
            } else {
                logger.log(Level.WARNING, "Error al cargar reservas: " + response.getStatus());
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando reservas: " + e.getMessage(), e);
        }
    }
    
    // Método para cargar huéspedes (para el combobox)
    public void cargarHuespedes() {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/huespedes")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                this.huespedes = response.readEntity(new GenericType<List<Huesped>>() {});
            }
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando huéspedes: " + e.getMessage(), e);
        }
    }
    
    // Método para cargar habitaciones disponibles
    public void cargarHabitacionesDisponibles() {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/habitaciones")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                this.habitaciones = response.readEntity(new GenericType<List<Habitacion>>() {});
            }
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando habitaciones: " + e.getMessage(), e);
        }
    }
    
    // Método para preparar nueva reserva
    public void prepararNuevaReserva() {
        this.reservaSeleccionada = new Reserva();
        this.reservaSeleccionada.setFechaReserva(LocalDateTime.now());
        this.reservaSeleccionada.setEstadoReserva("pendiente");
        this.modoEdicion = true;
        cargarHuespedes();
        cargarHabitacionesDisponibles();
    }
    
    // Método para editar reserva
    public void editarReserva(Reserva reserva) {
        this.reservaSeleccionada = reserva;
        this.modoEdicion = true;
        cargarHuespedes();
        cargarHabitacionesDisponibles();
    }
    
    // Método para guardar reserva (crear o actualizar)
    public void guardarReserva() {
        try {
            Client client = ClientBuilder.newClient();
            Response response;
            
            if (reservaSeleccionada.getIdReserva() == null) {
                // Crear nueva reserva
                response = client.target(baseURL + "/reservas")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(reservaSeleccionada, MediaType.APPLICATION_JSON));
                
                if (response.getStatus() == 201) {
                    logger.log(Level.INFO, "Reserva creada exitosamente");
                }
            } else {
                // Actualizar reserva existente
                response = client.target(baseURL + "/reservas/" + reservaSeleccionada.getIdReserva())
                        .request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity(reservaSeleccionada, MediaType.APPLICATION_JSON));
                
                if (response.getStatus() == 200) {
                    logger.log(Level.INFO, "Reserva actualizada exitosamente");
                }
            }
            
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                cargarReservas(); // Recargar la lista
                this.modoEdicion = false;
                this.reservaSeleccionada = null;
            } else {
                logger.log(Level.WARNING, "Error en la respuesta del servidor: " + response.getStatus());
                // Manejar errores de validación
                if (response.getStatus() == 400) {
                    // Mostrar mensaje de error al usuario
                }
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error guardando reserva: " + e.getMessage(), e);
        }
    }
    
    // Método para eliminar reserva
    public void eliminarReserva(Reserva reserva) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/reservas/" + reserva.getIdReserva())
                    .request(MediaType.APPLICATION_JSON)
                    .delete();
            
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, "Reserva eliminada exitosamente");
                cargarReservas(); // Recargar la lista
            } else {
                logger.log(Level.WARNING, "Error al eliminar reserva: " + response.getStatus());
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error eliminando reserva: " + e.getMessage(), e);
        }
    }
    
    // Método para confirmar reserva
    public void confirmarReserva(Reserva reserva) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/reservas/" + reserva.getIdReserva() + "/confirmar")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(""));
            
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, "Reserva confirmada exitosamente");
                cargarReservas(); // Recargar la lista
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error confirmando reserva: " + e.getMessage(), e);
        }
    }
    
    // Método para cancelar reserva
    public void cancelarReserva(Reserva reserva) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(baseURL + "/reservas/" + reserva.getIdReserva() + "/cancelar")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(""));
            
            if (response.getStatus() == 200) {
                logger.log(Level.INFO, "Reserva cancelada exitosamente");
                cargarReservas(); // Recargar la lista
            }
            
            client.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cancelando reserva: " + e.getMessage(), e);
        }
    }
    
    // Método para cancelar edición
    public void cancelarEdicion() {
        this.modoEdicion = false;
        this.reservaSeleccionada = null;
    }
    
    // Getters y Setters
    public List<Reserva> getReservas() {
        if (reservas == null) {
            cargarReservas();
        }
        return reservas;
    }
    
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    
    public Reserva getReservaSeleccionada() {
        return reservaSeleccionada;
    }
    
    public void setReservaSeleccionada(Reserva reservaSeleccionada) {
        this.reservaSeleccionada = reservaSeleccionada;
    }
    
    public boolean isModoEdicion() {
        return modoEdicion;
    }
    
    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }
    
    public List<Huesped> getHuespedes() {
        return huespedes;
    }
    
    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }
}