/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
/**
 *
 * @author martinmaldonado
 */
@Embeddable
public class HuespedReservaId implements Serializable{
    private Long idHuesped;
    private Long idReserva;

    public HuespedReservaId() {}

    public HuespedReservaId(Long idHuesped, Long idReserva) {
        this.idHuesped = idHuesped;
        this.idReserva = idReserva;
    }
    
    // Getters y Setters
    public Long getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Long idHuesped) { this.idHuesped = idHuesped; }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HuespedReservaId)) return false;
        HuespedReservaId that = (HuespedReservaId) o;
        return Objects.equals(idHuesped, that.idHuesped) &&
               Objects.equals(idReserva, that.idReserva);
    }
    
     @Override
    public int hashCode() {
        return Objects.hash(idHuesped, idReserva);
    }
}
