/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.web.bean;

import com.hotel.model.Usuario;
import com.hotel.service.UsuarioService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martinmaldonado
 */

@Named
@SessionScoped
public class LoginBean implements Serializable {
    private static final Logger logger = Logger.getLogger(LoginBean.class.getName());
    
    private String username;
    private String password;
    private Usuario usuarioLogueado;
    
    @Inject
    private UsuarioService usuarioService;
    
    public String login() {
        try {
            logger.log(Level.INFO, "Intentando login para usuario: {0}", username);

            // Validaciones
            if (username == null || username.trim().isEmpty()) {
                agregarMensajeError("El usuario es obligatorio");
                return null;
            }

            if (password == null || password.trim().isEmpty()) {
                agregarMensajeError("La contraseña es obligatoria");
                return null;
            }

            Usuario usuario = usuarioService.autenticarUsuario(username, password);

            if (usuario != null && usuario.getActivo()) {
                this.usuarioLogueado = usuario;
                logger.log(Level.INFO, "Login exitoso para: {0}", username);
                logger.log(Level.INFO, "Rol del usuario: {0}", usuario.getRol());

                String redireccion = redirigirSegunRol(usuario.getRol());
                logger.log(Level.INFO, "URL de redirección: {0}", redireccion);

                return redireccion;

            } else {
                agregarMensajeError("Usuario o contraseña incorrectos");
                return null;
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en login: " + e.getMessage(), e);
            agregarMensajeError("Error en el sistema. Contacte al administrador.");
            return null;
        }
    }
    
    private String redirigirSegunRol(String rol) {
        switch(rol.toLowerCase()) {
            case "administrador":
            case "gerente":
                return "/admin/dashboard?faces-redirect=true";
            case "recepcionista":
                return "/recepcion/dashboard?faces-redirect=true";
            case "huesped":
                return "/huesped/dashboard?faces-redirect=true";
            default:
                agregarMensajeError("Rol no reconocido: " + rol);
                return null;
        }
    }
    
    public String logout() {
        try {
            // Invalidar sesión
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            logger.log(Level.INFO, "Logout exitoso para: {0}", username);
            return "/auth/login?faces-redirect=true";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en logout: " + e.getMessage(), e);
            return "/auth/login?faces-redirect=true";
        }
    }
    
    private void agregarMensajeError(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", mensaje));
    }
    
    public void verificarSesion(jakarta.faces.event.ComponentSystemEvent event) {
        try {
            if (usuarioLogueado != null) {
                // Ya hay sesión activa → redirige según el rol
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() 
                        + redirigirSegunRol(usuarioLogueado.getRol()).replace("?faces-redirect=true", ""));
            } else {
                // Si no hay usuario logueado → redirige al login
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() 
                        + "/auth/login.xhtml");
            }
        } catch (Exception e) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, "Error en verificarSesion: " + e.getMessage(), e);
        }
    }

    
    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
    public boolean isLogueado() { return usuarioLogueado != null; }
}