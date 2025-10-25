/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.HuespedReserva;
import com.hotel.model.HuespedReservaId;
import com.hotel.model.Huesped;
import com.hotel.model.Reserva;
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
public class HuespedReservaDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager entityManager;

    public void guardar(HuespedReserva hr) {
        entityManager.persist(hr);
    }

    public void actualizar(HuespedReserva hr) {
        entityManager.merge(hr);
    }

    public void eliminar(HuespedReservaId id) {
        HuespedReserva hr = entityManager.find(HuespedReserva.class, id);
        if (hr != null) {
            entityManager.remove(hr);
        }
    }

    public void eliminarPorHuespedYReserva(Long idHuesped, Long idReserva) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "DELETE FROM HuespedReserva hr WHERE hr.huesped.idUsuario = :idHuesped AND hr.reserva.idReserva = :idReserva",
                HuespedReserva.class);
        query.setParameter("idHuesped", idHuesped);
        query.setParameter("idReserva", idReserva);
        query.executeUpdate();
    }

    public HuespedReserva buscarPorId(HuespedReservaId id) {
        return entityManager.find(HuespedReserva.class, id);
    }

    public HuespedReserva buscarPorHuespedYReserva(Long idHuesped, Long idReserva) {
        try {
            TypedQuery<HuespedReserva> query = entityManager.createQuery(
                    "SELECT hr FROM HuespedReserva hr WHERE hr.huesped.idUsuario = :idHuesped AND hr.reserva.idReserva = :idReserva",
                    HuespedReserva.class);
            query.setParameter("idHuesped", idHuesped);
            query.setParameter("idReserva", idReserva);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<HuespedReserva> listarPorReserva(Reserva reserva) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr WHERE hr.reserva = :reserva", HuespedReserva.class);
        query.setParameter("reserva", reserva);
        return query.getResultList();
    }

    public List<HuespedReserva> listarPorReservaId(Long idReserva) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr WHERE hr.reserva.idReserva = :idReserva", HuespedReserva.class);
        query.setParameter("idReserva", idReserva);
        return query.getResultList();
    }

    public List<HuespedReserva> listarPorHuesped(Huesped huesped) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr WHERE hr.huesped = :huesped", HuespedReserva.class);
        query.setParameter("huesped", huesped);
        return query.getResultList();
    }

    public List<HuespedReserva> listarPorHuespedId(Long idHuesped) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr WHERE hr.huesped.idUsuario = :idHuesped", HuespedReserva.class);
        query.setParameter("idHuesped", idHuesped);
        return query.getResultList();
    }

    public HuespedReserva buscarTitularPorReserva(Reserva reserva) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr WHERE hr.reserva = :reserva AND hr.esTitular = true",
                HuespedReserva.class);
        query.setParameter("reserva", reserva);
        List<HuespedReserva> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public HuespedReserva buscarTitularPorReservaId(Long idReserva) {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr WHERE hr.reserva.idReserva = :idReserva AND hr.esTitular = true",
                HuespedReserva.class);
        query.setParameter("idReserva", idReserva);
        List<HuespedReserva> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public int contarHuespedesPorReserva(Long idReserva) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(hr) FROM HuespedReserva hr WHERE hr.reserva.idReserva = :idReserva", Long.class);
        query.setParameter("idReserva", idReserva);
        return query.getSingleResult().intValue();
    }

    public List<HuespedReserva> listarTodos() {
        TypedQuery<HuespedReserva> query = entityManager.createQuery(
                "SELECT hr FROM HuespedReserva hr", HuespedReserva.class);
        return query.getResultList();
    }
}