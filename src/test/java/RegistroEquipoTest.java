
import edu.eci.pdsw.entities.Equipo;
import edu.eci.pdsw.entities.EquipoBasico;
import edu.eci.pdsw.entities.Modelo;
import edu.eci.pdsw.entities.PrestamoBasicoEquipo;
import edu.eci.pdsw.entities.PrestamoEquipo;
import edu.eci.pdsw.services.Services;
import edu.eci.pdsw.services.ServicesException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 2107262
 */
public class RegistroEquipoTest {
    public RegistroEquipoTest(){}
    
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
    //Prueba 1: cargar un modelo por nombre que no tiene equipos asociados
    @Test
    public void OneTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Modelo ans = new Modelo("modelo1","abcd",100,200000,true,null);
        Set<Equipo> equipo_ans=new LinkedHashSet<>();
        ans.setEquipos(equipo_ans);        
        ////respuesta obtenida
        Modelo Jhordy = servicios.loadModeloByName("modelo1");
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un modelo que aun no posee equipos asociados",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 2: cargar un modelo por nombre que contiene equipos asociados
    @Test
    public void TwoTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (456,'MultiTest',789,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (567,'MultiTest',245,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");        
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Modelo ans = new Modelo("modelo1","abcd",100,200000,true,null);
        Set<Equipo> equipo_ans=new LinkedHashSet<>();
        equipo_ans.add(new Equipo(456,"MultiTest",789,0,"La Ultima","tamano y altura promedio con buena calidad","activo","prestamo diario","Jhordy Salinas"));
        equipo_ans.add(new Equipo(567,"MultiTest",245,0,"La Ultima","tamano y altura promedio con buena calidad","activo","prestamo diario","Jhordy Salinas"));
        ans.setEquipos(equipo_ans);        
        ////respuesta obtenida
        Modelo Jhordy = servicios.loadModeloByName("modelo1");
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un modelo que posee equipos asociados",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 3: cargar un equipo sin prestamos asociados
    @Test
    public void ThreeTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (456,'MultiTest',789,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Equipo ans = new Equipo(456,"MultiTest",789,0,"La Ultima","tamano y altura promedio con buena calidad","activo","prestamo diario","Jhordy Salinas");
        ////respuesta obtenida
        Equipo Jhordy = servicios.loadEquipoBySerial(456);
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un equipo sin prestamos asociados",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 4: cargar un equipo con prestamos asociados
    @Test
    public void FourTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO MODELOS (nombre,clase,vidaUtil,valor,seguro,foto) values ('modelo1','abcd',100,200000,true,null)");              
        stmt.execute("INSERT INTO EQUIPOS (serial,nombre,placa,vidaInicial,marca,descripcion,estado,subestados,proveedor,Modelos_nombre) VALUES (456,'MultiTest',789,0,'La Ultima','tamano y altura promedio con buena calidad','activo','prestamo diario','Jhordy Salinas','modelo1')");
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (123,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (124,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");              
        stmt.execute("INSERT INTO PRESTAMOS (USUARIOS_id,EQUIPOS_serial,fechaExpedicion,fechaVencimiento,tipoPrestamo) VALUES (124,456,'2015-01-01 00:00:00',null,'prestamo semestral')");
        stmt.execute("INSERT INTO PRESTAMOS (USUARIOS_id,EQUIPOS_serial,fechaExpedicion,fechaVencimiento,tipoPrestamo) VALUES (123,456,'2015-01-01 00:00:00',null,'prestamo diario')");
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        Equipo ans = new Equipo(456,"MultiTest",789,0,"La Ultima","tamano y altura promedio con buena calidad","activo","prestamo diario","Jhordy Salinas");
        Set<PrestamoEquipo> prestamo_ans=new LinkedHashSet<>();
        prestamo_ans.add(new PrestamoEquipo(124,java.sql.Date.valueOf("2015-01-01"),null,"prestamo semestral"));
        prestamo_ans.add(new PrestamoEquipo(123,java.sql.Date.valueOf("2015-01-01"),null,"prestamo diario"));
        ans.setPrestamos(prestamo_ans);
        ////respuesta obtenida
        Equipo Jhordy = servicios.loadEquipoBySerial(456);
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un equipo con prestamos asociados",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 5: cargar un equipo basico sin prestamos asociados
    @Test
    public void FiveTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('cables',5000,null,'un metro de longitud y 0.5 centimetros de diametro',500)");      
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        EquipoBasico ans = new EquipoBasico("cables",5000,null,"un metro de longitud y 0.5 centimetros de diametro", 500);
        ////respuesta obtenida
        EquipoBasico Jhordy = servicios.loadEquipoBasicoByName("cables");
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un equipo basico sin prestamos asociados",ans.toString(),Jhordy.toString());       
    }
    
    //Prueba 6: cargar un equipo basico con prestamos asociados
    @Test
    public void SixTest() throws SQLException, ServicesException{
        //Insercion en BD
        Connection conn=getConnection();
        Statement stmt=conn.createStatement();       
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('cables',5000,null,'un metro de longitud y 0.5 centimetros de diametro',500)");      
        stmt.execute("INSERT INTO EQUIPOS_BASICOS (nombre,valor,foto,descripcion,cantidad) VALUES ('antenas','3000',null,'un metro de long',300)");      
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (123,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");
        stmt.execute("INSERT INTO USUARIOS (id,nombre,correo,contrasena) VALUES (124,'PEDRO PEREZ','pedro.perez@mail.escuelaing.edu.co','1test1')");              
        stmt.execute("INSERT INTO PRESTAMOS_BASICOS (USUARIOS_id,EQUIPOS_BASICOS_nombre,fechaExpedicion,fechaVencimiento,tipoPrestamo,cantidadPrestada) VALUES (123,'cables','2015-01-01 00:00:00','2015-01-01 03:00:00','prestamo 24 horas',10)");
        stmt.execute("INSERT INTO PRESTAMOS_BASICOS (USUARIOS_id,EQUIPOS_BASICOS_nombre,fechaExpedicion,fechaVencimiento,tipoPrestamo,cantidadPrestada) VALUES (124,'cables','2015-01-01 00:00:00','2015-01-01 03:00:00','prestamo 24 horas',10)");
        conn.commit();
        conn.close();
        //Realizar la operacion de la logica
        Services servicios = Services.getInstance("h2-applicationconfig.properties");
        ////respuesta esperada
        EquipoBasico ans = new EquipoBasico("cables",5000,null,"un metro de longitud y 0.5 centimetros de diametro", 500);
        Set<PrestamoBasicoEquipo> prestamo_ans=new LinkedHashSet<>();
        prestamo_ans.add(new PrestamoBasicoEquipo(123,java.sql.Date.valueOf("2015-01-01"),java.sql.Date.valueOf("2015-01-01"),"prestamo 24 horas",10));
        prestamo_ans.add(new PrestamoBasicoEquipo(124,java.sql.Date.valueOf("2015-01-01"),java.sql.Date.valueOf("2015-01-01"),"prestamo 24 horas",10));
        ans.setPrestamosBasicos(prestamo_ans);
        ////respuesta obtenida
        EquipoBasico Jhordy = servicios.loadEquipoBasicoByName("cables");
        //prueba
        assertEquals("No se obtuvo el resultado correcto al intentar cargar un equipo basico con prestamos asociados",ans.toString(),Jhordy.toString());       
    }
}
