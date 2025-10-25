/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.web.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class DashboardBean implements Serializable {
    private static final Logger logger = Logger.getLogger(DashboardBean.class.getName());
    
    @Inject
    private LoginBean loginBean;
    
    private Integer totalHabitaciones;
    private Integer habitacionesDisponibles;
    private Integer reservasActivas;
    private Integer reservasHoy;
    private Integer huespedesActivos;
    
    private final String baseURL = "http://localhost:8080/HotelS/api";
    
    public void cargarDatos() {
        try {
            Client client = ClientBuilder.newClient();
            
            // Cargar datos de habitaciones
            cargarDatosHabitaciones(client);
            
            // Cargar datos de reservas
            cargarDatosReservas(client);
            
            // Cargar datos de huéspedes
            cargarDatosHuespedes(client);
            
            client.close();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando datos del dashboard: " + e.getMessage(), e);
            // Valores por defecto en caso de error
            establecerValoresPorDefecto();
        }
    }
    
    private void cargarDatosHabitaciones(Client client) {
        try {
            // Obtener habitaciones disponibles
            Response responseDisponibles = client.target(baseURL + "/habitaciones/estado/disponible")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (responseDisponibles.getStatus() == 200) {
                List<Object> disponibles = responseDisponibles.readEntity(new GenericType<List<Object>>() {});
                this.habitacionesDisponibles = disponibles.size();
            }
            
            // Obtener total de habitaciones (puedes necesitar otro endpoint)
            Response responseTotal = client.target(baseURL + "/habitaciones")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (responseTotal.getStatus() == 200) {
                List<Object> total = responseTotal.readEntity(new GenericType<List<Object>>() {});
                this.totalHabitaciones = total.size();
            }
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error cargando datos de habitaciones: " + e.getMessage());
            this.totalHabitaciones = 0;
            this.habitacionesDisponibles = 0;
        }
    }
    
    private void cargarDatosReservas(Client client) {
        try {
            Response response = client.target(baseURL + "/reservas")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                List<Object> reservas = response.readEntity(new GenericType<List<Object>>() {});
                this.reservasActivas = reservas.size(); // Ajusta según tu lógica de reservas activas
                this.reservasHoy = 0; // Necesitarías un endpoint específico para reservas de hoy
            }
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error cargando datos de reservas: " + e.getMessage());
            this.reservasActivas = 0;
            this.reservasHoy = 0;
        }
    }
    
    private void cargarDatosHuespedes(Client client) {
        try {
            Response response = client.target(baseURL + "/huespedes")
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                List<Object> huespedes = response.readEntity(new GenericType<List<Object>>() {});
                this.huespedesActivos = huespedes.size(); // Ajusta según tu lógica de huéspedes activos
            }
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error cargando datos de huéspedes: " + e.getMessage());
            this.huespedesActivos = 0;
        }
    }
    
    private void establecerValoresPorDefecto() {
        this.totalHabitaciones = 0;
        this.habitacionesDisponibles = 0;
        this.reservasActivas = 0;
        this.reservasHoy = 0;
        this.huespedesActivos = 0;
    }
    
    // Getters
    public Integer getTotalHabitaciones() {
        if (totalHabitaciones == null) {
            cargarDatos();
        }
        return totalHabitaciones;
    }
    
    public Integer getHabitacionesDisponibles() {
        if (habitacionesDisponibles == null) {
            cargarDatos();
        }
        return habitacionesDisponibles;
    }
    
    public Integer getReservasActivas() {
        if (reservasActivas == null) {
            cargarDatos();
        }
        return reservasActivas;
    }
    
    public Integer getReservasHoy() {
        if (reservasHoy == null) {
            cargarDatos();
        }
        return reservasHoy;
    }
    
    public Integer getHuespedesActivos() {
        if (huespedesActivos == null) {
            cargarDatos();
        }
        return huespedesActivos;
    }
}