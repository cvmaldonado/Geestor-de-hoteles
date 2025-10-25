/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.ServicioExtra;
import com.hotel.service.ServicioExtraService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author martinmaldonado
 */
@Path("/servicios-extra")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioExtraController {

    @Inject
    private ServicioExtraService servicioExtraService;

    @POST
    public Response crearServicio(ServicioExtra servicio) {
        try {
            ServicioExtra nuevoServicio = servicioExtraService.crearServicio(servicio);
            return Response.status(Response.Status.CREATED).entity(nuevoServicio).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (jakarta.ejb.EJBException e) {
            Throwable causa = e.getCause();
            if (causa instanceof IllegalArgumentException) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(crearErrorResponse(causa.getMessage()))
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(crearErrorResponse("Error interno del servidor: " + e.getMessage()))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error interno del servidor"))
                    .build();
        }
    }

    @GET
    public List<ServicioExtra> listarTodos() {
        return servicioExtraService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response obtenerServicio(@PathParam("id") Long id) {
        try {
            ServicioExtra servicio = servicioExtraService.buscarPorId(id);
            if (servicio != null) {
                return Response.ok(servicio).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(crearErrorResponse("Servicio no encontrado"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener el servicio"))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarServicio(@PathParam("id") Long id, ServicioExtra servicio) {
        try {
            ServicioExtra servicioActualizado = servicioExtraService.actualizarServicio(id, servicio);
            return Response.ok(servicioActualizado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al actualizar el servicio"))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarServicio(@PathParam("id") Long id) {
        try {
            servicioExtraService.eliminarServicio(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Servicio eliminado correctamente");
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al eliminar el servicio"))
                    .build();
        }
    }

    @GET
    @Path("/tipo/{tipo}")
    public Response listarPorTipo(@PathParam("tipo") String tipo) {
        try {
            List<ServicioExtra> servicios = servicioExtraService.buscarPorTipo(tipo);
            return Response.ok(servicios).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener servicios por tipo"))
                    .build();
        }
    }

    private Map<String, Object> crearErrorResponse(String mensaje) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return errorResponse;
    }
}