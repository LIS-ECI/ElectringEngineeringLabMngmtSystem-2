
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.project.managedBeans;

import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.entities.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
//busqueda
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.RolUsuario;
import edu.eci.pdsw.services.Services;
import edu.eci.pdsw.services.ServicesException;

/**
 *
 * @author 2105534
 */
@ManagedBean(name="Equipos")
@SessionScoped
public class ServicioEquiposElectronicatobean implements Serializable{
    /**
     * 
     */
    public ServicioEquiposElectronicatobean() {
        
        this.services = Services.getInstance("applicationconfig.properties");
        
        //inical lista
        listaModelos=new ArrayList<>();
        Set<Modelo> conjunto=null;
        try {
            
            conjunto = services.loadModelos();
            
        } catch (ServicesException ex) {
            System.out.println(ex.getMessage());
            System.out.println("luego");
            System.out.println(ex.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","Sucedio algo inesperado en la carga de modelos"));
        }
        Modelo[] listaModelo=new Modelo[conjunto.size()];
        conjunto.toArray(listaModelo);
        listaModelos=Arrays.asList(listaModelo);
        //filtrada lista
        filteredListaModelos=new ArrayList<>();
        filteredListaModelos=Arrays.asList(listaModelo);
        listaEquipoBasico=new ArrayList<>();
        
        Set<EquipoBasico> conjunto1=null;
        try {
            conjunto1 = services.loadEquiposBasicos();
        } catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error","Sucedio algo inesperado en la carga de equipos basicos"));
        }
        EquipoBasico[] listaEquipoBasico2=new EquipoBasico[conjunto1.size()];
        conjunto1.toArray(listaEquipoBasico2);
        listaEquipoBasico=Arrays.asList(listaEquipoBasico2);
        //filtrada lista
        filteredListaEquipoBasico=new ArrayList<>();
        filteredListaEquipoBasico=Arrays.asList(listaEquipoBasico2);
        //estado y subestado de equipo
        estados  = new HashMap<>();
        estados.put("Desactivo", "Desactivo");
        estados.put("Activo", "Activo");
        Map<String,String> map;
        map = new HashMap<>();
        map.put("Préstamo diario", "Préstamo diario");
        map.put("Préstamo 24 horas", "Préstamo 24 horas");
        map.put("Préstamo por semestre", "Préstamo por semestre");
        map.put("Préstamo indefinido","Préstamo indefinido");
        map.put("Mantenimiento","Mantenimiento");
        map.put("En almacén","En almacén");
        data.put("Activo", map);
        map = new HashMap<>();
        
        map.put("Dado de baja", "Dado de baja");
        map.put("En reparación","En reparación");
        data.put("Desactivo", map);  
    }
    
    @ManagedProperty(value = "#{loginBean}")    
    private ShiroLoginBean loginBean;
    //pagina usuario
    private List<Modelo> listaModelos;
    private List<Modelo> filteredListaModelos = new ArrayList<>();
    private String id;
    private String nombre;
    private String correo;
    private String roles;
    private Services services;
    private String nombreDeModelo;
    private boolean elModeloYaExiste=false;
    private String textoSalidaModelo;
    private boolean yaBusqueModelo=false;
    private boolean elModeloNoExiste=false;
    //datos para un modelo nuevo
    private String claseModelo;
    private int vidaUtilEnHorasModelo;
    private int valorComercialModelo;
    private boolean estaAseguradoModelo;
    private String fotoModelo;
    private Modelo modeloSeleccionado;
    //datos para equipo nuevo
    private int serialEquipo;
    private String nombreEquipo;
    private int placaEquipo;
    private int vidaInicial;
    private String marcaEquipo;
    private String descripcionEquipo;
    private String estadoEquipo="Activo";
    private String subEstadoEquipo="En almacén";
    private String proveedorEquipo;
    private Map<String,String> estados;
    private Map<String,String> subestados;
    private final Map<String,Map<String,String>> data = new HashMap<>();
    //datos para equipo basico
    private List<EquipoBasico> listaEquipoBasico;
    private List<EquipoBasico> filteredListaEquipoBasico = new ArrayList<>();
    private String nombreEquipoBasico;
    private int valorEquipoBasico;
    private String fotoEquipoBasico;
    private String descripcionEquipoBasico;
    private int cantidadEquipoBasico;
    private EquipoBasico equipoBasicoSeleccionado;
    private int cantidadEquipoBasicoAactualizar;
   
