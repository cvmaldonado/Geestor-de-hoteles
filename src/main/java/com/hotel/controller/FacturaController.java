/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.Factura;
import com.hotel.service.FacturaService;
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
@Path("/facturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacturaController {

    @Inject
    private FacturaService facturaService;

    @POST
    public Response crearFactura(Factura factura) {
        try {
            Factura nuevaFactura = facturaService.crearFactura(factura);
            return Response.status(Response.Status.CREATED).entity(nuevaFactura).build();
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

    @POST
    @Path("/generar/{idReserva}")
    public Response generarFacturaAutomatica(@PathParam("idReserva") Long idReserva) {
        try {
            Factura factura = facturaService.generarFacturaAutomatica(idReserva);
            return Response.status(Response.Status.CREATED).entity(factura).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al generar factura automática"))
                    .build();
        }
    }

    @GET
    public List<Factura> listarTodas() {
        return facturaService.listarTodas();
    }

    @GET
    @Path("/{id}")
    public Response obtenerFactura(@PathParam("id") Long id) {
        try {
            Factura factura = facturaService.buscarPorId(id);
            if (factura != null) {
                return Response.ok(factura).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(crearErrorResponse("Factura no encontrada"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener la factura"))
                    .build();
        }
    }

    @GET
    @Path("/numero/{numeroFactura}")
    public Response obtenerFacturaPorNumero(@PathParam("numeroFactura") String numeroFactura) {
        try {
            Factura factura = facturaService.buscarPorNumero(numeroFactura);
            if (factura != null) {
                return Response.ok(factura).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(crearErrorResponse("Factura no encontrada"))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener la factura"))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizarFactura(@PathParam("id") Long id, Factura factura) {
        try {
            Factura facturaActualizada = facturaService.actualizarFactura(id, factura);
            return Response.ok(facturaActualizada).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al actualizar la factura"))
                    .build();
        }
    }

    @PUT
    @Path("/{id}/pagar")
    public Response marcarComoPagada(@PathParam("id") Long id, 
                                   @QueryParam("metodoPago") String metodoPago) {
        try {
            facturaService.marcarComoPagada(id, metodoPago);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Factura marcada como pagada");
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al marcar factura como pagada"))
                    .build();
        }
    }

    @PUT
    @Path("/{id}/anular")
    public Response anularFactura(@PathParam("id") Long id) {
        try {
            facturaService.anularFactura(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Factura anulada");
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al anular la factura"))
                    .build();
        }
    }

    @GET
    @Path("/reserva/{idReserva}")
    public Response listarPorReserva(@PathParam("idReserva") Long idReserva) {
        try {
            List<Factura> facturas = facturaService.listarPorReserva(idReserva);
            return Response.ok(facturas).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener facturas por reserva"))
                    .build();
        }
    }

    @GET
    @Path("/estado/{estado}")
    public Response listarPorEstado(@PathParam("estado") String estado) {
        try {
            List<Factura> facturas = facturaService.listarPorEstado(estado);
            return Response.ok(facturas).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(crearErrorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(crearErrorResponse("Error al obtener facturas por estado"))
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