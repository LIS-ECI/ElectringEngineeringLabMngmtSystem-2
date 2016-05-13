 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.eci.pdsw.services.Services;
import edu.eci.pdsw.services.ServicesException;
import edu.eci.pdsw.entities.PrestamoBasicoUsuario;
import edu.eci.pdsw.entities.PrestamoUsuario;
import edu.eci.pdsw.entities.RolUsuario;
import edu.eci.pdsw.entities.Usuario;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author 2105461
 */
public class RegistroDevolucionTest {
    
    public RegistroDevolucionTest(){}
    
    
    @Before
    public void setUp() {
    }

    @After
    public void clearDB() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "anonymous", "");
        Statement stmt = conn.createStatement();
        stmt.execute("delete from ROLES_USUARIOS");
        stmt.execute("delete from ROLES");
        stmt.execute("delete from PRESTAMOS");
        stmt.execute("delete from EQUIPOS");
        stmt.execute("delete from MODELOS");
        stmt.execute("delete from PRESTAMOS_BASICOS");
        stmt.execute("delete from EQUIPOS_BASICOS");
        stmt.execute("delete from USUARIOS");
        conn.commit();
        conn.close();
    }
    
    /**
     * Obtiene una conexion a la base de datos de prueba
     * @return
     * @throws SQLException 
     */
    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "anonymous", "");
    }                                     
    
    //Prueba 1: un usuario sin prestamos ni de equipos normales ni basicos coon rol de estudiante
    @Test
    public void OneTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO ROLES(rol) values ('estudiante')");
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (124,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");        
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'estudiante')");        
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Usuario ans = new Usuario(124,"PEDRO PEREZ","pedro.perez@mail.escuelaing.edu.co","1test1");
        Set<RolUsuario> rol_ans=new LinkedHashSet<>();
        rol_ans.add(new RolUsuario("estudiante"));
        ans.setRoles(rol_ans);
        ////respuesta obtenida
        Usuario Jhordy = servicios.loadUsuarioById(124);
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un usuario que no posee equipos normales ni basicos, y que se identifica unicamente con rol de estudiante",ans.toString(),Jhordy.toString());       
    }

    //Prueba 2: un usuario con prestamos de equipos normales, pero sin prestamos de eqipos basicos con rol de estudiante y laboratorista
    @Test
    public void TwoTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO ROLES(rol) values ('estudiante')");
        stmt.execute("INSERT INTO ROLES(rol) values ('laboratorista')");
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (124,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");        
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'estudiante')");        
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'laboratorista')");
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (456,'MultiTest',789,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (567,'MultiTest',245,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        stmt.execute("INSERT INTO PRESTAMOS (USUARIOS_id,EQUIPOS_serial,fechaExpedicion,fechaVencimiento,tipoPrestamo) VALUES (124,567,'2015-01-01 00:00:00',null,'prestamo diario')");
        stmt.execute("INSERT INTO PRESTAMOS (USUARIOS_id,EQUIPOS_serial,fechaExpedicion,fechaVencimiento,tipoPrestamo) VALUES (124,456,'2015-01-01 00:00:00',null,'prestamo diario')");
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Usuario ans = new Usuario(124,"PEDRO PEREZ","pedro.perez@mail.escuelaing.edu.co","1test1");
        Set<RolUsuario> rol_ans=new LinkedHashSet<>();
        rol_ans.add(new RolUsuario("estudiante"));
        rol_ans.add(new RolUsuario("laboratorista"));
        ans.setRoles(rol_ans);
        Set<PrestamoUsuario> prestamo_ans=new LinkedHashSet<>();
        prestamo_ans.add(new PrestamoUsuario(456,java.sql.Date.valueOf("2015-01-01"),null,"prestamo diario"));
        prestamo_ans.add(new PrestamoUsuario(567,java.sql.Date.valueOf("2015-01-01"),null,"prestamo diario"));
        ans.setPrestamos(prestamo_ans);
        ////respuesta obtenida
        Usuario Jhordy = servicios.loadUsuarioById(124);
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un usuario que posee prestamos normales pero no pretamos basicos y que se identifica con roles de estudiante y laboratorista",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 3: un usuario sin prestamos de equipos normales, pero con prestamos de eqipos basicos con rol de laboratorista y administrador
    @Test
    public void ThreeTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO ROLES(rol) values ('laboratorista')");
        stmt.execute("INSERT INTO ROLES(rol) values ('administrador')");
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (124,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");              
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'laboratorista')");
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'administrador')");
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('cables','5000',null,'un metro de longitud y 0.5 centimetros de diametro',500)");      
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('antenas','3000',null,'un metro de long',300)");      
        stmt.execute("INSERT INTO PRESTAMOS_BASICOS (USUARIOS_id,EQUIPOS_BASICOS_nombre,fechaExpedicion,fechaVencimiento,tipoPrestamo,cantidadPrestada) VALUES (124,'cables','2015-01-01 00:00:00','2015-01-01 03:00:00','prestamo 24 horas',10)");
        stmt.execute("INSERT INTO PRESTAMOS_BASICOS (USUARIOS_id,EQUIPOS_BASICOS_nombre,fechaExpedicion,fechaVencimiento,tipoPrestamo,cantidadPrestada) VALUES (124,'antenas','2015-01-01 00:00:00','2015-01-01 03:00:00','prestamo 24 horas',10)");
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Usuario ans = new Usuario(124,"PEDRO PEREZ","pedro.perez@mail.escuelaing.edu.co","1test1");
        Set<RolUsuario> rol_ans=new LinkedHashSet<>();
        rol_ans.add(new RolUsuario("administrador"));
        rol_ans.add(new RolUsuario("laboratorista"));
        ans.setRoles(rol_ans);
        Set<PrestamoBasicoUsuario> prestamo_ans=new LinkedHashSet<>();
        prestamo_ans.add(new PrestamoBasicoUsuario("cables",java.sql.Date.valueOf("2015-01-01"),java.sql.Date.valueOf("2015-01-01"),"prestamo 24 horas",10));
        prestamo_ans.add(new PrestamoBasicoUsuario("antenas",java.sql.Date.valueOf("2015-01-01"),java.sql.Date.valueOf("2015-01-01"),"prestamo 24 horas",10));
        ans.setPrestamosBasicos(prestamo_ans);
        ////respuesta obtenida
        Usuario Jhordy = servicios.loadUsuarioById(124);
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un usuario que posee prestamos tanto basicos como normales y que se identifica con roles de laboratorista y administrador",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 4: un usuario con prestamos de equipos normales,con prestamos de eqipos basicos y con todos los roles
    @Test
    public void FourTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO ROLES(rol) values ('estudiante')");
        stmt.execute("INSERT INTO ROLES(rol) values ('profesor')");
        stmt.execute("INSERT INTO ROLES(rol) values ('laboratorista')");
        stmt.execute("INSERT INTO ROLES(rol) values ('administrador')");        
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (124,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");              
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'estudiante')");
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'profesor')");
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'laboratorista')");
        stmt.execute("INSERT INTO ROLES_USUARIOS(USUARIOS_id,ROLES_rol) values (124,'administrador')");
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (456,'MultiTest',789,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (567,'MultiTest',245,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('cables','5000',null,'un metro de longitud y 0.5 centimetros de diametro',500)");      
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('antenas','3000',null,'un metro de long',300)");      
        stmt.execute("INSERT INTO PRESTAMOS (USUARIOS_id,EQUIPOS_serial,fechaExpedicion,fechaVencimiento,tipoPrestamo) VALUES (124,567,'2015-01-01 00:00:00',null,'prestamo diario')");
        stmt.execute("INSERT INTO PRESTAMOS (USUARIOS_id,EQUIPOS_serial,fechaExpedicion,fechaVencimiento,tipoPrestamo) VALUES (124,456,'2015-01-01 00:00:00',null,'prestamo diario')");
        stmt.execute("INSERT INTO PRESTAMOS_BASICOS (USUARIOS_id,EQUIPOS_BASICOS_nombre,fechaExpedicion,fechaVencimiento,tipoPrestamo,cantidadPrestada) VALUES (124,'cables','2015-01-01 00:00:00','2015-01-01 03:00:00','prestamo 24 horas',10)");
        stmt.execute("INSERT INTO PRESTAMOS_BASICOS (USUARIOS_id,EQUIPOS_BASICOS_nombre,fechaExpedicion,fechaVencimiento,tipoPrestamo,cantidadPrestada) VALUES (124,'antenas','2015-01-01 00:00:00','2015-01-01 03:00:00','prestamo 24 horas',10)");
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Usuario ans = new Usuario(124,"PEDRO PEREZ","pedro.perez@mail.escuelaing.edu.co","1test1");
        Set<RolUsuario> rol_ans=new LinkedHashSet<>();
        rol_ans.add(new RolUsuario("administrador"));
        rol_ans.add(new RolUsuario("estudiante"));
        rol_ans.add(new RolUsuario("laboratorista"));
        rol_ans.add(new RolUsuario("profesor"));
        ans.setRoles(rol_ans);
        Set<PrestamoUsuario> prestamo_ans=new LinkedHashSet<>();
        prestamo_ans.add(new PrestamoUsuario(456,java.sql.Date.valueOf("2015-01-01"),null,"prestamo diario"));
        prestamo_ans.add(new PrestamoUsuario(567,java.sql.Date.valueOf("2015-01-01"),null,"prestamo diario"));
        ans.setPrestamos(prestamo_ans);
        Set<PrestamoBasicoUsuario> prestamob_ans=new LinkedHashSet<>();
        prestamob_ans.add(new PrestamoBasicoUsuario("cables",java.sql.Date.valueOf("2015-01-01"),java.sql.Date.valueOf("2015-01-01"),"prestamo 24 horas",10));
        prestamob_ans.add(new PrestamoBasicoUsuario("antenas",java.sql.Date.valueOf("2015-01-01"),java.sql.Date.valueOf("2015-01-01"),"prestamo 24 horas",10));
        ans.setPrestamosBasicos(prestamob_ans);
        ////respuesta obtenida
        Usuario Jhordy = servicios.loadUsuarioById(124);
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un usuario que tiene prestamos basicos, prestamos normales y se identifica con todos los roles",ans.toString(),Jhordy.toString());       
    }
    
}