    /**
     * deja todos los elementos que se usan en la vista en su estado original
     */
    public void limpiarPaginaRegistrarUnEquipo(){
        nombreDeModelo=null;
        elModeloYaExiste=false;
        textoSalidaModelo=null;
        yaBusqueModelo=false;
        vidaInicial=0;
        elModeloNoExiste=false;
        //datos para un modelo nuevo
        claseModelo=null;
        vidaUtilEnHorasModelo=0;
        valorComercialModelo=0;
        estaAseguradoModelo=false;
        fotoModelo=null;
        //datos para equipo nuevo
        serialEquipo=0;
        nombreEquipo=null;
        placaEquipo=0;
        vidaInicial=0;
        marcaEquipo=null;
        descripcionEquipo=null;
        estadoEquipo="Activo";
        subEstadoEquipo="En almacén";
        proveedorEquipo=null;
        nombreEquipoBasico=null;
    }
    
    /**
     * limpia el forumulario para registrar un nuevo modelo
     */
    public void limpiarRegistrarModelo(){
        nombreDeModelo="";
        claseModelo="";
        vidaUtilEnHorasModelo=0;
        valorComercialModelo=0;
        estaAseguradoModelo=false;
        fotoModelo="";
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioReigstrarModelo");
    }
    
    /**
     * limpia el forumulario para registrar una nueva herramienta
     */
    public void limpiarRegistrarherramienta(){
        nombreEquipoBasico="";
        valorEquipoBasico=0;
        descripcionEquipoBasico="";
        cantidadEquipoBasico=0;
        fotoEquipoBasico="";
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioCrearHerramienta");
    }
    
    
    
         
    /**
     * dependiendo el estado (activo-desactivo) se actualizan los diferentes sub estados relacionados a cada uno. Se hace para evitar cosas como que el usuario meta estao desactivo y un subestado en almacen (que no puede pasar)
     */
    public void onEstadoChange() {
        if(estadoEquipo != null && !estadoEquipo.equals(""))
            setSubestados(data.get(estadoEquipo));
        else
            setSubestados(new HashMap<String, String>());
    }
    
    /**
     * devulve una string para hacer la busqueda de una imagen que tenemos guardad en el proyecto
     * @return check, si el modelo esta asegurado, de lo contrario fail
     */
    public String demeIcono(){
        if(modeloSeleccionado.getSeguro()){
            return "check";
        }
        else{
            return  "fail";
        }
    }
    
    /**
     * actualiza un equipo basico con una nueva cantidad
     */
    public void accionBotonActualizarEquipoBasico(){
        try{ 
            if(cantidadEquipoBasicoAactualizar<0)throw new Exception("la cantidad no puede ser negativa");
            services.updateEquipoBasico(equipoBasicoSeleccionado, cantidadEquipoBasicoAactualizar);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Successful","Se ha actualizado el equipo basico con exito"));
            RequestContext context = RequestContext.getCurrentInstance();
            listaEquipoBasico=new ArrayList<>();
            Set<EquipoBasico> conjunto=services.loadEquiposBasicos();
            EquipoBasico[] listaEquipoBasico1=new EquipoBasico[conjunto.size()];
            conjunto.toArray(listaEquipoBasico1);
            listaEquipoBasico=Arrays.asList(listaEquipoBasico1);
            setFilteredListaEquipoBasico(listaEquipoBasico);
            filterListEquipoBasico();
            context.update("equiposbasicos");
            context.execute("PF('actualizarCantidadEquipoBasico').hide();");
        }catch(Exception e){
           FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",e.getMessage()));
       }
    }
    
