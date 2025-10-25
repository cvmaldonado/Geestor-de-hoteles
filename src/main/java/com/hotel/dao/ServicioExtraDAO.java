/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

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
public class ServicioExtraDAO {

    @PersistenceContext(unitName = "hotelPU") // usa el mismo nombre que en tus otros DAOs
    private EntityManager em;

    public void guardar(ServicioExtra servicio) {
        em.persist(servicio);
    }

    public void actualizar(ServicioExtra servicio) {
        em.merge(servicio);
    }

    public void eliminar(Long idServicio) {
        ServicioExtra servicio = em.find(ServicioExtra.class, idServicio);
        if (servicio != null) {
            em.remove(servicio);
        }
    }

    public ServicioExtra buscarPorId(Long idServicio) {
        return em.find(ServicioExtra.class, idServicio);
    }

    public List<ServicioExtra> listarTodos() {
        return em.createQuery("SELECT s FROM ServicioExtra s", ServicioExtra.class)
                 .getResultList();
    }

    public List<ServicioExtra> buscarPorTipo(String tipo) {
        TypedQuery<ServicioExtra> query = em.createQuery(
                "SELECT s FROM ServicioExtra s WHERE s.tipoServicio = :tipo", ServicioExtra.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }
}
