/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eci.pdsw.entities;

import java.util.Set;

/**
 *
 * @author 2105461
 */
public class Rol {
    public String rol;
    public Set<Rol_Usuario> rolesUsuarios;
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    public Rol(String rol){
        this.rol=rol;
    }
}
