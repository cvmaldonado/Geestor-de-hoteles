/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author martinmaldonado
 */
@Entity
@Table(name = "servicioextra")
public class ServicioExtra implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "precio", nullable = false, length = 20)
    private BigDecimal precio;
    
    @Column(name = "tipo_servicio", nullable = false, length = 20)
    private String tipoServicio;
    
    // Campos opcionales
    @Column(name = "categoria_gasto", length = 50)
    private String categoriaGasto;

    @Column(name = "proveedor", length = 100)
    private String proveedor;

    @Column(name = "categoria_cargo", length = 50)
    private String categoriaCargo;

    @Column(name = "aplica_habitacion")
    private Boolean aplicaHabitacion;
    
    // Constructores

    public ServicioExtra() {}
        
    public ServicioExtra(String nombre, String descripcion, BigDecimal precio,
                        String tipoServicio) {        
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoServicio = tipoServicio;
    }

    public Long getIdServicio() {return idServicio;}
    public void setIdServicio(Long idServicio) {this.idServicio = idServicio;}
    

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public BigDecimal getPrecio() {return precio;}
    public void setPrecio(BigDecimal precio) {this.precio = precio;}

    public String getTipoServicio() {return tipoServicio;}
    public void setTipoServicio(String tipoServicio) {this.tipoServicio = tipoServicio;}

    public String getCategoriaGasto() {return categoriaGasto;}
    public void setCategoriaGasto(String categoriaGasto) {this.categoriaGasto = categoriaGasto;}

    public String getProveedor() {return proveedor;}
    public void setProveedor(String proveedor) {this.proveedor = proveedor;}

    public String getCategoriaCargo() {return categoriaCargo;}
    public void setCategoriaCargo(String categoriaCargo) {this.categoriaCargo = categoriaCargo;}

    public Boolean getAplicaHabitacion() {return aplicaHabitacion;}
    public void setAplicaHabitacion(Boolean aplicaHabitacion) {this.aplicaHabitacion = aplicaHabitacion;}
}
