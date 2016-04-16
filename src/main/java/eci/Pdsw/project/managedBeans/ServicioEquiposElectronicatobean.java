/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eci.pdsw.project.managedBeans;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2105534
 */
@ManagedBean(name="Equipos")
@SessionScoped
public class ServicioEquiposElectronicatobean implements Serializable{
    //Pagina principal
    private int identificador;
    private String clave;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private String medidaLargo=demeLargo();
    private String medidaAncho=demeAncho();
    
    
    private String demeLargo(){
        return (screenSize.height-150)+"px";
    }
    private String demeAncho(){
        return screenSize.width+"px";
    }
    
    
    /**
     * Accion boton login en la pagina principal
     * @return el string de la pagina a redireccionar
     */
    public String accionBotonLogin(){
        boolean loEncontro=true;
        String ans="";
        //intenta buscar en base de datos, si no lo encuentra entonces LoEncontro=false
        if(loEncontro){
            //redirecciona a la pagina nueva
            System.out.println(identificador);
            System.out.println(clave);
            ans="laboratorista";
            //pone los atributos de la pagina laboratorista con los datos del usuario nuevo
        }
        else{
            //no lo encontro, y se redirecciona a la misma pagina y lanza algun tipo de error
        }
        
        return ans;
    }
    /**
     * @return the identificador
     */
    public int getIdentificador() {
        return identificador;
    }

    /**
     * @param identificador the identificador to set
     */
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the medidaLargo
     */
    public String getMedidaLargo() {
        return medidaLargo;
    }

    /**
     * @param medidaLargo the medidaLargo to set
     */
    public void setMedidaLargo(String medidaLargo) {
        this.medidaLargo = medidaLargo;
    }

    /**
     * @return the medidaAncho
     */
    public String getMedidaAncho() {
        return medidaAncho;
    }

    /**
     * @param medidaAncho the medidaAncho to set
     */
    public void setMedidaAncho(String medidaAncho) {
        this.medidaAncho = medidaAncho;
    }
}
