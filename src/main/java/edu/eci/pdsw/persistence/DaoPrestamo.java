/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.persistence;

import edu.eci.pdsw.entities.EquipoPrestamo;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.entities.PrestamoBasicoEquipo;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.entities.PrestamoUsuario;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author 2105534
 */
public interface DaoPrestamo {
     /**
    * registrar un prestamo nuevo dado el prestamo como entidad (objeto)
    * @param pe, el prestamoEquipo a registrar en la bd
    * @param pu, el prestamoUsuario a registrar en la bd
    */
    public void registrarNuevoPrestamo(PrestamoEquipo pe,PrestamoUsuario pu) throws PersistenceException;
    
    /**
     * registrar un prestamo basico nuevo dado el prestamo basico como entidad (objeto) 
     * @param pbe, el prestamoBasicoEquipo a registrar en la bd
     * @param pbu , el prestamoBasicoUsuario a registrar en la bd
     */
    public void registrarNuevoPrestamoBasico(PrestamoBasicoEquipo pbe, PrestamoBasicoUsuario pbu);

    public Set<PrestamoUsuario> loadPrestamosByUsuario(int usuario);

    public Set<PrestamoBasicoUsuario> loadPrestamosBasicosByUsuario(int usuario);
    
}
