/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.persistence_mybatisimpl;

import edu.eci.pdsw.entities.EquipoPrestamo;
import edu.eci.pdsw.entities.PrestamoBasicoEquipo;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.entities.PrestamoUsuario;
import edu.eci.pdsw.mybatis.mappers.EquipoMapper;
import edu.eci.pdsw.persistence.DaoPrestamo;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author 2105534
 */
public class MyBatisDaoPrestamo implements DaoPrestamo{
 
    private EquipoMapper emap=null;

    public MyBatisDaoPrestamo(SqlSession session) throws PersistenceException {
         emap=session.getMapper(EquipoMapper.class);
    }
    
    @Override
    public void registrarNuevoPrestamo(PrestamoEquipo pe, PrestamoUsuario pu) throws PersistenceException {
        if(pe.getFechaExpedicion().getYear()!=Calendar.getInstance().getTime().getYear() || pe.getFechaExpedicion().getMonth()!=Calendar.getInstance().getTime().getMonth() || pe.getFechaExpedicion().getDay()!= Calendar.getInstance().getTime().getDay()){
            throw new PersistenceException("La fecha ingresada no es igual a la fecha actual");   
        }
        if(emap.loadUsuarioById(pe.getUsuario_id())==null){
            throw new PersistenceException("El usuario con id "+pe.getUsuario_id()+" no se encuentra registrado en la base de datos");
        }
        if(emap.loadEquipoBySerial(pu.getEquipo_serial())==null){
            throw new PersistenceException("El equipo con serial "+pu.getEquipo_serial()+" no se encuentra registrado en la base de datos");
        }
        if(!emap.loadEquipoBySerial(pu.getEquipo_serial()).getSubEstado().equals("En almacén")){
            throw new PersistenceException("El equipo con serial "+pu.getEquipo_serial()+" se encuentra registrado en la base de datos, pero no esta disponible para prestar");
        }
        emap.registrarNuevoPrestamo(pe, pu);
    }

    @Override
    public void registrarNuevoPrestamoBasico(PrestamoBasicoEquipo pbe, PrestamoBasicoUsuario pbu) {
        if (pbe.getCantidadPrestada()<=0){
            throw new PersistenceException("La cantidad ingresada para el prestamo debe ser mayor a 0");   
        }
        if(pbe.getFechaExpedicion().getYear()!=Calendar.getInstance().getTime().getYear() || pbe.getFechaExpedicion().getMonth()!=Calendar.getInstance().getTime().getMonth() || pbe.getFechaExpedicion().getDay()!= Calendar.getInstance().getTime().getDay()){
            throw new PersistenceException("La fecha ingresada no es igual a la fecha actual");   
        }
        if(emap.loadUsuarioById(pbe.getUsuario_id())==null){
            throw new PersistenceException("El usuario con id "+pbe.getUsuario_id()+" no se encuentra registrado en la base de datos");
        }
        if(emap.loadEquipoBasicoByName(pbu.getEquipoBasico_nombre()) == null){
            throw new PersistenceException("El equipo con nombre "+pbu.getEquipoBasico_nombre()+" no se encuentra registrado en la base de datos");
        }
        if(pbu.getCantidadPrestada() > emap.loadEquipoBasicoByName(pbu.getEquipoBasico_nombre()).getCantidadInventario()){
            throw new PersistenceException("El equipo con nombre "+pbu.getEquipoBasico_nombre()+" se encuentra registrado en la base de datos, pero lo maximo que puede prestar son "+emap.loadEquipoBasicoByName(pbu.getEquipoBasico_nombre()).getCantidadInventario());
        }
        emap.registrarNuevoPrestamoBasico(pbe,pbu);
    }

    @Override
    public Set<PrestamoUsuario> loadPrestamosByUsuario(int usuario) {
        if (emap.loadUsuarioById(usuario)==null){
            throw new PersistenceException("El usuario no existe");   
        }
        else{
            Set<PrestamoUsuario> ans = emap.loadPrestamosByUsuario(usuario);
            return ans;
        }
    }

    @Override
    public Set<PrestamoBasicoUsuario> loadPrestamosBasicosByUsuario(int usuario) {
        if (emap.loadUsuarioById(usuario)==null){
            throw new PersistenceException("El usuario no existe");   
        }
        else{
            Set<PrestamoBasicoUsuario> ans = emap.loadPrestamosBasicosByUsuario(usuario);
            return ans;
        }
    }
    
}