    /**
     * crea y guarda el equipo basico en la bd con los datos suministrados en los campos de la vista
     */
    public void accionBotonCrearEquipoBasico(){
        try{
        EquipoBasico equipoNuevo=new EquipoBasico(nombreEquipoBasico, valorEquipoBasico, fotoEquipoBasico,descripcionEquipoBasico, cantidadEquipoBasico);
        services.registroEquipoBasicoNuevo(equipoNuevo);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Successful","Se ha registrado el equipo basico con exito"));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('crearEquipoBasico').hide();");
       }catch(Exception e){
           FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",e.getMessage()));
       }
    }
    
    /**
     * filtra o busca los modelos segun un campo que el usuario va ingresando (facilita busqueda)
     */
    public void filterList(){
        ArrayList<String> ListaNombres = new ArrayList<String>();
        for (int i = 0; i<listaModelos.size() ; i++) {
            ListaNombres.add(listaModelos.get(i).getNombre().toLowerCase());
        } 
        List<String> filteredListNames;
        filteredListNames = Lists.newArrayList(Collections2.filter(ListaNombres,Predicates.containsPattern(nombreDeModelo.toLowerCase())));
        List<Modelo> filteredList = new ArrayList<>();
        for (int i = 0; i < listaModelos.size(); i++) {
            if (filteredListNames.contains(listaModelos.get(i).getNombre().toLowerCase())){
                filteredList.add(listaModelos.get(i));
            }
        }
    setFilteredListaModelos(filteredList);
    }
    
    /**
     * filtra o busca los equipos basicos segun un campo que el usuario va ingresando (facilita busqueda)
     */
    public void filterListEquipoBasico(){
        ArrayList<String> ListaNombres = new ArrayList<String>();
        for (int i = 0; i<listaEquipoBasico.size() ; i++) {
            ListaNombres.add(listaEquipoBasico.get(i).getNombre().toLowerCase());
        }
        List<String> filteredListNames;
        filteredListNames = Lists.newArrayList(Collections2.filter(ListaNombres,Predicates.containsPattern(nombreEquipoBasico.toLowerCase())));
        
        List<EquipoBasico> filteredList = new ArrayList<>();
        for (int i = 0; i < listaEquipoBasico.size(); i++) {
            if (filteredListNames.contains(listaEquipoBasico.get(i).getNombre().toLowerCase())){
                filteredList.add(listaEquipoBasico.get(i));
            }
        }
     setFilteredListaEquipoBasico(filteredList);   
    }
    
    /**
     * crea y guarda el equipo en la bd con los datos suministrados en los campos de la vista
     */
    public void mensajeCreacionEquipoExitoso(){
        try{
        Equipo equipoNuevo=new Equipo(serialEquipo, nombreEquipo, placaEquipo, vidaInicial,marcaEquipo, descripcionEquipo, estadoEquipo, subEstadoEquipo,proveedorEquipo);
        services.registroEquipoNuevo(equipoNuevo,nombreDeModelo);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Successful","Se ha registrado el equipo con exito"));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('crearEquipo').hide();");
        }
        catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",e.getMessage()));
    
        }
        
    }
    
    /**
     * crea y guarda el modelo en la bd con los datos suministrados en los campos de la vista
     */
    public void mensajeCreacionModeloExitoso(){
       try{
        Modelo modeloNuevo=new Modelo(nombreDeModelo, claseModelo, vidaUtilEnHorasModelo, valorComercialModelo, estaAseguradoModelo, fotoModelo);
        services.registroModeloNuevo(modeloNuevo);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Successful","Se ha registrado el modelo con exito"));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('crearModelo').hide();");
       }catch(Exception e){
           FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",e.getMessage()));
       }
    }
    
    public void accionBotoncrearEquipo(String modelo){
        nombreDeModelo=modelo;
        serialEquipo=0;
        nombreEquipo="";
        placaEquipo=0;
        marcaEquipo="";
        descripcionEquipo="";
        proveedorEquipo="";
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form2:crearequipo");
    }
   
    /**
     * @return the id
     */
    public String getId() {
        id=getLoginBean().getUsername();
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        id=getLoginBean().getUsername();
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return "Carlos Andres Sanchez Venegas";
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Accede a la base de datos para traer el nombre de usuario
     * @return retorna el nombre de usuario que tiene el equipo en prestamo 
     */
    public String getNombreUsuario(){
        this.nombre = "            ";
        return nombre;
    }
   
    /**
     * obtiene los datos de los equipos que se van a prestar
     */
    public void getDatosEquiposPrestamos(){
        
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return "carlos.sanchez-v@mail.escuelaing.edu.co";
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }


    /**
     * @return the listaConsultas
     */
    public List<Modelo> getListaModelos() {
        try {
            if(listaModelos.size()!=services.loadModelos().size()){
                listaModelos=new ArrayList<>();
                Set<Modelo> conjunto=services.loadModelos();
                Modelo[] listaModelo=new Modelo[conjunto.size()];
                conjunto.toArray(listaModelo);
                listaModelos=Arrays.asList(listaModelo);
                setFilteredListaModelos(listaModelos);
            }
        } catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
        return listaModelos;
    }
    
    /**
     * @return the listaEquipoBasico
     */
    public List<EquipoBasico> getListaEquipoBasico() {
        try {
            if(listaEquipoBasico.size()!=services.loadEquiposBasicos().size()){
                listaEquipoBasico=new ArrayList<>();
                Set<EquipoBasico> conjunto = services.loadEquiposBasicos();
                EquipoBasico[] listaEquiposBasicos=new EquipoBasico[conjunto.size()];
                conjunto.toArray(listaEquiposBasicos);
                listaEquipoBasico=Arrays.asList();
                setFilteredListaEquipoBasico(listaEquipoBasico); 
            }
        } catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
        return listaEquipoBasico;
    }

    
    /**
     * @param listaConsultas the listaConsultas to set
     */
    public void setListaModelos(List<Modelo> listaConsultas) {
        this.listaModelos = listaConsultas;
    }

    /**
     * @return the textoSalidaModelo
     */
    public String getTextoSalidaModelo() {
        return textoSalidaModelo;
    }

    /**
     * @param textoSalidaModelo the textoSalidaModelo to set
     */
    public void setTextoSalidaModelo(String textoSalidaModelo) {
        this.textoSalidaModelo = textoSalidaModelo;
    }

    /**
     * @return the loginBean
     */
    public ShiroLoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(ShiroLoginBean loginBean) {
        this.loginBean = loginBean;
    }

    /**
     * @return the elModeloYaExiste
     */
    public boolean isElModeloYaExiste() {
        return elModeloYaExiste;
    }

    /**
     * @param elModeloYaExiste the elModeloYaExiste to set
     */
    public void setElModeloYaExiste(boolean elModeloYaExiste) {
        this.elModeloYaExiste = elModeloYaExiste;
    }

    /**
     * @return the yaBusqueModelo
     */
    public boolean isYaBusqueModelo() {
        return yaBusqueModelo;
    }


    /**
     * @param yaBusqueModelo the yaBusqueModelo to set
     */
    public void setYaBusqueModelo(boolean yaBusqueModelo) {
        this.yaBusqueModelo = yaBusqueModelo;
    }

    /**
     * @return the elModeloNoExiste
     */
    public boolean isElModeloNoExiste() {
        return elModeloNoExiste;
    }

    /**
     * @param elModeloNoExiste the elModeloNoExiste to set
     */
    public void setElModeloNoExiste(boolean elModeloNoExiste) {
        this.elModeloNoExiste = elModeloNoExiste;
    }

    /**
     * @return the nombreDeModelo
     */
    public String getNombreDeModelo() {
        return nombreDeModelo;
    }

    /**
     * @param nombreDeModelo the nombreDeModelo to set
     */
    public void setNombreDeModelo(String nombreDeModelo) {
        this.nombreDeModelo = nombreDeModelo;
    }

 
    /**
     * @return the claseModelo
     */
    public String getClaseModelo() {
        return claseModelo;
    }

    /**
     * @param claseModelo the claseModelo to set
     */
    public void setClaseModelo(String claseModelo) {
        this.claseModelo = claseModelo;
    }

    /**
     * @return the vidaUtilEnHorasModelo
     */
    public int getVidaUtilEnHorasModelo() {
        return vidaUtilEnHorasModelo;
    }

    /**
     * @param vidaUtilEnHorasModelo the vidaUtilEnHorasModelo to set
     */
    public void setVidaUtilEnHorasModelo(int vidaUtilEnHorasModelo) {
        this.vidaUtilEnHorasModelo = vidaUtilEnHorasModelo;
    }

    /**
     * @return the valorComercialModelo
     */
    public int getValorComercialModelo() {
        return valorComercialModelo;
    }

    /**
     * @param valorComercialModelo the valorComercialModelo to set
     */
    public void setValorComercialModelo(int valorComercialModelo) {
        this.valorComercialModelo = valorComercialModelo;
    }

    /**
     * @return the estaAseguradoModelo
     */
    public boolean isEstaAseguradoModelo() {
        return estaAseguradoModelo;
    }

    /**
     * @param estaAseguradoModelo the estaAseguradoModelo to set
     */
    public void setEstaAseguradoModelo(boolean estaAseguradoModelo) {
        this.estaAseguradoModelo = estaAseguradoModelo;
    }

    /**
     * @return the fotoModelo
     */
    public String getFotoModelo() {
        return fotoModelo;
    }

    /**
     * @param fotoModelo the fotoModelo to set
     */
    public void setFotoModelo(String fotoModelo) {
        this.fotoModelo = fotoModelo;
        
    }

    /**
     * @return the serialEquipo
     */
    public int getSerialEquipo() {
        return serialEquipo;
    }

    /**
     * @param serialEquipo the serialEquipo to set
     */
    public void setSerialEquipo(int serialEquipo) {
        this.serialEquipo = serialEquipo;
    }

    /**
     * @return the nombreEquipo
     */
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    /**
     * @param nombreEquipo the nombreEquipo to set
     */
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    /**
     * @return the placaEquipo
     */
    public int getPlacaEquipo() {
        return placaEquipo;
    }

    /**
     * @param placaEquipo the placaEquipo to set
     */
    public void setPlacaEquipo(int placaEquipo) {
        this.placaEquipo = placaEquipo;
    }

    /**
     * @return the marcaEquipo
     */
    public String getMarcaEquipo() {
        return marcaEquipo;
    }

    /**
     * @param marcaEquipo the marcaEquipo to set
     */
    public void setMarcaEquipo(String marcaEquipo) {
        this.marcaEquipo = marcaEquipo;
    }

    /**
     * @return the descripcionEquipo
     */
    public String getDescripcionEquipo() {
        return descripcionEquipo;
    }

    /**
     * @param descripcionEquipo the descripcionEquipo to set
     */
    public void setDescripcionEquipo(String descripcionEquipo) {
        this.descripcionEquipo = descripcionEquipo;
    }

    /**
     * @return the estadoEquipo
     */
    public String getEstadoEquipo() {
        return estadoEquipo;
    }

    /**
     * @param estadoEquipo the estadoEquipo to set
     */
    public void setEstadoEquipo(String estadoEquipo) {
        this.estadoEquipo = estadoEquipo;
    }

    /**
     * @return the subEstadoEquipo
     */
    public String getSubEstadoEquipo() {
        onEstadoChange();
        return subEstadoEquipo;
    }

    /**
     * @param subEstadoEquipo the subEstadoEquipo to set
     */
    public void setSubEstadoEquipo(String subEstadoEquipo) {
        this.subEstadoEquipo = subEstadoEquipo;
    }

    /**
     * @return the proveedorEquipo
     */
    public String getProveedorEquipo() {
        return proveedorEquipo;
    }

    /**
     * @param proveedorEquipo the proveedorEquipo to set
     */
    public void setProveedorEquipo(String proveedorEquipo) {
        this.proveedorEquipo = proveedorEquipo;
    }  
    
    /**
     * @return nombreEquipoBasico 
     */
    public String getnombreEquipoBásico(){
        return "  ";
    }
    
    /**
     * @param nombrembreEquipoBasico the nombreEquipoBásico to set
     */
    public void setnombreEquipoBásico(String nombre){
        this.nombreEquipoBasico = nombre;
    }

    /**
     * @return the estados
     */
    public Map<String,String> getEstados() {
        return estados;
    }

    /**
     * @param estados the estados to set
     */
    public void setEstados(Map<String,String> estados) {
        this.estados = estados;
    }

    /**
     * @return the subestados
     */
    public Map<String,String> getSubestados() {
        return subestados;
    }

    /**
     * @param subestados the subestados to set
     */
    public void setSubestados(Map<String,String> subestados) {
        this.subestados = subestados;
    }

    /**
     * @return the modeloSeleccionado
     */
    public Modelo getModeloSeleccionado() {
        
        return modeloSeleccionado;
    }

    /**
     * @param modeloSeleccionado the modeloSeleccionado to set
     */
    public void setModeloSeleccionado(Modelo modeloSeleccionado) {
        
        this.modeloSeleccionado = modeloSeleccionado;
    }

    /**
     * @return the filteredListaModelos
     */
    public List<Modelo> getFilteredListaModelos() {
        try {
            if(listaModelos!=null && listaModelos.size()!=services.loadModelos().size()){
                listaModelos=new ArrayList<>();
                Set<Modelo> conjunto=services.loadModelos();
                Modelo[] listaModelo=new Modelo[conjunto.size()];
                conjunto.toArray(listaModelo);
                listaModelos=Arrays.asList(listaModelo);
                setFilteredListaModelos(listaModelos);
                filterList();
            }
        } catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
        return filteredListaModelos;
    }
    
    /**
     * @return the filteredListaEquipoBasico
     */
    public List<EquipoBasico> getFilteredListaEquipoBasico() {
        try {
            if(listaEquipoBasico!=null && listaEquipoBasico.size()!=services.loadEquiposBasicos().size()){
                listaEquipoBasico=new ArrayList<>();
                Set<EquipoBasico> conjunto=services.loadEquiposBasicos();
                EquipoBasico[] listaEquipoBasico1=new EquipoBasico[conjunto.size()];
                conjunto.toArray(listaEquipoBasico1);
                listaEquipoBasico=Arrays.asList(listaEquipoBasico1);
                setFilteredListaEquipoBasico(listaEquipoBasico);
                filterListEquipoBasico();
                
            }
        } catch (ServicesException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error",ex.getMessage()));
        }
        return filteredListaEquipoBasico;
    }
    
    /**
     * @param filteredListaModelos the filteredListaModelos to set
     */
    public void setFilteredListaModelos(List<Modelo> filteredListaModelos) {
        this.filteredListaModelos = filteredListaModelos;
    }
    /**
     * @return the nombreEquipoBasico
     */
    public String getNombreEquipoBasico() {
        return nombreEquipoBasico;
    }

    /**
     * @param nombreEquipoBasico the nombreEquipoBasico to set
     */
    public void setNombreEquipoBasico(String nombreEquipoBasico) {
        this.nombreEquipoBasico = nombreEquipoBasico;
    }

    /**
     * @return the valorEquipoBasico
     */
    public int getValorEquipoBasico() {
        return valorEquipoBasico;
    }

    /**
     * @param valorEquipoBasico the valorEquipoBasico to set
     */
    public void setValorEquipoBasico(int valorEquipoBasico) {
        this.valorEquipoBasico = valorEquipoBasico;
    }

    /**
     * @return the fotoEquipoBasico
     */
    public String getFotoEquipoBasico() {
        return fotoEquipoBasico;
    }

    /**
     * @param fotoEquipoBasico the fotoEquipoBasico to set
     */
    public void setFotoEquipoBasico(String fotoEquipoBasico) {
        this.fotoEquipoBasico = fotoEquipoBasico;
    }

    /**
     * @return the descripcionEquipoBasico
     */
    public String getDescripcionEquipoBasico() {
        return descripcionEquipoBasico;
    }

    /**
     * @param descripcionEquipoBasico the descripcionEquipoBasico to set
     */
    public void setDescripcionEquipoBasico(String descripcionEquipoBasico) {
        this.descripcionEquipoBasico = descripcionEquipoBasico;
    }

    /**
     * @return the cantidadEquipoBasico
     */
    public int getCantidadEquipoBasico() {
        return cantidadEquipoBasico;
    }

    /**
     * @param cantidadEquipoBasico the cantidadEquipoBasico to set
     */
    public void setCantidadEquipoBasico(int cantidadEquipoBasico) {
        this.cantidadEquipoBasico = cantidadEquipoBasico;
    }
    
    /**
     * @param listaEquipoBasico the listaEquipoBasico to set
     */
    public void setListaEquipoBasico(List<EquipoBasico> listaEquipoBasico) {
        this.listaEquipoBasico = listaEquipoBasico;
    }

    /**
     * @param filteredListaEquipoBasico the filteredListaEquipoBasico to set
     */
    public void setFilteredListaEquipoBasico(List<EquipoBasico> filteredListaEquipoBasico) {
        this.filteredListaEquipoBasico = filteredListaEquipoBasico;
    }

    /**
     * @return the equipoBasicoSeleccionado
     */
    public EquipoBasico getEquipoBasicoSeleccionado() {
        return equipoBasicoSeleccionado;
    }

    /**
     * @param equipoBasicoSeleccionado the equipoBasicoSeleccionado to set
     */
    public void setEquipoBasicoSeleccionado(EquipoBasico equipoBasicoSeleccionado) {
        this.equipoBasicoSeleccionado = equipoBasicoSeleccionado;
    }

    /**
     * @return the cantidadEquipoBasicoAactualizar
     */
    public int getCantidadEquipoBasicoAactualizar() {
        return cantidadEquipoBasicoAactualizar;
    }

    /**
     * @param cantidadEquipoBasicoAactualizar the cantidadEquipoBasicoAactualizar to set
     */
    public void setCantidadEquipoBasicoAactualizar(int cantidadEquipoBasicoAactualizar) {
        this.cantidadEquipoBasicoAactualizar = cantidadEquipoBasicoAactualizar;
    }

    /**
     * @return the roles
     */
    public String getRoles() {
        roles= "Laboratorista";
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(String roles) {
        this.roles = roles;
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

