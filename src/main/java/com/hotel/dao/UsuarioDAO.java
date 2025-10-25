/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.dao;

import com.hotel.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import jakarta.persistence.NoResultException;


/**
 *
 * @author martinmaldonado
 */

@Stateless
public class UsuarioDAO {

    @PersistenceContext(unitName = "hotelPU")
    private EntityManager em;

    public void crear(Usuario usuario) {
        em.persist(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

    public void eliminar(Long id) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            em.remove(usuario);
        }
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public Usuario buscarPorUsername(String username) {
    try {
        return em.createQuery(
            "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class)
            .setParameter("username", username)
            .getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
}
    
    public Usuario buscarPorEmail(String email) {
        try {
            return em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
