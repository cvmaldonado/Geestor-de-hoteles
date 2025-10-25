/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.Usuario;
import com.hotel.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    private UsuarioService usuarioService;

    // GET: Obtener todos los usuarios
    @GET
    public Response obtenerTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosLosUsuarios();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los usuarios: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener usuarios activos
    @GET
    @Path("/activos")
    public Response obtenerUsuariosActivos() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuariosActivos();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los usuarios activos: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener usuario por ID
    @GET
    @Path("/{id}")
    public Response obtenerUsuarioPorId(@PathParam("id") Long id) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuario no encontrado")
                        .build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el usuario: " + e.getMessage())
                    .build();
        }
    }

    // POST: Crear nuevo usuario
    @POST
    public Response crearUsuario(Usuario usuario) {
        try {
            usuarioService.crearUsuario(usuario);
            return Response.status(Response.Status.CREATED)
                    .entity("Usuario creado exitosamente")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el usuario: " + e.getMessage())
                    .build();
        }
    }

    // PUT: Actualizar usuario existente
    @PUT
    @Path("/{id}")
    public Response actualizarUsuario(@PathParam("id") Long id, Usuario usuarioActualizado) {
        try {
            // Verificar que el usuario existe
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
            if (usuarioExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuario no encontrado")
                        .build();
            }

            // Actualizar los campos
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
            usuarioExistente.setRol(usuarioActualizado.getRol());
            usuarioExistente.setActivo(usuarioActualizado.getActivo());
            
            // Solo actualizar password si se proporciona uno nuevo
            if (usuarioActualizado.getPasswordHash() != null && 
                !usuarioActualizado.getPasswordHash().trim().isEmpty()) {
                usuarioExistente.setPasswordHash(usuarioActualizado.getPasswordHash());
            }

            usuarioService.actualizarUsuario(usuarioExistente);
            return Response.ok("Usuario actualizado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el usuario: " + e.getMessage())
                    .build();
        }
    }

    // DELETE: Desactivar usuario (eliminación lógica)
    @DELETE
    @Path("/{id}")
    public Response desactivarUsuario(@PathParam("id") Long id) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuario no encontrado")
                        .build();
            }

            usuarioService.desactivarUsuario(id);
            return Response.ok("Usuario desactivado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al desactivar el usuario: " + e.getMessage())
                    .build();
        }
    }

    // POST: Autenticar usuario (login)
    @POST
    @Path("/autenticar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response autenticarUsuario(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        
        try {
            if (username == null || password == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Username y password son requeridos")
                        .build();
            }

            Usuario usuario = usuarioService.autenticarUsuario(username, password);
            if (usuario == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciales inválidas")
                        .build();
            }

            return Response.ok()
                    .entity("Autenticación exitosa. Rol: " + usuario.getRol())
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error en la autenticación: " + e.getMessage())
                    .build();
        }
    }

    // GET: Verificar si username existe
    @GET
    @Path("/verificar-username/{username}")
    public Response verificarUsername(@PathParam("username") String username) {
        try {
            boolean existe = usuarioService.existeUsername(username);
            return Response.ok(existe).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al verificar username: " + e.getMessage())
                    .build();
        }
    }
}