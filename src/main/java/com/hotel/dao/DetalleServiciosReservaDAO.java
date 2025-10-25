/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hotel.dao;

import com.hotel.model.DetalleServiciosReserva;
import com.hotel.model.Reserva;
import com.hotel.model.ServicioExtra;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Stateless
public class DetalleServiciosReservaDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager entityManager;

    public void guardar(DetalleServiciosReserva detalle) {
        entityManager.persist(detalle);
    }

    public void actualizar(DetalleServiciosReserva detalle) {
        entityManager.merge(detalle);
    }

    public void eliminar(Long idDetalle) {
        DetalleServiciosReserva detalle = entityManager.find(DetalleServiciosReserva.class, idDetalle);
        if (detalle != null) {
            entityManager.remove(detalle);
        }
    }

    public DetalleServiciosReserva buscarPorId(Long idDetalle) {
        return entityManager.find(DetalleServiciosReserva.class, idDetalle);
    }

    public List<DetalleServiciosReserva> listarTodos() {
        TypedQuery<DetalleServiciosReserva> query = entityManager.createQuery(
                "SELECT d FROM DetalleServiciosReserva d", DetalleServiciosReserva.class);
        return query.getResultList();
    }

    public List<DetalleServiciosReserva> buscarPorReserva(Reserva reserva) {
        TypedQuery<DetalleServiciosReserva> query = entityManager.createQuery(
                "SELECT d FROM DetalleServiciosReserva d WHERE d.reserva = :reserva", DetalleServiciosReserva.class);
        query.setParameter("reserva", reserva);
        return query.getResultList();
    }

    public List<DetalleServiciosReserva> buscarPorServicio(ServicioExtra servicio) {
        TypedQuery<DetalleServiciosReserva> query = entityManager.createQuery(
                "SELECT d FROM DetalleServiciosReserva d WHERE d.servicio = :servicio", DetalleServiciosReserva.class);
        query.setParameter("servicio", servicio);
        return query.getResultList();
    }
}
