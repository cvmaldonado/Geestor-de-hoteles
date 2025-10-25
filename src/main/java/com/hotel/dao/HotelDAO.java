/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.Hotel;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class HotelDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager em;

    public void crear(Hotel hotel) {
        em.persist(hotel);
    }

    public Hotel buscarPorId(Long id) {
        return em.find(Hotel.class, id);
    }

    public void actualizar(Hotel hotel) {
        em.merge(hotel);
    }

    public void eliminar(Long id) {
        Hotel hotel = buscarPorId(id);
        if (hotel != null) {
            em.remove(hotel);
        }
    }

    public List<Hotel> listarTodos() {
        return em.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();
    }

    public List<Hotel> listarActivos() {
        return em.createQuery(
            "SELECT h FROM Hotel h WHERE h.activo = TRUE", Hotel.class
        ).getResultList();
    }

    public List<Hotel> buscarPorNombre(String nombre) {
        return em.createQuery(
            "SELECT h FROM Hotel h WHERE LOWER(h.nombre) LIKE LOWER(:nombre)", Hotel.class
        )
        .setParameter("nombre", "%" + nombre + "%")
        .getResultList();
    }
}
