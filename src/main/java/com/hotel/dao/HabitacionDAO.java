/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.Habitacion;
import com.hotel.model.Hotel;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Stateless
public class HabitacionDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager em;

    public void crear(Habitacion habitacion) {
        em.persist(habitacion);
    }

    public Habitacion buscarPorId(Long id) {
        return em.find(Habitacion.class, id);
    }

    public void actualizar(Habitacion habitacion) {
        em.merge(habitacion);
    }

    public void eliminar(Long id) {
        Habitacion habitacion = buscarPorId(id);
        if (habitacion != null) {
            em.remove(habitacion);
        }
    }

    public List<Habitacion> listarTodas() {
        return em.createQuery("SELECT h FROM Habitacion h", Habitacion.class)
                 .getResultList();
    }

    public List<Habitacion> listarPorHotel(Hotel hotel) {
        return em.createQuery(
                "SELECT h FROM Habitacion h WHERE h.hotel = :hotel", Habitacion.class)
                 .setParameter("hotel", hotel)
                 .getResultList();
    }

    public List<Habitacion> listarPorEstado(String estado) {
        return em.createQuery(
                "SELECT h FROM Habitacion h WHERE LOWER(h.estado) = LOWER(:estado)", Habitacion.class)
                 .setParameter("estado", estado)
                 .getResultList();
    }

    public List<Habitacion> listarDisponiblesPorHotel(Hotel hotel) {
        return em.createQuery(
                "SELECT h FROM Habitacion h WHERE h.hotel = :hotel AND LOWER(h.estado) = 'disponible'",
                Habitacion.class)
                 .setParameter("hotel", hotel)
                 .getResultList();
    }

    public List<Habitacion> buscarPorTipo(String tipoHabitacion) {
        return em.createQuery(
                "SELECT h FROM Habitacion h WHERE LOWER(h.tipoHabitacion) LIKE LOWER(:tipo)",
                Habitacion.class)
                 .setParameter("tipo", "%" + tipoHabitacion + "%")
                 .getResultList();
    }

    public List<Habitacion> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        return em.createQuery(
                "SELECT h FROM Habitacion h WHERE h.precioBase BETWEEN :min AND :max",
                Habitacion.class)
                 .setParameter("min", min)
                 .setParameter("max", max)
                 .getResultList();
    }

    public Habitacion buscarPorNumero(String numero) {
        try {
            return em.createQuery(
                    "SELECT h FROM Habitacion h WHERE h.numero = :numero", Habitacion.class)
                     .setParameter("numero", numero)
                     .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
