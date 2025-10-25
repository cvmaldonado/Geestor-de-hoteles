/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.service;

import com.hotel.dao.HuespedReservaDAO;
import com.hotel.dao.HuespedDAO;
import com.hotel.dao.ReservaDAO;
import com.hotel.model.HuespedReserva;
import com.hotel.model.Huesped;
import com.hotel.model.Reserva;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

/**
 *
 * @author martinmaldonado
 */
@Stateless
public class HuespedReservaService {

    @Inject
    private HuespedReservaDAO huespedReservaDAO;

    @Inject
    private HuespedDAO huespedDAO;

    @Inject
    private ReservaDAO reservaDAO;

    public HuespedReserva agregarHuespedReserva(HuespedReserva huespedReserva) {
        validarHuespedReserva(huespedReserva);
        
        // Verificar que el huésped existe
        Huesped huesped = huespedDAO.buscarPorId(huespedReserva.getHuesped().getIdUsuario());
        if (huesped == null) {
            throw new IllegalArgumentException("Huésped no encontrado con ID: " + huespedReserva.getHuesped().getIdUsuario());
        }
        huespedReserva.setHuesped(huesped);
        
        // Verificar que la reserva existe
        Reserva reserva = reservaDAO.buscarPorId(huespedReserva.getReserva().getIdReserva());
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + huespedReserva.getReserva().getIdReserva());
        }
        huespedReserva.setReserva(reserva);
        
        // Verificar que no existe ya esta relación
        HuespedReserva existente = huespedReservaDAO.buscarPorHuespedYReserva(
            huesped.getIdUsuario(), reserva.getIdReserva());
        if (existente != null) {
            throw new IllegalArgumentException("El huésped ya está asociado a esta reserva");
        }
        
        // Si es titular, verificar que no haya otro titular
        if (huespedReserva.getEsTitular() != null && huespedReserva.getEsTitular()) {
            HuespedReserva titularExistente = huespedReservaDAO.buscarTitularPorReservaId(reserva.getIdReserva());
            if (titularExistente != null) {
                throw new IllegalArgumentException("Ya existe un huésped titular para esta reserva");
            }
        }
        
        // Si no se especifica, establecer como no titular
        if (huespedReserva.getEsTitular() == null) {
            huespedReserva.setEsTitular(false);
        }
        
        huespedReservaDAO.guardar(huespedReserva);
        return huespedReserva;
    }

    public void eliminarHuespedReserva(Long idHuesped, Long idReserva) {
        HuespedReserva huespedReserva = huespedReservaDAO.buscarPorHuespedYReserva(idHuesped, idReserva);
        if (huespedReserva == null) {
            throw new IllegalArgumentException("Relación huésped-reserva no encontrada");
        }
        
        // No permitir eliminar al titular
        if (huespedReserva.getEsTitular() != null && huespedReserva.getEsTitular()) {
            throw new IllegalArgumentException("No se puede eliminar al huésped titular de la reserva");
        }
        
        huespedReservaDAO.eliminarPorHuespedYReserva(idHuesped, idReserva);
    }

    public HuespedReserva actualizarHuespedReserva(Long idHuesped, Long idReserva, Boolean esTitular) {
        HuespedReserva huespedReserva = huespedReservaDAO.buscarPorHuespedYReserva(idHuesped, idReserva);
        if (huespedReserva == null) {
            throw new IllegalArgumentException("Relación huésped-reserva no encontrada");
        }
        
        // Si se quiere establecer como titular, verificar que no haya otro titular
        if (esTitular != null && esTitular) {
            HuespedReserva titularExistente = huespedReservaDAO.buscarTitularPorReservaId(idReserva);
            if (titularExistente != null && !titularExistente.getHuesped().getIdUsuario().equals(idHuesped)) {
                throw new IllegalArgumentException("Ya existe un huésped titular para esta reserva");
            }
        }
        
        huespedReserva.setEsTitular(esTitular);
        huespedReservaDAO.actualizar(huespedReserva);
        return huespedReserva;
    }

    public HuespedReserva buscarPorId(Long idHuesped, Long idReserva) {
        return huespedReservaDAO.buscarPorHuespedYReserva(idHuesped, idReserva);
    }

    public List<HuespedReserva> listarPorReserva(Long idReserva) {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }
        return huespedReservaDAO.listarPorReservaId(idReserva);
    }

    public List<HuespedReserva> listarPorHuesped(Long idHuesped) {
        Huesped huesped = huespedDAO.buscarPorId(idHuesped);
        if (huesped == null) {
            throw new IllegalArgumentException("Huésped no encontrado con ID: " + idHuesped);
        }
        return huespedReservaDAO.listarPorHuespedId(idHuesped);
    }

    public HuespedReserva obtenerTitularPorReserva(Long idReserva) {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }
        return huespedReservaDAO.buscarTitularPorReservaId(idReserva);
    }

    public int contarHuespedesPorReserva(Long idReserva) {
        Reserva reserva = reservaDAO.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva);
        }
        return huespedReservaDAO.contarHuespedesPorReserva(idReserva);
    }

    public List<HuespedReserva> listarTodos() {
        return huespedReservaDAO.listarTodos();
    }

    private void validarHuespedReserva(HuespedReserva huespedReserva) {
        if (huespedReserva.getHuesped() == null || huespedReserva.getHuesped().getIdUsuario() == null) {
            throw new IllegalArgumentException("El huésped es obligatorio");
        }

        if (huespedReserva.getReserva() == null || huespedReserva.getReserva().getIdReserva() == null) {
            throw new IllegalArgumentException("La reserva es obligatoria");
        }
    }
}