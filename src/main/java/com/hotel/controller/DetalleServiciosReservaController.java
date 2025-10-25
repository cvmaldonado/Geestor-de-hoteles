/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.DetalleServiciosReserva;
import com.hotel.service.DetalleServiciosReservaService;
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
@Path("/detalle-servicios-reserva")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DetalleServiciosReservaController {

    @Inject
    private DetalleServiciosReservaService detalleServiciosReservaService;

    @POST
    public Response crearDetalle(DetalleServiciosReserva detalle) {
        try {
            DetalleServiciosReserva nuevoDetalle = detalleServiciosReservaService.crearDetalle(detalle);
            return Response.status(Response.Status.CREATED).entity(nuevoDetalle).build();
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
    public List<DetalleServiciosReserva> listarTodos() {
        return detalleServiciosReservaService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response obtenerDetalle(@PathParam("id") Long id) {
        try {
            DetalleServiciosReserva detalle = detalleServiciosReservaService.buscarPorId(id);
            if (detalle != null) {
                return Response.ok(detalle).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(crearErrorResponse("Detalle de servicio no encontrado"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener el detalle"))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarDetalle(@PathParam("id") Long id, DetalleServiciosReserva detalle) {
        try {
            DetalleServiciosReserva detalleActualizado = detalleServiciosReservaService.actualizarDetalle(id, detalle);
            return Response.ok(detalleActualizado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al actualizar el detalle"))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarDetalle(@PathParam("id") Long id) {
        try {
            detalleServiciosReservaService.eliminarDetalle(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Detalle de servicio eliminado correctamente");
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al eliminar el detalle"))
                    .build();
        }
    }

    @GET
    @Path("/reserva/{idReserva}")
    public Response listarPorReserva(@PathParam("idReserva") Long idReserva) {
        try {
            List<DetalleServiciosReserva> detalles = detalleServiciosReservaService.buscarPorReserva(idReserva);
            return Response.ok(detalles).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener detalles por reserva"))
                    .build();
        }
    }

    @GET
    @Path("/servicio/{idServicio}")
    public Response listarPorServicio(@PathParam("idServicio") Long idServicio) {
        try {
            List<DetalleServiciosReserva> detalles = detalleServiciosReservaService.buscarPorServicio(idServicio);
            return Response.ok(detalles).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener detalles por servicio"))
                    .build();
        }
    }

    @GET
    @Path("/reserva/{idReserva}/total")
    public Response calcularTotalPorReserva(@PathParam("idReserva") Long idReserva) {
        try {
            java.math.BigDecimal total = detalleServiciosReservaService.calcularTotalPorReserva(idReserva);
            Map<String, Object> response = new HashMap<>();
            response.put("idReserva", idReserva);
            response.put("totalServicios", total);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al calcular el total"))
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