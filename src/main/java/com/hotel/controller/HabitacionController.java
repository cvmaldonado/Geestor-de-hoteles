/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.Habitacion;
import com.hotel.service.HabitacionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Path("/habitaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HabitacionController {

    @Inject
    private HabitacionService habitacionService;

    // GET: Obtener todas las habitaciones
    @GET
    public Response obtenerTodasLasHabitaciones() {
        try {
            List<Habitacion> habitaciones = habitacionService.listarTodasLasHabitaciones();
            return Response.ok(habitaciones).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las habitaciones: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener habitación por ID
    @GET
    @Path("/{id}")
    public Response obtenerHabitacionPorId(@PathParam("id") Long id) {
        try {
            Habitacion habitacion = habitacionService.obtenerHabitacionPorId(id);
            if (habitacion == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Habitación no encontrada")
                        .build();
            }
            return Response.ok(habitacion).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la habitación: " + e.getMessage())
                    .build();
        }
    }

    // GET: Listar habitaciones por hotel
    @GET
    @Path("/hotel/{idHotel}")
    public Response obtenerHabitacionesPorHotel(@PathParam("idHotel") Long idHotel) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarHabitacionesPorHotel(idHotel);
            return Response.ok(habitaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las habitaciones del hotel: " + e.getMessage())
                    .build();
        }
    }

    // GET: Listar habitaciones disponibles por hotel
    @GET
    @Path("/hotel/{idHotel}/disponibles")
    public Response obtenerHabitacionesDisponiblesPorHotel(@PathParam("idHotel") Long idHotel) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarHabitacionesDisponiblesPorHotel(idHotel);
            return Response.ok(habitaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las habitaciones disponibles: " + e.getMessage())
                    .build();
        }
    }

    // GET: Listar habitaciones por estado
    @GET
    @Path("/estado/{estado}")
    public Response obtenerHabitacionesPorEstado(@PathParam("estado") String estado) {
        try {
            List<Habitacion> habitaciones = habitacionService.listarHabitacionesPorEstado(estado);
            return Response.ok(habitaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las habitaciones por estado: " + e.getMessage())
                    .build();
        }
    }

    // GET: Buscar habitaciones por tipo
    @GET
    @Path("/buscar/tipo")
    public Response buscarHabitacionesPorTipo(@QueryParam("tipo") String tipo) {
        try {
            if (tipo == null || tipo.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El parámetro 'tipo' es requerido")
                        .build();
            }
            
            List<Habitacion> habitaciones = habitacionService.buscarHabitacionesPorTipo(tipo);
            return Response.ok(habitaciones).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar habitaciones por tipo: " + e.getMessage())
                    .build();
        }
    }

    // GET: Buscar habitaciones por rango de precio
    @GET
    @Path("/buscar/precio")
    public Response buscarHabitacionesPorRangoPrecio(
            @QueryParam("min") BigDecimal min,
            @QueryParam("max") BigDecimal max) {
        try {
            if (min == null || max == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Los parámetros 'min' y 'max' son requeridos")
                        .build();
            }
            
            List<Habitacion> habitaciones = habitacionService.buscarHabitacionesPorRangoPrecio(min, max);
            return Response.ok(habitaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar habitaciones por precio: " + e.getMessage())
                    .build();
        }
    }

    // POST: Crear nueva habitación
    @POST
    public Response crearHabitacion(Habitacion habitacion) {
        try {
            habitacionService.crearHabitacion(habitacion);
            return Response.status(Response.Status.CREATED)
                    .entity("Habitación creada exitosamente")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear la habitación: " + e.getMessage())
                    .build();
        }
    }

    // PUT: Actualizar habitación existente
    @PUT
    @Path("/{id}")
    public Response actualizarHabitacion(@PathParam("id") Long id, Habitacion habitacionActualizada) {
        try {
            // Verificar que la habitación existe
            Habitacion habitacionExistente = habitacionService.obtenerHabitacionPorId(id);
            if (habitacionExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Habitación no encontrada")
                        .build();
            }

            // Actualizar los campos
            habitacionExistente.setNumero(habitacionActualizada.getNumero());
            habitacionExistente.setPiso(habitacionActualizada.getPiso());
            habitacionExistente.setTipoHabitacion(habitacionActualizada.getTipoHabitacion());
            habitacionExistente.setCapacidad(habitacionActualizada.getCapacidad());
            habitacionExistente.setPrecioBase(habitacionActualizada.getPrecioBase());
            habitacionExistente.setDescripcion(habitacionActualizada.getDescripcion());
            habitacionExistente.setEstado(habitacionActualizada.getEstado());
            habitacionExistente.setTieneEscritorio(habitacionActualizada.getTieneEscritorio());
            habitacionExistente.setTipoCama(habitacionActualizada.getTipoCama());
            habitacionExistente.setNumeroCamas(habitacionActualizada.getNumeroCamas());
            habitacionExistente.setTipoCamas(habitacionActualizada.getTipoCamas());

            habitacionService.actualizarHabitacion(habitacionExistente);
            return Response.ok("Habitación actualizada exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar la habitación: " + e.getMessage())
                    .build();
        }
    }

    // PATCH: Cambiar estado de habitación
    @PATCH
    @Path("/{id}/estado")
    public Response cambiarEstadoHabitacion(@PathParam("id") Long id, 
                                           @QueryParam("estado") String estado) {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El parámetro 'estado' es requerido")
                        .build();
            }

            Habitacion habitacion = habitacionService.obtenerHabitacionPorId(id);
            if (habitacion == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Habitación no encontrada")
                        .build();
            }

            habitacionService.cambiarEstadoHabitacion(id, estado);
            return Response.ok("Estado de habitación actualizado exitosamente").build();
            
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al cambiar el estado de la habitación: " + e.getMessage())
                    .build();
        }
    }

    // DELETE: Eliminar habitación
    @DELETE
    @Path("/{id}")
    public Response eliminarHabitacion(@PathParam("id") Long id) {
        try {
            Habitacion habitacion = habitacionService.obtenerHabitacionPorId(id);
            if (habitacion == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Habitación no encontrada")
                        .build();
            }

            habitacionService.eliminarHabitacion(id);
            return Response.ok("Habitación eliminada exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar la habitación: " + e.getMessage())
                    .build();
        }
    }
}