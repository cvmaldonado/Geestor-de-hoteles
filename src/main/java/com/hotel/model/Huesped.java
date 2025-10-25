/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author martinmaldonado
 */

@Entity
@Table(name = "huesped")
@PrimaryKeyJoinColumn(name = "id_usuario")
@DiscriminatorValue("huesped")

public class Huesped extends Usuario {
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "documento_identidad", unique = true, nullable = false, length = 50)
    private String documentoIdentidad;
    
    @Column(name = "tipo_documento", length = 20)
    private String tipoDocumento;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "nacionalidad", length = 50)
    private String nacionalidad;
    
    // Constructores
    public Huesped() {
        super();
        this.tipoDocumento = "DNI";
    }
    
    public Huesped(String username, String passwordHash, String email, 
                  String nombre, String apellido, String documentoIdentidad) {
        super(username, passwordHash, email, "huesped");
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoIdentidad = documentoIdentidad;
        this.tipoDocumento = "DNI";
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
    
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
}
