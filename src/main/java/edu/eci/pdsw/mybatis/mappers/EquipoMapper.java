
/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.mybatis.mappers;
import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.entities.PrestamoBasicoEquipo;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.entities.PrestamoUsuario;
import edu.eci.pdsw.entities.Usuario;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author hcadavid
 */
public interface EquipoMapper {
    
    /**
     * carga un usuario dado el id de este
     * @param id, el identificador del usuario a cargar
     * @return el usuario identificado con ese id
     */
    public Usuario loadUsuarioById(@Param("idusuario") int id);
    
    /**
     * carga un modelo dado el nombre de este
     * @param nombre, el nombre del modelo a cargar
     * @return el modelo identificado con ese nombre
     */
    public Modelo loadModeloByName(@Param("nombremodelo")String nombre);
    
    /**
     * carga un equipo dado el serial de este
     * @param serial, el serial del equipo a cargar
     * @return el equipo identificado con ese serial
     */
    public Equipo loadEquipoBySerial(@Param("serialequipo")int serial);
    
    /**
     * carga un equipo basico dado el nombre de este
     * @param nombre, el nombre del equipo basico a cargar
     * @return el equipo basico identificado con ese nombre
     */
    public EquipoBasico loadEquipoBasicoByName(@Param("nombreequipobasico")String nombre);
    
    /**
     * cargar todos los prestamos
     * @return un contenedor con todos los prestamos que se han hecho
     */
    public Set<PrestamoUsuario> loadPrestamos();
    
     /**
     * cargar todos los usuarios
     * @return un contenedor con todos los usuarios que estan registrados
     */
    public Set<Usuario> loadUsuarios();
    
    /**
     * cargar todos los modelos
     * @return un contenedor con todos los modelos que se han registrado
     */
    public Set<Modelo> loadModelos();
    
    /**
     * cargar todos los equipos basicos
     * @return un contenedor con todos los equipos basicos que se han registrado
     */
    public Set<EquipoBasico> loadEquiposBasicos();
    
    /**
     * actualiza la cantidad de un  equipo basico
     * @param equipoBasico el equipo a actualizar
     * @param cantidad la nueva cantidad del equipo basico 
     */
    public void updateEquipoBasico(@Param("equipoBasico")String equipoBasico,@Param("cantidad")int cantidad);

    /**
     * registrar un modelo nuevo dado el modelo como entidad (objeto)
     * @param m, el modelo a registrar en la bd
     */
    public void registrarModeloNuevo(@Param("modelo")Modelo m);
    
    /**
     * registrar un equipo nuevo dado el equipo como entidad (objeto)
     * @param e, el equipo a registrar en la bd
     * @param modelo, el modelo al que pertenece este equipo
     */
    public void registrarEquipoNuevo(@Param("equipo")Equipo e,@Param("modelo") String modelo);

    /**
     * registrar un equipo basico nuevo dado el equipo basico como entidad (objeto)
     * @param eb, el equipo basico a registrar en la bd
     */
    public void registrarEquipoBasicoNuevo(@Param("equipoBasico")EquipoBasico eb); 
    
   /**
     * Actualizar la fecha de vencimiento de un prestamo normal
     * @param s, serial del equipo a devolver
     * @param u, identificador del usuario que esta devolviendo el equipo
     */
    public void updatePrestamo(@Param("serial_equipo") int s,@Param("id_usuario") int u);
    
    /**
     * Actualizar la fecha de vencimiento para un préstamo de equipo básico
     * @param n, nombre del equipo basico a devolver
     * @param u, id del usuario que realiza la devolución
     */
    public void updatePrestamosBasicos(@Param("nombre_equipo")String n,@Param("id_usuario") int u);
    
    /**
     * Actualizar la cantidad en almacen de un equipo básico despues de una devolución
     * @param n, nombre del equipo basico a devolver
     * @param c, cantidad devuelta
     */
    public void updateEquiposBasicosDevo(@Param("nombre_equipo")String n,@Param("cantidad") int c);
    
    /**
     * Actualizar el estado y el subestado de un equipo despues de una devolución
     * @param equipo_serial, serial del equipo devuelto
     */
    public void updateEquipoDevo(@Param("equipo_serial")int equipo_serial);
    
    /**
     * 
     * @param pe
     * @param pu 
     */
    public void registrarNuevoPrestamo(@Param("pe")PrestamoEquipo pe,@Param("pu")PrestamoUsuario pu);
    
    /**
     * 
     * @param codigoEquipo
     * @param tipoPrestamoSeleccionadoDos 
     */
    public void updateEstadoEquipo(@Param("serial") int codigoEquipo,@Param("prestamo") String tipoPrestamoSeleccionadoDos);

    /**
     * 
     * @param nombreEquipoBasicoPrestar
     * @param cantidadEquipoBasicoSeleccionada 
     */
    public void updateCantidadEquipoBasico(@Param("nombre") String nombreEquipoBasicoPrestar,@Param("cantidad") int cantidadEquipoBasicoSeleccionada);
    
    /**
     * 
     * @param pbe
     * @param pbu 
     */
    public void registrarNuevoPrestamoBasico(@Param("pbe") PrestamoBasicoEquipo pbe,@Param("pbu") PrestamoBasicoUsuario pbu);

    /**
     * 
     * @param placa
     * @return 
     */
    public Equipo loadEquipoByPlaca(@Param("placaequipo")int placa);
    
    /**
     * 
     * @param placa
     * @return 
     */
    public String loadNameModeloByPlaca(@Param("placaequipo")int placa);
}
