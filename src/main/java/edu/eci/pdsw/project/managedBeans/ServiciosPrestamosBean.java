/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.project.managedBeans;

import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.entities.EquipoBasicoPrestamo;
import edu.eci.pdsw.entities.EquipoPrestamo;
import edu.eci.pdsw.entities.PrestamoBasicoEquipo;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoUsuario;
import edu.eci.pdsw.entities.RolUsuario;
import edu.eci.pdsw.entities.Usuario;
import edu.eci.pdsw.services.Services;
import edu.eci.pdsw.services.ServicesException;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jhordy
 */
@ManagedBean(name="Prestamo")
@SessionScoped
public class ServiciosPrestamosBean implements Serializable{

    public ServiciosPrestamosBean() {
    }
    Services services = Services.getInstance("applicationconfig.properties");
    @ManagedProperty(value = "#{loginBean}")    
    private ShiroLoginBean loginBean;
    private String codigoUsuarioPrestamo;
    private boolean estudianteExiste=false;
    private Usuario usuarioSeleccionado=null;
    private String rolesUsuarioSeleccionado="";
    private String nombreEquipoBasicoPrestar="";
    private String codigoEquipo="";
    private String tipoEquipo="";
    private boolean codificado=false;
    private boolean noCodificado=false;
    private String tipoPrestamoSeleccionado;
    private List<String> nombresTipoPrestamo;
    private List<EquipoPrestamo> listaEquipos;
    private List<EquipoPrestamo> listaEquiposOne;
    private List<EquipoBasicoPrestamo> listaEquiposBasicos;
    private List<EquipoBasicoPrestamo> listaEquiposBasicosOne;
    private EquipoPrestamo equipoSeleccionado;
    private EquipoBasicoPrestamo prestamoEquipoBasicoSeleccionado;
    private int cantidadEquipoBasicoSeleccionada=1;
    private String  tipoPrestamoSeleccionadoDos; 
    
    
    public void limpiarPaginaRegistrarUnPrestamo(){
        cantidadEquipoBasicoSeleccionada=1;
        equipoSeleccionado=null;
        prestamoEquipoBasicoSeleccionado=null;
        listaEquipos= new ArrayList<EquipoPrestamo>();
        listaEquiposBasicos= new ArrayList<EquipoBasicoPrestamo>();
        listaEquiposOne= new ArrayList<EquipoPrestamo>();
        listaEquiposBasicosOne= new ArrayList<EquipoBasicoPrestamo>();
        nombresTipoPrestamo=new ArrayList<String>();
        setEstudianteExiste(false);
        usuarioSeleccionado=null;
        codigoUsuarioPrestamo=null;
        rolesUsuarioSeleccionado="";
        setCodigoEquipo("");
        setNombreEquipoBasicoPrestar("");
        setTipoEquipo("");
        setCodificado(false);
        setNoCodificado(false);
        tipoPrestamoSeleccionado=null;
    }
    
