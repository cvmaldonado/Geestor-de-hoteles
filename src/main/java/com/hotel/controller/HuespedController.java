/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.Huesped;
import com.hotel.service.HuespedService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Path("/huespedes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HuespedController {

    @Inject
    private HuespedService huespedService;

    // GET: Obtener todos los huéspedes
    @GET
    public Response obtenerTodosLosHuespedes() {
        try {
            List<Huesped> huespedes = huespedService.listarTodosLosHuespedes();
            return Response.ok(huespedes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los huéspedes: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener huéspedes activos
    @GET
    @Path("/activos")
    public Response obtenerHuespedesActivos() {
        try {
            List<Huesped> huespedes = huespedService.listarHuespedesActivos();
            return Response.ok(huespedes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los huéspedes activos: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener huésped por ID
    @GET
    @Path("/{id}")
    public Response obtenerHuespedPorId(@PathParam("id") Long id) {
        try {
            Huesped huesped = huespedService.obtenerHuespedPorId(id);
            if (huesped == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Huésped no encontrado")
                        .build();
            }
            return Response.ok(huesped).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el huésped: " + e.getMessage())
                    .build();
        }
    }

    // GET: Buscar huéspedes por apellido
    @GET
    @Path("/buscar")
    public Response buscarHuespedesPorApellido(@QueryParam("apellido") String apellido) {
        try {
            if (apellido == null || apellido.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El parámetro 'apellido' es requerido")
                        .build();
            }
            
            List<Huesped> huespedes = huespedService.buscarPorApellido(apellido);
            return Response.ok(huespedes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar huéspedes: " + e.getMessage())
                    .build();
        }
    }

    // POST: Crear nuevo huésped
    @POST
    public Response crearHuesped(Huesped huesped) {
        try {
            huespedService.crearHuesped(huesped);
            return Response.status(Response.Status.CREATED)
                    .entity("Huésped creado exitosamente")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el huésped: " + e.getMessage())
                    .build();
        }
    }

    // PUT: Actualizar huésped existente
    @PUT
    @Path("/{id}")
    public Response actualizarHuesped(@PathParam("id") Long id, Huesped huespedActualizado) {
        try {
            // Verificar que el huésped existe
            Huesped huespedExistente = huespedService.obtenerHuespedPorId(id);
            if (huespedExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Huésped no encontrado")
                        .build();
            }

            // Actualizar los campos específicos de huésped
            huespedExistente.setNombre(huespedActualizado.getNombre());
            huespedExistente.setApellido(huespedActualizado.getApellido());
            huespedExistente.setTelefono(huespedActualizado.getTelefono());
            huespedExistente.setDireccion(huespedActualizado.getDireccion());
            huespedExistente.setDocumentoIdentidad(huespedActualizado.getDocumentoIdentidad());
            huespedExistente.setTipoDocumento(huespedActualizado.getTipoDocumento());
            huespedExistente.setFechaNacimiento(huespedActualizado.getFechaNacimiento());
            huespedExistente.setNacionalidad(huespedActualizado.getNacionalidad());

            // Actualizar campos heredados de Usuario
            huespedExistente.setUsername(huespedActualizado.getUsername());
            huespedExistente.setEmail(huespedActualizado.getEmail());
            huespedExistente.setActivo(huespedActualizado.getActivo());
            
            // Solo actualizar password si se proporciona uno nuevo
            if (huespedActualizado.getPasswordHash() != null && 
                !huespedActualizado.getPasswordHash().trim().isEmpty()) {
                huespedExistente.setPasswordHash(huespedActualizado.getPasswordHash());
            }

            huespedService.actualizarHuesped(huespedExistente);
            return Response.ok("Huésped actualizado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el huésped: " + e.getMessage())
                    .build();
        }
    }

    // DELETE: Desactivar huésped (eliminación lógica)
    @DELETE
    @Path("/{id}")
    public Response desactivarHuesped(@PathParam("id") Long id) {
        try {
            Huesped huesped = huespedService.obtenerHuespedPorId(id);
            if (huesped == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Huésped no encontrado")
                        .build();
            }

            huespedService.desactivarHuesped(id);
            return Response.ok("Huésped desactivado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al desactivar el huésped: " + e.getMessage())
                    .build();
        }
    }

    // GET: Verificar si documento de identidad existe
    @GET
    @Path("/verificar-documento/{documento}")
    public Response verificarDocumento(@PathParam("documento") String documento) {
        try {
            boolean existe = huespedService.existeDocumentoIdentidad(documento);
            return Response.ok(existe).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al verificar documento: " + e.getMessage())
                    .build();
        }
    }
}