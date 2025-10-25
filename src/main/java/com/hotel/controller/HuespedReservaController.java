/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.HuespedReserva;
import com.hotel.service.HuespedReservaService;
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
@Path("/huespedes-reservas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HuespedReservaController {

    @Inject
    private HuespedReservaService huespedReservaService;

    @POST
    public Response agregarHuespedReserva(HuespedReserva huespedReserva) {
        try {
            HuespedReserva nuevaRelacion = huespedReservaService.agregarHuespedReserva(huespedReserva);
            return Response.status(Response.Status.CREATED).entity(nuevaRelacion).build();
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
    public List<HuespedReserva> listarTodos() {
        return huespedReservaService.listarTodos();
    }

    @GET
    @Path("/reserva/{idReserva}")
    public Response listarPorReserva(@PathParam("idReserva") Long idReserva) {
        try {
            List<HuespedReserva> relaciones = huespedReservaService.listarPorReserva(idReserva);
            return Response.ok(relaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener relaciones por reserva"))
                    .build();
        }
    }

    @GET
    @Path("/huesped/{idHuesped}")
    public Response listarPorHuesped(@PathParam("idHuesped") Long idHuesped) {
        try {
            List<HuespedReserva> relaciones = huespedReservaService.listarPorHuesped(idHuesped);
            return Response.ok(relaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener relaciones por huésped"))
                    .build();
        }
    }

    @GET
    @Path("/reserva/{idReserva}/titular")
    public Response obtenerTitularPorReserva(@PathParam("idReserva") Long idReserva) {
        try {
            HuespedReserva titular = huespedReservaService.obtenerTitularPorReserva(idReserva);
            if (titular != null) {
                return Response.ok(titular).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(crearErrorResponse("No se encontró huésped titular para esta reserva"))
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener el titular de la reserva"))
                    .build();
        }
    }

    @GET
    @Path("/reserva/{idReserva}/cantidad")
    public Response contarHuespedesPorReserva(@PathParam("idReserva") Long idReserva) {
        try {
            int cantidad = huespedReservaService.contarHuespedesPorReserva(idReserva);
            Map<String, Object> response = new HashMap<>();
            response.put("idReserva", idReserva);
            response.put("cantidadHuespedes", cantidad);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al contar huéspedes por reserva"))
                    .build();
        }
    }

    @PUT
    @Path("/huesped/{idHuesped}/reserva/{idReserva}")
    public Response actualizarHuespedReserva(
            @PathParam("idHuesped") Long idHuesped,
            @PathParam("idReserva") Long idReserva,
            @QueryParam("esTitular") Boolean esTitular) {
        try {
            HuespedReserva relacionActualizada = huespedReservaService.actualizarHuespedReserva(idHuesped, idReserva, esTitular);
            return Response.ok(relacionActualizada).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al actualizar la relación huésped-reserva"))
                    .build();
        }
    }

    @DELETE
    @Path("/huesped/{idHuesped}/reserva/{idReserva}")
    public Response eliminarHuespedReserva(
            @PathParam("idHuesped") Long idHuesped,
            @PathParam("idReserva") Long idReserva) {
        try {
            huespedReservaService.eliminarHuespedReserva(idHuesped, idReserva);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Relación huésped-reserva eliminada correctamente");
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al eliminar la relación huésped-reserva"))
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