    public void accionCompletarPrestamo(){
        listaEquiposOne=getListaEquiposOne();
        listaEquiposBasicosOne=getListaEquiposBasicosOne();
        if(listaEquiposOne.size()>0 || listaEquiposBasicosOne.size()>0){
            for(int i=0;i<listaEquiposOne.size();i++){
                PrestamoEquipo pe=new PrestamoEquipo(Integer.parseInt(codigoUsuarioPrestamo),new java.sql.Date(Calendar.getInstance().getTime().getTime()),null,listaEquiposOne.get(i).getTipoPrestamo());
                PrestamoUsuario pu=new PrestamoUsuario(listaEquiposOne.get(i).getEquipoBasico().getSerial(),new java.sql.Date(Calendar.getInstance().getTime().getTime()),null,listaEquiposOne.get(i).getTipoPrestamo());
                try {
                    services.registrarNuevoPrestamo(pe,pu);
                    services.updateEstadoEquipo(Integer.toString(listaEquiposOne.get(i).getEquipoBasico().getSerial()),listaEquiposOne.get(i).getTipoPrestamo());
                } catch (ServicesException ex) {
                    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
                }
                
            }
            for(int j=0;j<listaEquiposBasicosOne.size();j++){
                PrestamoBasicoEquipo pbe=new PrestamoBasicoEquipo(Integer.parseInt(codigoUsuarioPrestamo),new java.sql.Date(Calendar.getInstance().getTime().getTime()),null,listaEquiposBasicosOne.get(j).getTipoPrestamo(), listaEquiposBasicosOne.get(j).getCantidad());
                PrestamoBasicoUsuario pbu=new PrestamoBasicoUsuario(listaEquiposBasicosOne.get(j).getEquipoBasico().getNombre(),new java.sql.Date(Calendar.getInstance().getTime().getTime()),null,listaEquiposBasicosOne.get(j).getTipoPrestamo(), listaEquiposBasicosOne.get(j).getCantidad());
                try {
                    services.registrarNuevoPrestamoBasico(pbe,pbu);
                    services.updateCantidadEquipoBasico(listaEquiposBasicosOne.get(j).getEquipoBasico().getNombre(),listaEquiposBasicosOne.get(j).getCantidad());
                } catch (ServicesException ex) {
                    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
                }
                
            }
            limpiarPaginaRegistrarUnPrestamo();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Succes","Se han registrado todos los préstamos con éxito"));
            
            
        }
        else{
           FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","Hay que registrar los préstamos que se quieran hacer")); 
        }
    }
    
    
    /**
     * registra un prestamo de un equipo
     */
    public void accionRegistrarPrestamoEquipo(){
        try {         
           
           Equipo nuevo=services.loadEquipoBySerial(Integer.parseInt(codigoEquipo));
           if(nuevo.getSubEstado().equals("En almacén")){
            EquipoPrestamo aMeter=new EquipoPrestamo(nuevo, getTipoPrestamoSeleccionadoDos());
            listaEquipos=getListaEquipos();
            listaEquipos.add(aMeter); 
            listaEquiposOne=getListaEquiposOne();
            listaEquiposOne.add(aMeter);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Succes","Se ha agregado el préstamo a la lista de equipos codificados con éxito"));    
           }
           else{
               FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","El equipo se encuentra en la base de datos, pero no esta disponible para préstamo"));
           }
           
        }
        catch (ServicesException ex) { //ServicesException
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
    }
    
    /**
     * registra un prestamo de un equipo basico
     */
    public void accionRegistrarPrestamoEquipoBasico(){
        try {
            
            EquipoBasico nuevo=services.loadEquipoBasicoByName(nombreEquipoBasicoPrestar);
            if(nuevo.getCantidadInventario()>=cantidadEquipoBasicoSeleccionada && cantidadEquipoBasicoSeleccionada>0){
                EquipoBasicoPrestamo aMeter=new EquipoBasicoPrestamo(nuevo,cantidadEquipoBasicoSeleccionada,tipoPrestamoSeleccionado);
                listaEquiposBasicos=getListaEquiposBasicos();
                listaEquiposBasicos.add(aMeter);
                listaEquiposBasicosOne=getListaEquiposBasicosOne();
                listaEquiposBasicosOne.add(aMeter);
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Succes","Se ha agregado el préstamo a la lista de equipos no codificados con éxito"));    
            }
            else if(cantidadEquipoBasicoSeleccionada<=0){
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","La cantidad ingresada para el prestamo debe ser mayor a 0"));    
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","La maxima cantidad que puede ser prestada para este equipo es: "+nuevo.getCantidadInventario()));    
            }
            
            
        }
        catch (ServicesException ex) { //ServicesException
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
        
    }
    
    
    public void accionCambiarTipo(){
        
        if (tipoEquipo.equals("Codificado")){
            codificado=true;
            noCodificado=false;
            
        }
        else if(tipoEquipo.equals("No Codificado")){
            codificado=false;
            noCodificado=true;
        }
        else{
            codificado=false;
            noCodificado=false;
        }
        tipoPrestamoSeleccionado=null;
    }
    /**
     * Actualiza los datos del usuario a quien se le realiza el prestamo
     */
    public void accionBotonUsuarioPrestamo(){
        usuarioSeleccionado=null;
        try {
            
            usuarioSeleccionado = services.loadUsuarioById(Integer.parseInt(codigoUsuarioPrestamo));
            String roles="";
            
            for (RolUsuario r: usuarioSeleccionado.getRoles()) {
                roles+=r.getRol_r()+", ";
            }
            if(usuarioSeleccionado.getRoles().size()>0){
                rolesUsuarioSeleccionado=roles.substring(0,roles.length()-2);
            }
            estudianteExiste=true;
            listaEquiposBasicos=services.loadPrestamosBasicosByUsuario(Integer.parseInt(codigoUsuarioPrestamo));
            listaEquipos=services.loadPrestamosByUsuario(Integer.parseInt(codigoUsuarioPrestamo));
        } 
        catch (ServicesException ex) {
            estudianteExiste=false;
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
    }
    
    /**
     * @param codigo codigo de usuario a quien se le va a realizar un prestamo
     */
    public void setCodigoUsuarioPrestamo(String codigo){
        
        this.codigoUsuarioPrestamo = codigo;
    }
    
    /**
     * @return codigo del usuario a quien se le realiza el prestamo
     */
    public String getCodigoUsuarioPrestamo(){
        return codigoUsuarioPrestamo;
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
     * @return the estudianteExiste
     */
    public boolean isEstudianteExiste() {
        return estudianteExiste;
    }

    /**
     * @param estudianteExiste the estudianteExiste to set
     */
    public void setEstudianteExiste(boolean estudianteExiste) {
        this.estudianteExiste = estudianteExiste;
    }

    /**
     * @return the usuarioSeleccionado
     */
    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    /**
     * @param usuarioSeleccionado the usuarioSeleccionado to set
     */
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    /**
     * @return the rolesUsuarioSeleccionado
     */
    public String getRolesUsuarioSeleccionado() {
        return rolesUsuarioSeleccionado;
    }

    /**
     * @param rolesUsuarioSeleccionado the rolesUsuarioSeleccionado to set
     */
    public void setRolesUsuarioSeleccionado(String rolesUsuarioSeleccionado) {
        this.rolesUsuarioSeleccionado = rolesUsuarioSeleccionado;
    }

    /**
     * @return the nombreEquipoBasicoPrestar
     */
    public String getNombreEquipoBasicoPrestar() {
        return nombreEquipoBasicoPrestar;
    }

    /**
     * @param nombreEquipoBasicoPrestar the nombreEquipoBasicoPrestar to set
     */
    public void setNombreEquipoBasicoPrestar(String nombreEquipoBasicoPrestar) {
        this.nombreEquipoBasicoPrestar = nombreEquipoBasicoPrestar;
    }

    /**
     * @return the codigoEquipo
     */
    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    /**
     * @param codigoEquipo the codigoEquipo to set
     */
    public void setCodigoEquipo(String codigoEquipo) {
        
        this.codigoEquipo = codigoEquipo;
    }

    /**
     * @return the tipoEquipo
     */
    public String getTipoEquipo() {
        return tipoEquipo;
    }

    /**
     * @param tipoEquipo the tipoEquipo to set
     */
    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    /**
     * @return the codificado
     */
    public boolean isCodificado() {
        return codificado;
    }

    /**
     * @param codificado the codificado to set
     */
    public void setCodificado(boolean codificado) {
        this.codificado = codificado;
    }

    /**
     * @return the noCodificado
     */
    public boolean isNoCodificado() {
        return noCodificado;
    }

    /**
     * @param noCodificado the noCodificado to set
     */
    public void setNoCodificado(boolean noCodificado) {
        this.noCodificado = noCodificado;
    }

    /**
     * @return the tipoPrestamoSeleccionado
     */
    public String getTipoPrestamoSeleccionado() {
        
        return tipoPrestamoSeleccionado;
    }

    /**
     * @param tipoPrestamoSeleccionado the tipoPrestamoSeleccionado to set
     */
    public void setTipoPrestamoSeleccionado(String tipoPrestamoSeleccionado) {
        this.tipoPrestamoSeleccionado = tipoPrestamoSeleccionado;
    }

    /**
     * @return the nombresTipoPrestamo
     */
    public List<String> getNombresTipoPrestamo() {
        nombresTipoPrestamo=new ArrayList<String>();
        String[] listaNombres=rolesUsuarioSeleccionado.split(", ");
        nombresTipoPrestamo.add("Préstamo diario");
        nombresTipoPrestamo.add("Préstamo 24 horas");
        nombresTipoPrestamo.add("Préstamo por semestre");
        for(int i=0;i<listaNombres.length;i++){
            if(listaNombres[i].equals("profesor")){
                nombresTipoPrestamo.add("Préstamo indefinido");
            }
        }
        
        return nombresTipoPrestamo;
    }

    /**
     * @param nombresTipoPrestamo the nombresTipoPrestamo to set
     */
    public void setNombresTipoPrestamo(List<String> nombresTipoPrestamo) {
        this.nombresTipoPrestamo = nombresTipoPrestamo;
    }

    /**
     * @return the listaEquipos
     */
    public List<EquipoPrestamo> getListaEquipos() {
        if(listaEquipos==null){
            listaEquipos=new ArrayList<EquipoPrestamo>();
        }
        return listaEquipos;
    }
    
    /**
     * @return the listaEquipos1
     */
    public List<EquipoPrestamo> getListaEquiposOne() {
        if(listaEquiposOne==null){
            listaEquiposOne=new ArrayList<EquipoPrestamo>();
        }
        return listaEquiposOne;
    }

    /**
     * @param listaEquipos the listaEquipos to set
     */
    public void setListaEquipos(List<EquipoPrestamo> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }

    /**
     * @param listaEquipos| the listaEquipos to set
     */
    public void setListaEquiposOne(List<EquipoPrestamo> listaEquiposOne) {
        this.listaEquiposOne = listaEquiposOne;
    }
    
    /**
     * @return the listaEquiposBasicos
     */
    public List<EquipoBasicoPrestamo> getListaEquiposBasicos() {
        if(listaEquiposBasicos==null){
            listaEquiposBasicos=new ArrayList<EquipoBasicoPrestamo>();
        }
        return listaEquiposBasicos;
    }

    /**
     * @param listaEquiposBasicos the listaEquiposBasicos to set
     */
    public void setListaEquiposBasicos(List<EquipoBasicoPrestamo> listaEquiposBasicos) {
        this.listaEquiposBasicos = listaEquiposBasicos;
    }

    /**
     * @return the listaEquiposBasicos1
     */
    public List<EquipoBasicoPrestamo> getListaEquiposBasicosOne() {
        if(listaEquiposBasicosOne==null){
            listaEquiposBasicosOne=new ArrayList<EquipoBasicoPrestamo>();
        }
        return listaEquiposBasicosOne;
    }

    /**
     * @param listaEquiposBasicos1 the listaEquiposBasicos to set
     */
    public void setListaEquiposBasicosOne(List<EquipoBasicoPrestamo> listaEquiposBasicosOne) {
        this.listaEquiposBasicosOne = listaEquiposBasicosOne;
    }
    
    /**
     * @return the equipoSeleccionado
     */
    public EquipoPrestamo getEquipoSeleccionado() {
        return equipoSeleccionado;
    }

    /**
     * @param equipoSeleccionado the equipoSeleccionado to set
     */
    public void setEquipoSeleccionado(EquipoPrestamo equipoSeleccionado) {
        this.equipoSeleccionado = equipoSeleccionado;
    }



    /**
     * @return the cantidadEquipoBasicoSeleccionada
     */
    public int getCantidadEquipoBasicoSeleccionada() {
        return cantidadEquipoBasicoSeleccionada;
    }

    /**
     * @param cantidadEquipoBasicoSeleccionada the cantidadEquipoBasicoSeleccionada to set
     */
    public void setCantidadEquipoBasicoSeleccionada(int cantidadEquipoBasicoSeleccionada) {
        this.cantidadEquipoBasicoSeleccionada = cantidadEquipoBasicoSeleccionada;
    }

    /**
     * @return the prestamoEquipoBasicoSeleccionado
     */
    public EquipoBasicoPrestamo getPrestamoEquipoBasicoSeleccionado() {
        return prestamoEquipoBasicoSeleccionado;
    }

    /**
     * @param prestamoEquipoBasicoSeleccionado the prestamoEquipoBasicoSeleccionado to set
     */
    public void setPrestamoEquipoBasicoSeleccionado(EquipoBasicoPrestamo prestamoEquipoBasicoSeleccionado) {
        this.prestamoEquipoBasicoSeleccionado = prestamoEquipoBasicoSeleccionado;
    }

    /**
     * @return the tipoPrestamoSeleccionadoDos
     */
    public String getTipoPrestamoSeleccionadoDos() {
        return tipoPrestamoSeleccionadoDos;
    }

    /**
     * @param tipoPrestamoSeleccionadoDos the tipoPrestamoSeleccionadoDos to set
     */
    public void setTipoPrestamoSeleccionadoDos(String tipoPrestamoSeleccionadoDos) {
        this.tipoPrestamoSeleccionadoDos = tipoPrestamoSeleccionadoDos;
    }
}
