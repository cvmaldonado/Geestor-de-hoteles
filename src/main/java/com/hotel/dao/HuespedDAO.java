/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.Huesped;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Stateless
public class HuespedDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager em;

    public void crear(Huesped huesped) {
        em.persist(huesped);
    }

    public Huesped buscarPorId(Long id) {
        return em.find(Huesped.class, id);
    }

    public void actualizar(Huesped huesped) {
        em.merge(huesped);
    }

    public void eliminar(Long id) {
        Huesped huesped = buscarPorId(id);
        if (huesped != null) {
            em.remove(huesped);
        }
    }

    public List<Huesped> listarTodos() {
        return em.createQuery("SELECT h FROM Huesped h", Huesped.class).getResultList();
    }

    public List<Huesped> buscarPorApellido(String apellido) {
        return em.createQuery(
            "SELECT h FROM Huesped h WHERE LOWER(h.apellido) LIKE LOWER(:apellido)", Huesped.class
        )
        .setParameter("apellido", "%" + apellido + "%")
        .getResultList();
    }

    public Huesped buscarPorDocumento(String documentoIdentidad) {
        try {
            return em.createQuery(
                "SELECT h FROM Huesped h WHERE h.documentoIdentidad = :doc", Huesped.class
            )
            .setParameter("doc", documentoIdentidad)
            .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Huesped> listarActivos() {
        return em.createQuery(
            "SELECT h FROM Huesped h WHERE h.activo = TRUE", Huesped.class
        ).getResultList();
    }
    
     public Huesped buscarById(Long id) {
        return em.find(Huesped.class, id);
    }
}
