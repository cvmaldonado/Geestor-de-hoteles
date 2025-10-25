/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author martinmaldonado
 */
@Entity
@Table(name = "detalleserviciosreserva")

public class DetalleServiciosReserva implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;
    
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
    
    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioExtra servicio;
    
    @Column (name = "fecha_servicio", nullable = false)
    private LocalDate fechaServicio;
    
    @Column (name = "cantidad")
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    // Constructores
    public DetalleServiciosReserva(){
        this.cantidad = 1;
        
    }
    

    public DetalleServiciosReserva(Reserva reserva, ServicioExtra servicio,
            LocalDate fechaServicio, Integer cantidad, BigDecimal precio_unitario,
            BigDecimal subtotal, String observaciones) {
        this.reserva = reserva;
        this.servicio = servicio;
        this.fechaServicio = fechaServicio;
        this.cantidad = cantidad;
        this.precioUnitario = precio_unitario;
        this.subtotal = subtotal;
        this.observaciones = observaciones;
    }

    public Long getIdDetalle() {return idDetalle;}
    public void setIdDetalle(Long idDetalle) {this.idDetalle = idDetalle;}

    public Reserva getReserva() {return reserva;}
    public void setReserva(Reserva reserva) {this.reserva = reserva;}

    public ServicioExtra getServicio() {return servicio;}
    public void setServicio(ServicioExtra servicio) {this.servicio = servicio;}

    public LocalDate getFechaServicio() {return fechaServicio;}
    public void setFechaServicio(LocalDate fechaServicio) {this.fechaServicio = fechaServicio;}

    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}

    public BigDecimal getPrecioUnitario() {return precioUnitario;}
    public void setPrecioUnitario(BigDecimal precioUnitario) {this.precioUnitario = precioUnitario;}

    public BigDecimal getSubtotal() { return subtotal;} // solo lectura

    public String getObservaciones() {return observaciones;}
    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}
    
}
