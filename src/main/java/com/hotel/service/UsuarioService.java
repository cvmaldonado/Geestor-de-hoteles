/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.UsuarioDAO;
import com.hotel.model.Usuario;
import jakarta.ejb.Stateless;

import jakarta.inject.Inject;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Stateless
public class UsuarioService {

    @Inject private UsuarioDAO usuarioDAO;


    // Crear un nuevo usuario
    public void crearUsuario(Usuario usuario) {
        // Validaciones más robustas
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no puede ser nulo");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username no puede estar vacío");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email no puede estar vacío");
        }

        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password no puede estar vacío");
        }

        if (usuario.getRol() == null || !esRolValido(usuario.getRol())) {
            throw new IllegalArgumentException("Rol no válido");
        }

        // Verificar unicidad
        if (usuarioDAO.buscarPorUsername(usuario.getUsername()) != null) {
            throw new IllegalArgumentException("El username ya existe");
        }

        if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("El email ya existe");
        }

        usuarioDAO.crear(usuario);
    }

    private boolean esRolValido(String rol) {
        return rol != null && 
               (rol.equals("administrador") || 
                rol.equals("recepcionista") || 
                rol.equals("gerente") ||
                rol.equals("huesped"));
    }

    // Obtener usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    // Actualizar usuario
    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    // Eliminar usuario
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            usuario.setActivo(false);
            usuarioDAO.actualizar(usuario);
        }
    }

    // Listar todos los usuarios activos
    public List<Usuario> listarUsuariosActivos() {
        List<Usuario> todos = usuarioDAO.listarTodos();
        return todos.stream()
                .filter(usuario -> usuario.getActivo() != null && usuario.getActivo())
                .toList();
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioDAO.listarTodos();
    }

    // Buscar usuario por username
    public Usuario buscarPorUsername(String username) {
        return usuarioDAO.buscarPorUsername(username);
    }

    // Autenticar usuario
    public Usuario autenticarUsuario(String username, String passwordHash) {
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario != null && 
            usuario.getActivo() != null && 
            usuario.getActivo() && 
            usuario.getPasswordHash().equals(passwordHash)) {
            return usuario;
        }
        return null;
    }

    // Verificar si el username existe
    public boolean existeUsername(String username) {
        return usuarioDAO.buscarPorUsername(username) != null;
    }

    // Verificar si el email existe
    public boolean existeEmail(String email) {
        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            return usuario != null;
        } catch (Exception e) {
            return false;
        }
    }
}