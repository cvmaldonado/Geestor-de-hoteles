/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.Reserva;
import com.hotel.service.ReservaService;
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


@Path("/reservas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservaController {

    @Inject
    private ReservaService reservaService;

    @POST
        public Response crearReserva(Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.crearReserva(reserva);
            return Response.status(Response.Status.CREATED).entity(nuevaReserva).build();
        } catch (IllegalArgumentException e) {
            // Captura excepciones de validación directas
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (jakarta.ejb.EJBException e) {
            // Captura EJBException y extrae la causa real
            Throwable causa = e.getCause();
            if (causa instanceof IllegalArgumentException) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(crearErrorResponse(causa.getMessage()))
                        .build();
            } else {
                // Para otros tipos de EJBException
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(crearErrorResponse("Error interno del servidor: " + e.getMessage()))
                        .build();
            }
        } catch (Exception e) {
            // Captura cualquier otra excepción
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error interno del servidor"))
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

    @GET
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    @GET
    @Path("/{id}")
    public Response obtenerReserva(@PathParam("id") Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        if (reserva != null) {
            return Response.ok(reserva).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarReserva(@PathParam("id") Long id, Reserva reserva) {
        try {
            Reserva reservaActualizada = reservaService.actualizarReserva(id, reserva);
            return Response.ok(reservaActualizada).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/confirmar")
    public Response confirmarReserva(@PathParam("id") Long id) {
        try {
            reservaService.confirmarReserva(id);
            return Response.ok().entity("Reserva confirmada").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/cancelar")
    public Response cancelarReserva(@PathParam("id") Long id) {
        try {
            reservaService.cancelarReserva(id);
            return Response.ok().entity("Reserva cancelada").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/huesped/{idHuesped}")
    public List<Reserva> listarPorHuesped(@PathParam("idHuesped") Long idHuesped) {
        return reservaService.listarPorHuesped(idHuesped);
    }

    @GET
    @Path("/estado/{estado}")
    public List<Reserva> listarPorEstado(@PathParam("estado") String estado) {
        return reservaService.listarPorEstado(estado);
    }
}