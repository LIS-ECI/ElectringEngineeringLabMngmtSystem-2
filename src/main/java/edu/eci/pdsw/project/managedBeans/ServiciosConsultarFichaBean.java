/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.project.managedBeans;

import edu.eci.pdsw.services.Services;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jhordy
 */
@ManagedBean(name="ConsultarFicha")
@SessionScoped
public class ServiciosConsultarFichaBean {
    @ManagedProperty(value = "#{loginBean}")    
    private ShiroLoginBean loginBean;  
    private Services services;
    private String serialAconsultar;
    
    /**
     * consulta la informacion de la ficha que lleva el equipo correspondiente al serial ingresado por el ususario
     */
    public void accionConsultarFicha(){
    
    }
    /**
     * @return the loginBean
     */
    public ShiroLoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(ShiroLoginBean loginBean) {
        this.loginBean = loginBean;
    }
    
    /**
     * @return the services
     */
    public Services getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(Services services) {
        this.services = services;
    }

    /**
     * @return the serialAconsultar
     */
    public String getSerialAconsultar() {
        return serialAconsultar;
    }

    /**
     * @param serialAconsultar the serialAconsultar to set
     */
    public void setSerialAconsultar(String serialAconsultar) {
        this.serialAconsultar = serialAconsultar;
    }
}
