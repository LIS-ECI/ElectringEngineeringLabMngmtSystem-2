/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.services;

import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.entities.PrestamoBasicoEquipo;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.entities.PrestamoUsuario;
import edu.eci.pdsw.persistence.DaoFactory;
import edu.eci.pdsw.entities.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author Jhordy
 */
public class Services {

    private static Services instance=null;
    
    private final Properties properties=new Properties();
    
    private final DaoFactory dc=null;
    
    private Services(String propFileName) throws IOException{        
	InputStream input = null;
        input = this.getClass().getClassLoader().getResourceAsStream(propFileName);        
        properties.load(input);
        
    }
    
    public static Services getInstance(String propertiesFileName) throws RuntimeException{
        
        if (instance==null){
            try {
                instance=new Services(propertiesFileName);
            } catch (IOException ex) {
                throw new RuntimeException("Error on application configuration:",ex);
            }
        }        
        
        return instance;
    }
    
    /*METODOS*/
    
    /**
     * carga un usuario dado el id de este
     * @param id, el identificador del usuario a cargar
     * @return el usuario identificado con ese id
     * @throws edu.eci.pdsw.services.ServicesException
     */
    public Usuario loadUsuarioById(int id) throws ServicesException{
        Usuario ans=null;
        DaoFactory df=DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            ans =df.getDaoUsuario().loadUsuarioById(id);
            df.commitTransaction();
            
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }
        finally{
            df.endSession();
        }
        return ans;
    }
    
    /**
     * carga un modelo dado el nombre de este
     * @param nombre, el nombre del modelo a cargar
     * @return el modelo identificado con ese nombre
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public Modelo loadModeloByName(String nombre)throws ServicesException{
        Modelo ans=null;
        DaoFactory df=  DaoFactory.getInstance(properties);
        try{
           df.beginSession();
           ans = df.getDaoEquipo().loadModeloByName(nombre);
           
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        
        return ans;
    }
    
    /**
     * carga un equipo dado el serial de este
     * @param serial, el serial del equipo a cargar
     * @return el equipo identificado con ese serial
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public Equipo loadEquipoBySerial(int serial) throws ServicesException{
        DaoFactory df=DaoFactory.getInstance(properties);
        Equipo ans=null;
        try{
            df.beginSession();
            ans =df.getDaoEquipo().loadEquipoBySerial(serial);  
            
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return ans;
    }
    
    /**
     * carga un equipo basico dado el nombre de este
     * @param name, el nombre del equipo basico a cargar
     * @return el equipo basico identificado con ese nombre
     * @throws edu.eci.pdsw.services.ServicesException
    */
    public EquipoBasico loadEquipoBasicoByName(String name) throws ServicesException{
        DaoFactory df=DaoFactory.getInstance(properties);
        EquipoBasico ans=null;
        try{
            df.beginSession();
            ans =df.getDaoEquipo().loadEquipoBasicoByName(name);
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return ans; 
    }
    
    /**
     * cargar todos los prestamos
     * @return un contenedor con todos los prestamos que se han hecho
     */    
    public Set<PrestamoUsuario> loadPrestamos() throws ServicesException{
        DaoFactory df=DaoFactory.getInstance(properties);
        Set<PrestamoUsuario> ans=null;
        try{
            df.beginSession();
            ans =df.getDaoUsuario().loadPrestamos();
            df.commitTransaction();    
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return ans;
    }
    
    /**
     * registrar un equipo nuevo dado el equipo como entidad (objeto)
     * @param equipo, el equipo a registrar en la bd
     * @param modelo, el modelo al que pertenece este equipo
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public void registroEquipoNuevo(Equipo equipo,String modelo)throws ServicesException{
        DaoFactory df=DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoEquipo().registrarEquipoNuevo(equipo,modelo);
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }
    
    /**
     * registrar un modelo nuevo dado el modelo como entidad (objeto)
     * @param modelo , el modelo a registrar en la bd
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public void registroModeloNuevo(Modelo modelo) throws ServicesException{
        DaoFactory df=DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoEquipo().registrarModeloNuevo(modelo);
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }
    
    /**
     * registrar un equipo nuevo dado el equipo como entidad (objeto)
     * @param equipobasico , el equipo a registrar en la bd
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public void registroEquipoBasicoNuevo(EquipoBasico equipobasico) throws ServicesException{
        DaoFactory df=DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoEquipo().registrarEquipoBasicoNuevo(equipobasico);
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }
    
     /**
     * cargar todos los usuarios
     * @return un contenedor con todos los usuarios que estan registrados
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public Set<Usuario> loadUsuarios() throws ServicesException{
        DaoFactory df= DaoFactory.getInstance(properties);
        Set<Usuario> usuarios=null;
        try{
            df.beginSession();
            usuarios = df.getDaoUsuario().loadUsuarios();
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return usuarios;
    }
    
    /**
     * cargar todos los modelos
     * @return un contenedor con todos los modelos que se han registrado
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public Set<Modelo> loadModelos() throws ServicesException{
        DaoFactory df= DaoFactory.getInstance(properties);
        Set<Modelo> modelos=null;
        try{
            df.beginSession();
            modelos = df.getDaoEquipo().loadModelos();
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return modelos;
    }
    
    /**
     * cargar todos los equipos basicos
     * @return un contenedor con todos los equipos basicos que se han registrado
     * @throws edu.eci.pdsw.services.ServicesException
     */    
    public Set<EquipoBasico> loadEquiposBasicos() throws ServicesException{
        DaoFactory df= DaoFactory.getInstance(properties);
        Set<EquipoBasico> equiposBasicos=null;
        try{
            df.beginSession();
            equiposBasicos= df.getDaoEquipo().loadEquiposBasicos();
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return equiposBasicos;
    }
    
    /**
     *
     * @param equipoBasico
     * @param cantidad
     * @throws ServicesException
     */
    public void updateEquipoBasico(EquipoBasico equipoBasico,int cantidad) throws ServicesException{
        DaoFactory df= DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoEquipo().updateEquipoBasico(equipoBasico,cantidad);
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }
    /**
     *Actualiza la fechaVencimiento del prestamo a la fecha actual y actualiza el estado del equipo
     * a "Activo" y el subestado a "En almacén"
     *@param p,PrestamoUsuario del cual se saca la información para realizar la actualización
     *@param usuario, identificador del usuario que realiza la devolución
     */
    public void updatePrestamos(PrestamoUsuario p, int usuario) throws ServicesException {
        DaoFactory df= DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoDevolucion().updatePrestamos(p.getEquipo_serial(), usuario);
            df.getDaoDevolucion().updateEquiposDevo(p.getEquipo_serial());
            df.commitTransaction();
        }catch(PersistenceException e){
            df.rollbackTransaction();
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    } 
    
    /**
     *Actualiza la fechaVencimiento del prestamo basico a la fecha actual y actualiza la cantidad
     *del equipo basico en almacen sumando a la que esta en almacen la devuelta
     *@param p,PrestamoBasicoUsuario del cual se saca la información para realizar la actualización
     *@param usuario, identificador del usuario que realiza la devolución
     */
    public void updatePrestamosBasicos(PrestamoBasicoUsuario p, int usuario) throws ServicesException {
        DaoFactory df= DaoFactory.getInstance(properties);
         try{
            df.beginSession();
            df.getDaoDevolucion().updatePrestamosBasicos(p.getEquipoBasico_nombre(), usuario);
            df.getDaoDevolucion().updateEquiposBasicosDevo(p.getEquipoBasico_nombre(), p.getCantidadPrestada());
            df.commitTransaction();
        }catch(PersistenceException e){
            df.rollbackTransaction();
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    } 
    public void registrarNuevoPrestamo(PrestamoEquipo pe,PrestamoUsuario pu) throws ServicesException{
        DaoFactory df= DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoPrestamo().registrarNuevoPrestamo(pe, pu);
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }

    public void updateEstadoEquipo(String codigoEquipo, String tipoPrestamoSeleccionadoDos) throws ServicesException {
        DaoFactory df= DaoFactory.getInstance(properties);
         try{
            df.beginSession();
            df.getDaoEquipo().updateEstadoEquipo(Integer.parseInt(codigoEquipo),tipoPrestamoSeleccionadoDos);
            df.commitTransaction();
        }catch(PersistenceException e){
            df.rollbackTransaction();
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }

    public void registrarNuevoPrestamoBasico(PrestamoBasicoEquipo pbe, PrestamoBasicoUsuario pbu) throws ServicesException {
         DaoFactory df= DaoFactory.getInstance(properties);
        try{
            df.beginSession();
            df.getDaoPrestamo().registrarNuevoPrestamoBasico(pbe, pbu);
            df.commitTransaction();
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }

    public void updateCantidadEquipoBasico(String nombreEquipoBasicoPrestar, int cantidadEquipoBasicoSeleccionada) throws ServicesException {
        DaoFactory df= DaoFactory.getInstance(properties);
         try{
            df.beginSession();
            df.getDaoEquipo().updateCantidadEquipoBasico(nombreEquipoBasicoPrestar,cantidadEquipoBasicoSeleccionada);
            df.commitTransaction();
        }catch(PersistenceException e){
            df.rollbackTransaction();
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
    }

    public Equipo loadEquipoByPlaca(int placa) throws ServicesException {
        DaoFactory df=DaoFactory.getInstance(properties);
        Equipo ans=null;
        try{
            df.beginSession();
            ans =df.getDaoEquipo().loadEquipoByPlaca(placa);  
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return ans;
    }

    public String loadNameModeloByPlaca(int placa) throws ServicesException {
        DaoFactory df=DaoFactory.getInstance(properties);
        String ans= null;
        try{
            df.beginSession();
            ans=df.getDaoEquipo().loadNameModeloByPlaca(placa);
        }catch(PersistenceException e){
            throw new ServicesException(e,e.getLocalizedMessage());
        }finally{
           df.endSession();  
        }
        return ans;
    }
    
}
