/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.project.managedBeans;

import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.services.Services;
import edu.eci.pdsw.services.ServicesException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jhordy
 */
@ManagedBean(name="ConsultarFicha")
@SessionScoped
public class ServiciosConsultarFichaBean implements Serializable{
    
    @ManagedProperty(value = "#{loginBean}")    
    private ShiroLoginBean loginBean;  
    private Services services=Services.getInstance("applicationconfig.properties");
    private String placaAconsultar=null;
    private Equipo equiposeleccionado=null;
    private Modelo modeloAsociado=null;
    private Boolean equipoexiste=false;
    
    
    public void limpiarPaginaConsultarFicha(){
        services = Services.getInstance("applicationconfig.properties");
        placaAconsultar=null;
        equiposeleccionado=null;
        modeloAsociado=null;
        setEquipoexiste((Boolean) false);
    }
        
    
    /**
     * me dice si esta asegurado o no
     */
    public String demeloTodo(){
        if (modeloAsociado!=null){
            if(modeloAsociado.getSeguro()){return "SI";}
            else{return "NO";}
        }
        else{return "";}
    }
    
    /**
     * consulta la informacion de la ficha que lleva el equipo correspondiente al serial ingresado por el ususario
     */
    public void accionConsultarFicha(){
        try {
            equiposeleccionado = services.loadEquipoByPlaca(Integer.parseInt(placaAconsultar));
            modeloAsociado = services.loadModeloByName(services.loadNameModeloByPlaca(Integer.parseInt(placaAconsultar)));
            setEquipoexiste((Boolean) true);
        } catch (ServicesException e) {
            setEquipoexiste((Boolean) false);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",e.getLocalizedMessage()));
        }
        catch(NumberFormatException e){
            setEquipoexiste((Boolean) false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "La placa "+placaAconsultar+" no esta permitida, debe ser un numero positivo"));
        }
        
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
     * @return the placaAconsultar
     */
    public String getPlacaAconsultar() {
        return placaAconsultar;
    }

    /**
     * @param placaAconsultar the placaAconsultar to set
     */
    public void setPlacaAconsultar(String placaAconsultar) {
        this.placaAconsultar = placaAconsultar;
    }

    /**
     * @return the equiposeleccionado
     */
    public Equipo getEquiposeleccionado() {
        return equiposeleccionado;
    }

    /**
     * @param equiposeleccionado the equiposeleccionado to set
     */
    public void setEquiposeleccionado(Equipo equiposeleccionado) {
        this.equiposeleccionado = equiposeleccionado;
    }

    /**
     * @return the modeloAsociado
     */
    public Modelo getModeloAsociado() {
        return modeloAsociado;
    }

    /**
     * @param modeloAsociado the modeloAsociado to set
     */
    public void setModeloAsociado(Modelo modeloAsociado) {
        this.modeloAsociado = modeloAsociado;
    }

    /**
     * @return the equipoexiste
     */
    public Boolean getEquipoexiste() {
        return equipoexiste;
    }

    /**
     * @param equipoexiste the equipoexiste to set
     */
    public void setEquipoexiste(Boolean equipoexiste) {
        this.equipoexiste = equipoexiste;
    }

}
