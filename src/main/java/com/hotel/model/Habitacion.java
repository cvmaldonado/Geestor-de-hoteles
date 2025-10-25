/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Entity
@Table(name = "habitacion")

public class Habitacion implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long idHabitacion;
    
    @ManyToOne
    @JoinColumn(name = "id_hotel", nullable = false)
    private Hotel hotel;
    
    @Column(name = "numero", unique = true, nullable = false, length = 10)
    private String numero;
    
    @Column(name = "piso")
    private Integer piso;
    
    @Column(name = "tipo_habitacion", nullable = false, length = 30)
    private String tipoHabitacion;
    
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;
    
    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "estado", length = 20)
    private String estado;
    
    // Campos opcionales por tipo

    // Para habitaciones individuales
    @Column(name = "tiene_escritorio")
    private Boolean tieneEscritorio;

    @Column(name = "tipo_cama")
    private String tipoCama;

    // Para habitaciones dobles
    @Column(name = "numero_camas")
    private Integer numeroCamas;

    @Column(name = "tipo_camas", length = 30)
    private String tipoCamas;
    
     // Constructores
    
    public Habitacion() {
        this.estado = "disponible";
        this.tieneEscritorio = null;
        this.tipoCama = null;
        this.numeroCamas = null;
        this.tipoCamas = null;
    }
        
    
    public Habitacion(Hotel hotel, String numero, String tipoHabitacion, 
                     Integer capacidad, BigDecimal precioBase) {
        this();
        this.hotel = hotel;
        this.numero = numero;
        this.tipoHabitacion = tipoHabitacion;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        
                // Valores por defecto según tipo
        if ("individual".equalsIgnoreCase(tipoHabitacion)) {
            this.tieneEscritorio = true;
            this.tipoCama = "individual";
        } else if ("doble".equalsIgnoreCase(tipoHabitacion)) {
            this.numeroCamas = 2;
            this.tipoCamas = "doble";
        }
    }
    
    // Getters y Setters
    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }
    
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public Integer getPiso() { return piso; }
    public void setPiso(Integer piso) { this.piso = piso; }
    
    public String getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(String tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
    
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    
    public BigDecimal getPrecioBase() { return precioBase; }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Boolean getTieneEscritorio() {return tieneEscritorio;}
    public void setTieneEscritorio(Boolean tieneEscritorio) {this.tieneEscritorio = tieneEscritorio;}

    public String getTipoCama() {return tipoCama;}
    public void setTipoCama(String tipoCama) {this.tipoCama = tipoCama;}

    public Integer getNumeroCamas() {return numeroCamas;}
    public void setNumeroCamas(Integer numeroCamas) {this.numeroCamas = numeroCamas;}

    public String getTipoCamas() {return tipoCamas;}
    public void setTipoCamas(String tipoCamas) {this.tipoCamas = tipoCamas;}
    
}
