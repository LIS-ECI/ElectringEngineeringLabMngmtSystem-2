/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;

import java.sql.Date;

/**
 *
 * @author Jhordy
 */
public class PrestamoEquipo {

    private int usuario_id;
    private Date fechaExpedicion;
    private Date fechaVencimiento;
    private String fechaExpedicionString;
    private String fechaVencimientoString;
    private String tipoPrestamo;
    
    /**
     * 
     */
    public PrestamoEquipo() {
    }
     
    /**
     * @param usuario_id
     * @param fechaExpedicion
     * @param fechaVencimiento
     * @param tipoPrestamo 
     */
    public PrestamoEquipo(int usuario_id, Date fechaExpedicion, Date fechaVencimiento, String tipoPrestamo) {
        this.usuario_id = usuario_id;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
        this.tipoPrestamo = tipoPrestamo;
    }

    /**
     * @return the usuario_id
     */
    public int getUsuario_id() {
        return usuario_id;
    }

    /**
     * @param usuario_id the usuario_id to set
     */
    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    /**
     * @return the fechaExpedicion
     */
    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    /**
     * @param fechaExpedicion the fechaExpedicion to set
     */
    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the tipoPrestamo
     */
    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    /**
     * @param tipoPrestamo the tipoPrestamo to set
     */
    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }
    
    @Override
    public String toString() {
        return "("+usuario_id+","+fechaExpedicion+","+fechaVencimiento+","+tipoPrestamo+")";
    } 

    /**
     * @return the fechaExpedicionString
     */
    public String getFechaExpedicionString() {
        return fechaExpedicionString;
    }

    /**
     * @param fechaExpedicionString the fechaExpedicionString to set
     */
    public void setFechaExpedicionString(String fechaExpedicionString) {
        this.fechaExpedicionString = fechaExpedicionString;
    }

    /**
     * @return the fechaVencimientoString
     */
    public String getFechaVencimientoString() {
        return fechaVencimientoString;
    }

    /**
     * @param fechaVencimientoString the fechaVencimientoString to set
     */
    public void setFechaVencimientoString(String fechaVencimientoString) {
        this.fechaVencimientoString = fechaVencimientoString;
    }
}
