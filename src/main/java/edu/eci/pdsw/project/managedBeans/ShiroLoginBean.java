/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.project.managedBeans;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean(name = "loginBean")
@SessionScoped
public class ShiroLoginBean implements Serializable {
private static final Logger log = LoggerFactory.getLogger(ShiroLoginBean.class);
private String username;
private String password;
private Boolean rememberMe;

//botonesDisponibles 
//laboratorista
private boolean botonRegistrarEquipoEnInventario=false;
private boolean botonRegistrarUnaDevolucion=false;
private boolean botonRegistrarUnPrestamo =false;
private boolean botonConsultarFicha = false;

public ShiroLoginBean() {
}
public Subject getSubject() {
return SecurityUtils.getSubject();
}
/**
* Try and authenticate the user
*/
public void doLogin() {
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(getUsername(), getPassword());
    try {
        subject.login(token);
        if (subject.hasRole("laboratorista")) {
            botonRegistrarEquipoEnInventario=true;
            botonRegistrarUnaDevolucion=true;
            botonRegistrarUnPrestamo=true;
            botonConsultarFicha=true;
            FacesContext.getCurrentInstance().getExternalContext().redirect("restricted/usuario.xhtml");
        }
        else if (subject.hasRole("administrador")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("restricted/usuario.xhtml");
        }
        else if (subject.hasRole("profesor")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("restricted/usuario.xhtml");
        }
        else if (subject.hasRole("estudiante")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("restricted/usuario.xhtml");
        }
        else {
        FacesContext.getCurrentInstance().getExternalContext().redirect("principal.xhtml");
        }
    }
    catch (UnknownAccountException ex) {
        facesError("Unknown account");
        log.error(ex.getMessage(), ex);
    }
    catch (IncorrectCredentialsException ex) {
        facesError("Wrong password");
        log.error(ex.getMessage(), ex);
    }
    catch (LockedAccountException ex) {
        facesError("Locked account");
        log.error(ex.getMessage(), ex);
    }
    catch (AuthenticationException ex) {
        facesError("Unknown error: " + ex.getMessage());
        log.error(ex.getMessage(), ex);
    }
    catch (IOException ex){
        facesError("Unknown error: " + ex.getMessage());
        log.error(ex.getMessage(), ex);
    }
    finally {
    token.clear();
    }
}
/**
* Adds a new SEVERITY_ERROR FacesMessage for the ui
* @param message Error Message
*/
private void facesError(String message) {
FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
}
public String getUsername() {
return username;
}
public void setUsername(String login) {
this.username = login;
}
public String getPassword() {
return password;
}
public void setPassword(String senha) {
this.password = senha;
}
public Boolean getRememberMe() {
return rememberMe;
}
public void setRememberMe(Boolean lembrar) {
this.rememberMe = lembrar;
}

    /**
     * @return the botonRegistrarEquipoEnInventario
     */
    public boolean isBotonRegistrarEquipoEnInventario() {
        return botonRegistrarEquipoEnInventario;
    }

    /**
     * @param botonRegistrarEquipoEnInventario the botonRegistrarEquipoEnInventario to set
     */
    public void setBotonRegistrarEquipoEnInventario(boolean botonRegistrarEquipoEnInventario) {
        this.botonRegistrarEquipoEnInventario = botonRegistrarEquipoEnInventario;
    }

    /**
     * @return the botonRegistrarUnaDevolucion
     */
    public boolean isBotonRegistrarUnaDevolucion() {
        return botonRegistrarUnaDevolucion;
    }

    /**
     * @param botonRegistrarUnaDevolucion the botonRegistrarUnaDevolucion to set
     */
    public void setBotonRegistrarUnaDevolucion(boolean botonRegistrarUnaDevolucion) {
        this.botonRegistrarUnaDevolucion = botonRegistrarUnaDevolucion;
    }
    /**
     * @return the botonRegistrarUnPrestamo
     */
    public boolean isBotonRegistrarUnPrestamo() {
        return botonRegistrarUnPrestamo;
    }

    /**
     * @param botonRegistrarUnPrestamo the botonRegistrarUnPrestamo to set
     */
    public void setBotonRegistrarUnPrestamo(boolean botonRegistrarUnPrestamo) {
        this.botonRegistrarUnPrestamo = botonRegistrarUnPrestamo;
    }

    /**
     * @return the botonConsultarFicha
     */
    public boolean isBotonConsultarFicha() {
        return botonConsultarFicha;
    }

    /**
     * @param botonConsultarFicha the botonConsultarFicha to set
     */
    public void setBotonConsultarFicha(boolean botonConsultarFicha) {
        this.botonConsultarFicha = botonConsultarFicha;
    }
}
