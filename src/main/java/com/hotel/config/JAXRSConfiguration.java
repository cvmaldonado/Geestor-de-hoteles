/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 *
 * @author martinmaldonado
 */

@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {
    // Esta clase activa JAX-RS y establece la ruta base /api
}