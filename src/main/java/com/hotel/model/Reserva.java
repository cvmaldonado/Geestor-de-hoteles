/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author martinmaldonado
 */

@Entity
@Table(name = "reserva")
public class Reserva implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;
    
    @ManyToOne
    @JoinColumn(name = "id_huesped", nullable = false)
    private Huesped huesped;
    
    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;
        
    @Column (name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;
    
    @Column (name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;
    
    @Column (name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;
    
    @Column (name = "numero_huespedes", nullable = false)
    private Integer numeroHuespedes;
    
    @Column (name = "estado_reserva", nullable = false)
    private String estadoReserva;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    // Constructores

    public Reserva() {
        this.fechaReserva = LocalDateTime.now();
        this.estadoReserva = "pendiente";
    }
    
    public Reserva(Huesped huesped, Habitacion habitacion, LocalDate fechaEntrada,
            LocalDate fechaSalida, LocalDateTime fechaReserva, Integer numeroHuespedes,
            String estadoReserva, String observaciones) {
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.fechaReserva = fechaReserva;
        this.numeroHuespedes = numeroHuespedes;
        this.estadoReserva = estadoReserva;
        this.observaciones = observaciones;
        
    }

    public Long getIdReserva() {return idReserva;}
    public void setIdReserva(Long idReserva) {this.idReserva = idReserva;}
    
    public Huesped getHuesped() {return huesped;}
    public void setHuesped(Huesped huesped) {this.huesped = huesped;}

    public Habitacion getHabitacion() {return habitacion;}
    public void setHabitacion(Habitacion habitacion) {this.habitacion = habitacion;}

    public LocalDate getFechaEntrada() {return fechaEntrada;}
    public void setFechaEntrada(LocalDate fechaEntrada) {this.fechaEntrada = fechaEntrada;}

    public LocalDate getFechaSalida() {return fechaSalida;}
    public void setFechaSalida(LocalDate fechaSalida) {this.fechaSalida = fechaSalida;}

    public LocalDateTime getFechaReserva() {return fechaReserva;}
    public void setFechaReserva(LocalDateTime fechaReserva) {this.fechaReserva = fechaReserva;}

    public Integer getNumeroHuespedes() {return numeroHuespedes;}
    public void setNumeroHuespedes(Integer numeroHuespedes) {this.numeroHuespedes = numeroHuespedes;}

    public String getObservaciones() {return observaciones;}
    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}

    public String getEstadoReserva() {return estadoReserva;}
    public void setEstadoReserva(String estadoReserva) {this.estadoReserva = estadoReserva;}
    
    
    
}
