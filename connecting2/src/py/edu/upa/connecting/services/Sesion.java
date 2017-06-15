package py.edu.upa.connecting.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.edu.upa.connecting.entidades.Usuario;

@Path("/sesion")
public class Sesion {
	
	/**
	 * Datasource para obtener conexion a base de datos
	 */
	@Resource(lookup = "java:jboss/datasources/ConnectingDS")
	DataSource ds;
	
	//Almacena las sesiones activas
	static HashMap<String, String> sesiones = new HashMap<String,String>();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response iniciarSesion(py.edu.upa.connecting.entidades.Sesion sesion) {
		Map<String, String> responseObj = new HashMap<String, String>();
		
		System.out.println("Iniciando sesion para el usuario " + sesion.getUsuario());

		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("select * from usuario where email = ? and password = ?")
			 ) {
			
			ps.setString(1, sesion.getUsuario());
			ps.setString(2, sesion.getPassword());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {	
				String token =Math.random() + "";
				
				sesiones.put(token, sesion.getUsuario());
				
				System.out.println("Login exitoso");
				responseObj.put("mensaje","Sesion iniciada exitosamente");
				responseObj.put("token",token);
				return Response.ok(responseObj).build();				
			} else {
				System.out.println("Login fallido");
				responseObj.put("mensaje","El usuario o la contrasena no coinciden");
				return Response.status(Response.Status.FORBIDDEN).entity(responseObj).build();
			}
		
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();			
			responseObj.put("error","Ocurrio el siguiente error: " +  e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(responseObj).build();
		}

	}
	
	@GET
	@Path("/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validarSesion(@PathParam("token") String token) {
		if(sesiones.containsKey(token)) {
			py.edu.upa.connecting.entidades.Sesion sesion = new py.edu.upa.connecting.entidades.Sesion();
			sesion.setUsuario(sesiones.get(token));
			sesion.setPassword("******");
			return Response.status(Response.Status.OK).entity(sesion).build();
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
		
	
}
