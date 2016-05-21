/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.project.managedBeans;
import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.entities.PrestamoUsuario;
import edu.eci.pdsw.entities.Usuario;
import edu.eci.pdsw.services.Services;
import edu.eci.pdsw.services.ServicesException;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author 2107262
 */
@ManagedBean(name="Devolucion")
@SessionScoped
public class ServiciosDevolucionesBean implements Serializable{

    public ServiciosDevolucionesBean() {
        try {
            se = Services.getInstance("applicationconfig.properties");
            nombresEquiposBasicos = new HashMap<>();
            Set<EquipoBasico> eb = se.loadEquiposBasicos();
            for(EquipoBasico b:eb){
                nombresEquiposBasicos.put(b.getNombre(),b.getNombre());
            }
        } catch (ServicesException ex) {
            Logger.getLogger(ServiciosDevolucionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @ManagedProperty(value = "#{loginBean}")    
    private ShiroLoginBean loginBean;
    //datos para una devolucion de un equipo normal
    private boolean yaBusqueEquipoADevolver = false;
    private boolean serialDevolucionEncontrado = false;
    private boolean serialDevolucionNoEncontrado = true;
    private String textoSalidaEquipoADevolver;
    private String serialADevolver;
    private Usuario usuarioDevolucion;
    java.sql.Date horaActual = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    //datos para una devolucion de un equipo basico
    private String condigoEstudianteBasicos;
    private String nombreEquipoBasicoDevolver;
    private int cantidadBasicaDevuelta=1;
    private Usuario usuarioDevolucionBasico;
    //datos para una devolucion global
    private String codigoUsuario;
    private boolean usuarioExiste=false;
    private List<PrestamoUsuario> listaPrestamoActuales;
    private List<PrestamoBasicoUsuario> listaPrestamoBasicoActuales;
    
    private HashMap<String, String> nombresEquiposBasicos;
    Services se;
    
    
    /**
     * boton aceptar, para registrar una devolucion global
     */
    public void botonAceptarDevolucionGlobal(){
        
        RequestContext context = RequestContext.getCurrentInstance();
        boolean continua=true;
        int codigo=0;
        try{
            codigo=Integer.parseInt(codigoUsuario);
        }catch(Exception e){
            continua=false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "El codigo del usuario debe ser un entero positivo"));
        }
        if(continua){
            try {
                Usuario usuario=se.loadUsuarioById(codigo);
                listaPrestamoActuales=new ArrayList<PrestamoUsuario>();
                listaPrestamoBasicoActuales=new ArrayList<PrestamoBasicoUsuario>();
                for(PrestamoUsuario p:usuario.getPrestamos()){
                    if(p.getFechaVencimiento()==null){
                        listaPrestamoActuales.add(p);
                    }
                }
                for(PrestamoBasicoUsuario pb:usuario.getPrestamosBasicos()){
                    if(pb.getFechaVencimiento()==null){
                        listaPrestamoBasicoActuales.add(pb);
                    }
                }
                usuarioExiste=true;
                context.update(":form69:registrarGlobal");
            } catch (ServicesException ex) {
                usuarioExiste=false;
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getLocalizedMessage()));
            }
        }
    }
    
    /**
     * botonFinalDevolucioGlobal, este boton es el que realmente cnofirma la devolucion (cambia el estado de los equipos)
     */
    public void botonFinalDevolucioGlobal(){
        try{
            RequestContext context = RequestContext.getCurrentInstance();
            if(listaPrestamoActuales!=null){
                for (int i = 0; i < listaPrestamoActuales.size(); i++) {
                    se.updatePrestamos(listaPrestamoActuales.get(i),Integer.parseInt(codigoUsuario));
                }
            }
            if(listaPrestamoBasicoActuales!=null){
                for (int j = 0; j < listaPrestamoBasicoActuales.size(); j++) {
                    se.updatePrestamosBasicos(listaPrestamoBasicoActuales.get(j),Integer.parseInt(codigoUsuario));
                }
            }
            context.execute("PF('devolucionGlobal').hide();");
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Succes","Se han registrado todas las devoluciónes con exito"));
            limpiarRegistrarDevolucionGlobal();
            context.update("form69");
            
        }catch(ServicesException ex){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getLocalizedMessage()));
        }
    }
    
    /**
     * limpia registrar una devolucion global
     */
    public void limpiarRegistrarDevolucionGlobal(){
        usuarioExiste=false;
        listaPrestamoBasicoActuales=new ArrayList<PrestamoBasicoUsuario>();
        listaPrestamoActuales=new ArrayList<PrestamoUsuario>();
        codigoUsuario="";
    }
    
    
    /**
     * deja a todos los elementos involucrados en una devolución de equipo normal en su estado original
     */
    public void limpiarDevolucion(){
        usuarioExiste=false;
        yaBusqueEquipoADevolver = false;
        serialDevolucionEncontrado = false;
        serialDevolucionNoEncontrado = true;
        textoSalidaEquipoADevolver = null;
        setSerialADevolver(null);
        usuarioDevolucion = null;
    
    }
    
    /**
     * deja a todos los elementos involucrados en una devolución de equipo basico en su estado original
     */
    public void limpiarDevolucionBasica(){
        condigoEstudianteBasicos = null;
        nombreEquipoBasicoDevolver = null;
        cantidadBasicaDevuelta = 1;
        usuarioDevolucionBasico = null;
    }
    /**
     * deja a todos los elementos involucrados en una devolución en su estado original
     */
    public void limpiarPaginaRegistrarUnaDevolucion(){
        limpiarDevolucion();
        limpiarDevolucionBasica();
    }
    
    /**
     * Busca si existe algun usuario que tenga actualmente en prestamo el equipo del serial ingresado
     */
    public void accionBuscarDevolucion() {
        try {
            setUsuarioDevolucion(null);
            Set<Usuario> usuarios = se.loadUsuarios();
            int serial=Integer.parseInt(getSerialADevolver());
            for (Usuario u : usuarios) {
                Set<PrestamoUsuario> prestamos = u.getPrestamos();
                for (PrestamoUsuario p : prestamos) {
                    if (p.getEquipo_serial() == serial && p.getFechaVencimiento() == null) {
                        setUsuarioDevolucion(u);
                        setSerialDevolucionEncontrado(true);
                        setSerialDevolucionNoEncontrado(false);
                        setTextoSalidaEquipoADevolver("El usuario con el prestamo del equipo fue encontrado exitosamente");
                    }
                }
            }
            if (getUsuarioDevolucion() == null) {
                setSerialDevolucionEncontrado(false);
                setSerialDevolucionNoEncontrado(true);
                setTextoSalidaEquipoADevolver("No fue encontrado un usuario con el presente equipo  " + getSerialADevolver() + " en prestamo ");
            }
            setYaBusqueEquipoADevolver(true);
            
        }
        catch(NumberFormatException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "El serial "+serialADevolver+" no esta permitido, debe ser un numero positivo"));
            RequestContext context = RequestContext.getCurrentInstance();
            setTextoSalidaEquipoADevolver("");
        }
        catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Ocurrio un error inemin=\"0\"erado"));
            RequestContext context = RequestContext.getCurrentInstance();
        }
    }
    
    /**
     * realiza la devolución del equipo actual actualizando la fechaVencimiento del prestamo y actualizando el estado del equipo
     * a "Activo" y el subestado a "En almacén"
     */
    public void accionRealizarDevolucion() {
        //la fecha actual entrega anho,mes y dia pero no minutos ni segundos.
        try{
            Set<PrestamoUsuario> prestamos = usuarioDevolucion.getPrestamos();
            for (PrestamoUsuario p : prestamos) {
                if (p.getEquipo_serial() == Integer.parseInt(getSerialADevolver()) && p.getFechaVencimiento() == null) {
                    se.updatePrestamos(p,usuarioDevolucion.getId());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Completado", "Se ha realizado la devolución exitosamente"));
                    RequestContext context = RequestContext.getCurrentInstance();
                    break;
                }
            }
            limpiarDevolucion();
        }
        catch(ServicesException e){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",e.getLocalizedMessage()));
        }
    }
    
    /**
     * Realiza la devolución del equipo básico actual actualizando la fechaVencimiento del prestamo y actualizando la cantidad en almacen
     * del equipo
     */
    public void accionRealizarDevolucionBasica() {
        try {
            Set<Usuario> usuarios = se.loadUsuarios();
            for (Usuario u : usuarios) {
                Set<PrestamoBasicoUsuario> prestamos = u.getPrestamosBasicos();
                for (PrestamoBasicoUsuario p : prestamos) {
                    if (p.getCantidadPrestada() == cantidadBasicaDevuelta && p.getFechaVencimiento() == null && p.getEquipoBasico_nombre().equals(nombreEquipoBasicoDevolver) && Integer.parseInt(condigoEstudianteBasicos)==u.getId()) {
                        setUsuarioDevolucionBasico(u);
                        se.updatePrestamosBasicos(p, u.getId());
                    }
                }
            }
            if(usuarioDevolucionBasico != null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Completado", "Se ha realizado la devolución exitosamente"));
                RequestContext context = RequestContext.getCurrentInstance();
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "No se encontro usuario con los objetos seleccionados prestados"));
                RequestContext context = RequestContext.getCurrentInstance();
            }
            limpiarDevolucionBasica();
        } catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","vaya a saber"));
        }
    }

    /**
     * @return the yaBusqueEquipoADevolver
     */
    public boolean isYaBusqueEquipoADevolver() {
        return yaBusqueEquipoADevolver;
    }

    /**
     * @param yaBusqueEquipoADevolver the yaBusqueEquipoADevolver to set
     */
    public void setYaBusqueEquipoADevolver(boolean yaBusqueEquipoADevolver) {
        this.yaBusqueEquipoADevolver = yaBusqueEquipoADevolver;
    }

    /**
     * @return the serialDevolucionEncontrado
     */
    public boolean isSerialDevolucionEncontrado() {
        return serialDevolucionEncontrado;
    }

    /**
     * @param serialDevolucionEncontrado the serialDevolucionEncontrado to set
     */
    public void setSerialDevolucionEncontrado(boolean serialDevolucionEncontrado) {
        this.serialDevolucionEncontrado = serialDevolucionEncontrado;
    }

    /**
     * @return the serialDevolucionNoEncontrado
     */
    public boolean isSerialDevolucionNoEncontrado() {
        return serialDevolucionNoEncontrado;
    }

    /**
     * @param serialDevolucionNoEncontrado the serialDevolucionNoEncontrado to set
     */
    public void setSerialDevolucionNoEncontrado(boolean serialDevolucionNoEncontrado) {
        this.serialDevolucionNoEncontrado = serialDevolucionNoEncontrado;
    }

    /**
     * @return the textoSalidaEquipoADevolver
     */
    public String getTextoSalidaEquipoADevolver() {
        return textoSalidaEquipoADevolver;
    }

    /**
     * @param textoSalidaEquipoADevolver the textoSalidaEquipoADevolver to set
     */
    public void setTextoSalidaEquipoADevolver(String textoSalidaEquipoADevolver) {
        this.textoSalidaEquipoADevolver = textoSalidaEquipoADevolver;
    }

    /**
     * @return the serialADevolver
     */
    public String getSerialADevolver() {
        return serialADevolver;
    }

    /**
     * @param serialADevolver the serialADevolver to set
     */
    public void setSerialADevolver(String serialADevolver) {
        this.serialADevolver = serialADevolver;
    }

    /**
     * @return the usuarioDevolucion
     */
    public Usuario getUsuarioDevolucion() {
        return usuarioDevolucion;
    }

    /**
     * @param usuarioDevolucion the usuarioDevolucion to set
     */
    public void setUsuarioDevolucion(Usuario usuarioDevolucion) {
        this.usuarioDevolucion = usuarioDevolucion;
    }

    /**
     * @return the condigoEstudianteBasicos
     */
    public String getCondigoEstudianteBasicos() {
        return condigoEstudianteBasicos;
    }

    /**
     * @param condigoEstudianteBasicos the condigoEstudianteBasicos to set
     */
    public void setCondigoEstudianteBasicos(String condigoEstudianteBasicos) {
        this.condigoEstudianteBasicos = condigoEstudianteBasicos;
    }

    /**
     * @return the nombreEquipoBasicoDevolver
     */
    public String getNombreEquipoBasicoDevolver() {
        return nombreEquipoBasicoDevolver;
    }

    /**
     * @param nombreEquipoBasicoDevolver the nombreEquipoBasicoDevolver to set
     */
    public void setNombreEquipoBasicoDevolver(String nombreEquipoBasicoDevolver) {
        this.nombreEquipoBasicoDevolver = nombreEquipoBasicoDevolver;
    }

    /**
     * @return the cantidadBasicaDevuelta
     */
    public int getCantidadBasicaDevuelta() {
        return cantidadBasicaDevuelta;
    }

    /**
     * @param cantidadBasicaDevuelta the cantidadBasicaDevuelta to set
     */
    public void setCantidadBasicaDevuelta(int cantidadBasicaDevuelta) {
        this.cantidadBasicaDevuelta = cantidadBasicaDevuelta;
    }

    /**
     * @return the usuarioDevolucionBasico
     */
    public Usuario getUsuarioDevolucionBasico() {
        return usuarioDevolucionBasico;
    }

    /**
     * @param usuarioDevolucionBasico the usuarioDevolucionBasico to set
     */
    public void setUsuarioDevolucionBasico(Usuario usuarioDevolucionBasico) {
        this.usuarioDevolucionBasico = usuarioDevolucionBasico;
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
     * @return the nombresEquiposBasicos
     */
    public HashMap<String, String> getNombresEquiposBasicos() {
        try {
            se = Services.getInstance("applicationconfig.properties");
            nombresEquiposBasicos = new HashMap<>();
            Set<EquipoBasico> eb = se.loadEquiposBasicos();
            for(EquipoBasico b:eb){
                nombresEquiposBasicos.put(b.getNombre(),b.getNombre());
            }
        } catch (ServicesException ex) {
            Logger.getLogger(ServiciosDevolucionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombresEquiposBasicos;
    }

    /**
     * @param nombresEquiposBasicos the nombresEquiposBasicos to set
     */
    public void setNombresEquiposBasicos(HashMap<String, String> nombresEquiposBasicos) {
        this.nombresEquiposBasicos = nombresEquiposBasicos;
    }

    /**
     * @return the nombreUsuario
     */
    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    /**
     * @param codigoUsuario the nombreUsuario to set
     */
    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    /**
     * @return the usuarioExiste
     */
    public boolean isUsuarioExiste() {
        return usuarioExiste;
    }

    /**
     * @param usuarioExiste the usuarioExiste to set
     */
    public void setUsuarioExiste(boolean usuarioExiste) {
        this.usuarioExiste = usuarioExiste;
    }

    /**
     * @return the listaPrestamoActuales
     */
    public List<PrestamoUsuario> getListaPrestamoActuales() {
        return listaPrestamoActuales;
    }

    /**
     * @param listaPrestamoActuales the listaPrestamoActuales to set
     */
    public void setListaPrestamoActuales(List<PrestamoUsuario> listaPrestamoActuales) {
        this.listaPrestamoActuales = listaPrestamoActuales;
    }

    /**
     * @return the listaPrestamoBasicoActuales
     */
    public List<PrestamoBasicoUsuario> getListaPrestamoBasicoActuales() {
        return listaPrestamoBasicoActuales;
    }

    /**
     * @param listaPrestamoBasicoActuales the listaPrestamoBasicoActuales to set
     */
    public void setListaPrestamoBasicoActuales(List<PrestamoBasicoUsuario> listaPrestamoBasicoActuales) {
        this.listaPrestamoBasicoActuales = listaPrestamoBasicoActuales;
    }

    
}
