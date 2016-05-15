/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author 2105461
 */
public class Equipo {

    private int serial;
    private String nombre;
    private int placa;
    private int vidaInicial;
    private String marca;
    private String descripcion;
    private String estado;
    private String subEstado;
    private String proveedor;
    private Set<PrestamoEquipo> prestamos;
    
    /**
     * 
     */
    public Equipo() {
        this.prestamos = new LinkedHashSet<>();
    }
    
    /**
     * @param serial
     * @param nombre
     * @param placa
     * @param vidaInicial
     * @param marca
     * @param descripcion
     * @param estado
     * @param subEstado
     * @param proveedor 
     */
    public Equipo(int serial, String nombre, int placa,int vidaInicial, String marca, String descripcion, String estado, String subEstado, String proveedor) {
        this.serial = serial;
        this.nombre = nombre;
        this.placa = placa;
        this.vidaInicial=vidaInicial;
        this.marca = marca;
        this.descripcion = descripcion;
        this.estado = estado;
        this.subEstado = subEstado;
        this.proveedor = proveedor;
        this.prestamos = new LinkedHashSet<>();
    }

    /**
     * @return the serial
     */
    public int getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(int serial) {
        this.serial = serial;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the placa
     */
    public int getPlaca() {
        return placa;
    }

    /**
     * @param placa the placa to set
     */
    public void setPlaca(int placa) {
        this.placa = placa;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the subEstado
     */
    public String getSubEstado() {
        return subEstado;
    }

    /**
     * @param subEstado the subEstado to set
     */
    public void setSubEstado(String subEstado) {
        this.subEstado = subEstado;
    }

    /**
     * @return the proveedor
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the prestamos
     */
    public Set<PrestamoEquipo> getPrestamos() {
        return prestamos;
    }

    /**
     * @param prestamos the prestamos to set
     */
    public void setPrestamos(Set<PrestamoEquipo> prestamos) {
        this.prestamos = prestamos;
    }
    
    @Override
    public String toString() {
        String rep = "Equipo:["+serial+","+nombre+","+vidaInicial+","+placa+","+marca+","+descripcion+","+estado+","+subEstado+","+proveedor+"]\n";
        for (PrestamoEquipo p:prestamos){
            rep+="\t["+p+"]\n";
        }
        return rep;
    }

    /**
     * @return the vidaInicial
     */
    public int getVidaInicial() {
        return vidaInicial;
    }

    /**
     * @param vidaInicial the vidaInicial to set
     */
    public void setVidaInicial(int vidaInicial) {
        this.vidaInicial = vidaInicial;
    }
}

