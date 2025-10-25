/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.controller;

import com.hotel.model.Hotel;
import com.hotel.service.HotelService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Path("/hoteles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HotelController {

    @Inject
    private HotelService hotelService;

    // GET: Obtener todos los hoteles
    @GET
    public Response obtenerTodosLosHoteles() {
        try {
            List<Hotel> hoteles = hotelService.listarTodosLosHoteles();
            return Response.ok(hoteles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los hoteles: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener hoteles activos
    @GET
    @Path("/activos")
    public Response obtenerHotelesActivos() {
        try {
            List<Hotel> hoteles = hotelService.listarHotelesActivos();
            return Response.ok(hoteles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los hoteles activos: " + e.getMessage())
                    .build();
        }
    }

    // GET: Obtener hotel por ID
    @GET
    @Path("/{id}")
    public Response obtenerHotelPorId(@PathParam("id") Long id) {
        try {
            Hotel hotel = hotelService.obtenerHotelPorId(id);
            if (hotel == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Hotel no encontrado")
                        .build();
            }
            return Response.ok(hotel).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el hotel: " + e.getMessage())
                    .build();
        }
    }

    // GET: Buscar hoteles por nombre
    @GET
    @Path("/buscar")
    public Response buscarHotelesPorNombre(@QueryParam("nombre") String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El parámetro 'nombre' es requerido")
                        .build();
            }
            
            List<Hotel> hoteles = hotelService.buscarPorNombre(nombre);
            return Response.ok(hoteles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar hoteles: " + e.getMessage())
                    .build();
        }
    }

    // POST: Crear nuevo hotel
    @POST
    public Response crearHotel(Hotel hotel) {
        try {
            hotelService.crearHotel(hotel);
            return Response.status(Response.Status.CREATED)
                    .entity("Hotel creado exitosamente")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el hotel: " + e.getMessage())
                    .build();
        }
    }

    // PUT: Actualizar hotel existente
    @PUT
    @Path("/{id}")
    public Response actualizarHotel(@PathParam("id") Long id, Hotel hotelActualizado) {
        try {
            // Verificar que el hotel existe
            Hotel hotelExistente = hotelService.obtenerHotelPorId(id);
            if (hotelExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Hotel no encontrado")
                        .build();
            }

            // Actualizar los campos
            hotelExistente.setNombre(hotelActualizado.getNombre());
            hotelExistente.setDireccion(hotelActualizado.getDireccion());
            hotelExistente.setTelefono(hotelActualizado.getTelefono());
            hotelExistente.setEmail(hotelActualizado.getEmail());
            hotelExistente.setEstrellas(hotelActualizado.getEstrellas());
            hotelExistente.setActivo(hotelActualizado.getActivo());

            hotelService.actualizarHotel(hotelExistente);
            return Response.ok("Hotel actualizado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el hotel: " + e.getMessage())
                    .build();
        }
    }

    // DELETE: Desactivar hotel (eliminación lógica)
    @DELETE
    @Path("/{id}")
    public Response desactivarHotel(@PathParam("id") Long id) {
        try {
            Hotel hotel = hotelService.obtenerHotelPorId(id);
            if (hotel == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Hotel no encontrado")
                        .build();
            }

            hotelService.desactivarHotel(id);
            return Response.ok("Hotel desactivado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al desactivar el hotel: " + e.getMessage())
                    .build();
        }
    }

    // PATCH: Activar hotel
    @PATCH
    @Path("/{id}/activar")
    public Response activarHotel(@PathParam("id") Long id) {
        try {
            Hotel hotel = hotelService.obtenerHotelPorId(id);
            if (hotel == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Hotel no encontrado")
                        .build();
            }

            hotelService.activarHotel(id);
            return Response.ok("Hotel activado exitosamente").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al activar el hotel: " + e.getMessage())
                    .build();
        }
    }
}