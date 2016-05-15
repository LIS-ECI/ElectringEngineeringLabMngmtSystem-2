/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.persistence;

import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.Modelo;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author 2107262
 */
public interface DaoEquipo {
    
    /**
    * carga un modelo dado el nombre de este
    * @param nombre, el nombre del modelo a cargar
    * @return el modelo identificado con ese nombre
    */
    public Modelo loadModeloByName(String nombre) throws PersistenceException;
    
    /**
     * carga un equipo dado el serial de este
     * @param serial, el serial del equipo a cargar
     * @return el equipo identificado con ese serial
     */
    public Equipo loadEquipoBySerial(int serial) throws PersistenceException;
    
    /**
    * carga un equipo basico dado el nombre de este
    * @param nombre, el nombre del equipo basico a cargar
    * @return el equipo basico identificado con ese nombre
    */
    public EquipoBasico loadEquipoBasicoByName(String nombre) throws PersistenceException;
    
    /**
    * cargar todos los modelos
    * @return un contenedor con todos los modelos que se han registrado
    */
    public Set<Modelo> loadModelos() throws PersistenceException;
    
    /**
     * cargar todos los equipos basicos
     * @return un contenedor con todos los equipos basicos que se han registrado
     */
    public Set<EquipoBasico> loadEquiposBasicos() throws PersistenceException;
    
    /**
    * registrar un modelo nuevo dado el modelo como entidad (objeto)
    * @param m, el modelo a registrar en la bd
    */
    public void registrarModeloNuevo(Modelo m) throws PersistenceException;
    
    /**
     * registrar un equipo nuevo dado el equipo como entidad (objeto)
     * @param e, el equipo a registrar en la bd
     * @param modelo, el modelo al que pertenece este equipo
     */    
    public void registrarEquipoNuevo(Equipo e,String modelo) throws PersistenceException;
    
    /**
    * registrar un equipo basico nuevo dado el equipo basico como entidad (objeto)
    * @param eq, el equipo basico a registrar en la bd
    */
    public void registrarEquipoBasicoNuevo(EquipoBasico eq) throws PersistenceException;
    
    public void updateEquipoBasico(EquipoBasico equipoBasico,int cantidad);

    public void updateEstadoEquipo(int codigoEquipo, String tipoPrestamoSeleccionadoDos);

    public void updateCantidadEquipoBasico(String nombreEquipoBasicoPrestar, int cantidadEquipoBasicoSeleccionada);

    public Equipo loadEquipoByPlaca(int placa);

    public String loadNameModeloByPlaca(int placa);
    
}
