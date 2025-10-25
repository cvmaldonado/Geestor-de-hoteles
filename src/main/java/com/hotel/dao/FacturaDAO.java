/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.Factura;
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
public class FacturaDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager entityManager;

    public void guardar(Factura factura) {
        entityManager.persist(factura);
    }

    public void actualizar(Factura factura) {
        entityManager.merge(factura);
    }

    public void eliminar(Long idFactura) {
        Factura factura = entityManager.find(Factura.class, idFactura);
        if (factura != null) {
            entityManager.remove(factura);
        }
    }

    public Factura buscarPorId(Long idFactura) {
        return entityManager.find(Factura.class, idFactura);
    }

    public Factura buscarPorNumero(String numeroFactura) {
        try {
            TypedQuery<Factura> query = entityManager.createQuery(
                    "SELECT f FROM Factura f WHERE f.numeroFactura = :numero", Factura.class);
            query.setParameter("numero", numeroFactura);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Factura> listarTodas() {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f ORDER BY f.fechaEmision DESC", Factura.class);
        return query.getResultList();
    }

    public List<Factura> listarPorReserva(Reserva reserva) {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f WHERE f.reserva = :reserva ORDER BY f.fechaEmision DESC", Factura.class);
        query.setParameter("reserva", reserva);
        return query.getResultList();
    }

    public List<Factura> listarPorEstado(String estado) {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f WHERE f.estadoFactura = :estado ORDER BY f.fechaEmision DESC", Factura.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    public boolean existeNumeroFactura(String numeroFactura) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(f) FROM Factura f WHERE f.numeroFactura = :numero", Long.class);
        query.setParameter("numero", numeroFactura);
        return query.getSingleResult() > 0;
    }
}