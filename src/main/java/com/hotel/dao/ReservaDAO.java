/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.Reserva;
import com.hotel.model.Habitacion;
import com.hotel.model.Huesped;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Stateless
public class ReservaDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager em;

    public void crear(Reserva reserva) {
        em.persist(reserva);
    }

    public Reserva buscarPorId(Long id) {
        return em.find(Reserva.class, id);
    }

    public void actualizar(Reserva reserva) {
        em.merge(reserva);
    }

    public void eliminar(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva != null) {
            em.remove(reserva);
        }
    }

    public List<Reserva> listarTodas() {
        return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
    }

    // --- Consultas personalizadas ---

    public List<Reserva> listarPorHuesped(Huesped huesped) {
        return em.createQuery(
                "SELECT r FROM Reserva r WHERE r.huesped = :huesped ORDER BY r.fechaEntrada DESC",
                Reserva.class)
                .setParameter("huesped", huesped)
                .getResultList();
    }

    public List<Reserva> listarPorHabitacion(Habitacion habitacion) {
        return em.createQuery(
                "SELECT r FROM Reserva r WHERE r.habitacion = :habitacion ORDER BY r.fechaEntrada DESC",
                Reserva.class)
                .setParameter("habitacion", habitacion)
                .getResultList();
    }

    public List<Reserva> listarPorEstado(String estadoReserva) {
        return em.createQuery(
                "SELECT r FROM Reserva r WHERE LOWER(r.estadoReserva) = LOWER(:estado)",
                Reserva.class)
                .setParameter("estado", estadoReserva)
                .getResultList();
    }

    public List<Reserva> listarEntreFechas(LocalDate inicio, LocalDate fin) {
        return em.createQuery(
                "SELECT r FROM Reserva r WHERE r.fechaEntrada >= :inicio AND r.fechaSalida <= :fin",
                Reserva.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
    }

    public List<Reserva> listarReservasActivas() {
        return em.createQuery(
                "SELECT r FROM Reserva r WHERE LOWER(r.estadoReserva) IN ('pendiente', 'confirmada')",
                Reserva.class)
                .getResultList();
    }

    public boolean existeReservaEnRango(Habitacion habitacion, LocalDate entrada, LocalDate salida) {
        Long count = em.createQuery(
                "SELECT COUNT(r) FROM Reserva r " +
                "WHERE r.habitacion = :habitacion " +
                "AND r.estadoReserva IN ('pendiente', 'confirmada') " +
                "AND (r.fechaEntrada <= :salida AND r.fechaSalida >= :entrada)", Long.class)
                .setParameter("habitacion", habitacion)
                .setParameter("entrada", entrada)
                .setParameter("salida", salida)
                .getSingleResult();
        return count > 0;
    }

}
