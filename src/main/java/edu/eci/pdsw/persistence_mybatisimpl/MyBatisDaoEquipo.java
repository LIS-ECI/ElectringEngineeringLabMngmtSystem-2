/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.persistence_mybatisimpl;

import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.mybatis.mappers.EquipoMapper;
import org.apache.ibatis.session.SqlSession;
import edu.eci.pdsw.persistence.DaoEquipo;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author 2107262
 */
public class MyBatisDaoEquipo implements DaoEquipo{
    private EquipoMapper emap=null;

    public MyBatisDaoEquipo(SqlSession session) {        
        emap=session.getMapper(EquipoMapper.class);
    }
    @Override
    public void registrarEquipoNuevo(Equipo e,String m) throws PersistenceException{
        if(m==null && emap.loadModeloByName(m)==null){
            throw new PersistenceException("El modelo del equipo a registrar no se encuentra actualmente en la base de datos");
        }
        if(emap.loadEquipoBySerial(e.getSerial())!=null){
            throw new PersistenceException("El equipo ya se encuentra registrado en la base de datos");
        }
        emap.registrarEquipoNuevo(e,m);
    }

    @Override
    public void registrarModeloNuevo(Modelo m) throws PersistenceException{
        if(emap.loadModeloByName(m.getNombre())!=null){
            throw new PersistenceException("El modelo con nombre "+m.getNombre()+" ya se encuentra registrado en la base de datos");
        }
        emap.registrarModeloNuevo(m);
    }

    @Override
    public Modelo loadModeloByName(String nombre) throws PersistenceException{
        Modelo mod=emap.loadModeloByName(nombre);
        if(mod==null){
            throw new PersistenceException("El modelo con nombre "+nombre+" no se encuentra registrado en la base de datos");
        }
        return mod;
    }

    @Override
    public Equipo loadEquipoBySerial(int serial) throws PersistenceException{
        Equipo eq=emap.loadEquipoBySerial(serial);
        
        if(eq==null){
            throw new PersistenceException("El equipo con serial"+serial+" no se encuentra registrado en la base de datos");
        }
        return eq;
    }

    @Override
    public EquipoBasico loadEquipoBasicoByName(String nombre) throws PersistenceException{
        EquipoBasico eqb=emap.loadEquipoBasicoByName(nombre);
        if(eqb==null){
            throw new PersistenceException("El equipo básico con nombre "+nombre+" no se encuentra registrado en la base de datos");
        }
        return eqb;
    }

    @Override
    public void registrarEquipoBasicoNuevo(EquipoBasico eq) throws PersistenceException{
        if(emap.loadEquipoBasicoByName(eq.getNombre())!=null){
            throw new PersistenceException("El equipo básico con nombre "+eq.getNombre()+" ya se encuentra registrado en la base de datos");
        }
        emap.registrarEquipoBasicoNuevo(eq);
    }

    @Override
    public Set<Modelo> loadModelos() throws PersistenceException {
        Set<Modelo> ans=emap.loadModelos();
        if(ans==null){
           throw new PersistenceException("Ningún modelo se encuentra registrado en la base de datos "); 
        }
        return ans;
    }
    
    @Override
    public Set<EquipoBasico> loadEquiposBasicos() throws PersistenceException{
        Set<EquipoBasico> ans=emap.loadEquiposBasicos();
        if(ans==null){
           throw new PersistenceException("Ningún equipo bàsico se encuentra registrado en la base de datos "); 
        }
        return ans;
    }

    @Override
    public void updateEquipoBasico(EquipoBasico equipoBasico, int cantidad) throws PersistenceException {
        if(cantidad<0){
            throw new PersistenceException("La cantidad del equipo basico: "+ equipoBasico.getNombre() +", no puede ser negativa"); 
        }
        if(emap.loadEquipoBasicoByName(equipoBasico.getNombre())==null){
            throw new PersistenceException("El equipo básico con nombre "+ equipoBasico.getNombre()+"  no se encuentra en la base de datos"); 
        }
        emap.updateEquipoBasico(equipoBasico.getNombre(),cantidad);
    }

    @Override
    public void updateEstadoEquipo(int codigoEquipo, String tipoPrestamoSeleccionadoDos) {
        emap.updateEstadoEquipo(codigoEquipo,tipoPrestamoSeleccionadoDos);
    }

    @Override
    public void updateCantidadEquipoBasico(String nombreEquipoBasicoPrestar, int cantidadEquipoBasicoSeleccionada) {
        emap.updateCantidadEquipoBasico(nombreEquipoBasicoPrestar,cantidadEquipoBasicoSeleccionada);
    }

    @Override
    public Equipo loadEquipoByPlaca(int placa) {
        Equipo eq = emap.loadEquipoByPlaca(placa);
        if(eq==null){
            throw new PersistenceException("El equipo con placa "+placa+" no se encuentra registrado en la base de datos");
        }
        return eq;
    }

    @Override
    public String loadNameModeloByPlaca(int placa) {
        String name= emap.loadNameModeloByPlaca(placa);
        if(name==null){
            throw new PersistenceException("El Modelo asociado al equipo con placa "+placa+" no se encuentra registrado en la base de datos");
        }
        return name;
    }

    
}
