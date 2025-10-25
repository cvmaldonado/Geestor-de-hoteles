/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 *
 * @author martinmaldonado
 */

@Entity
@Table(name = "huespedreserva")
public class HuespedReserva implements Serializable{
    @EmbeddedId
    private HuespedReservaId id;
    
    @ManyToOne
    @MapsId("idHuesped")
    @JoinColumn(name = "id_huesped", nullable = false)
    private Huesped huesped;
    
    @ManyToOne
    @MapsId("idReserva")
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
    
    @Column(name = "es_titular", nullable = false)
    private Boolean esTitular = false;
    
    // Constructores
    public HuespedReserva() {}

    public HuespedReserva(Huesped huesped, Reserva reserva, Boolean esTitular) {
        this.huesped = huesped;
        this.reserva = reserva;
        this.id = new HuespedReservaId(huesped.getIdUsuario(), reserva.getIdReserva()); 
        this.esTitular = esTitular;
    }

    // Getters y Setters
    public HuespedReservaId getId() { return id; }
    public void setId(HuespedReservaId id) { this.id = id; }

    public Huesped getHuesped() { return huesped; }
    public void setHuesped(Huesped huesped) { this.huesped = huesped; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    public Boolean getEsTitular() { return esTitular; }
    public void setEsTitular(Boolean esTitular) { this.esTitular = esTitular; }
    
}


