package py.edu.upa.connecting.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.security.auth.callback.DatabaseCallbackHandler;

import py.edu.upa.connecting.entidades.Grupo;
import py.edu.upa.connecting.entidades.Usuario;

@Path("/grupos")
@RequestScoped
public class GruposCopy {

	
	/**
	 * Datasource para obtener conexion a base de datos
	 * @param member
	 * @return
	 */
	@Resource(lookup = "java:jboss/datasources/ConnectingDS")
	DataSource ds;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Grupo> obtenerUbicacion(@QueryParam("latitud") Integer latitud,
									   @QueryParam("longitud") Integer longitud) {
		
		String sql = "select * from GRUPO where 1 = 1 ";
		
		if (latitud != null)
			sql += "and latitud = " + latitud;
		
		if (longitud != null)
			sql += "and longitud = " + longitud;
		
		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement(sql)
			 ) {
			
			ResultSet rs = ps.executeQuery();
			return cargarGrupos(rs);
			
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/{id}")//probandoCOmentario
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerGrupo(@PathParam("id") String codGrupo) {
		
		System.out.println("Se va a buscar al grupo  : [" + codGrupo + "]");

		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("select * from GRUPO  "
			 										   + "where cod_grupo = ?")
			 ) {
						
			ps.setLong(1, Long.parseLong(codGrupo));
			
			ResultSet rs = ps.executeQuery();
			ArrayList<Grupo> listaGrupo = cargarGrupos(rs);
			
			if (listaGrupo.size() == 0)
				return Response.status(Response.Status.NOT_FOUND).build();
			else
				return Response.ok(listaGrupo.get(0)).build();
		
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearGrupo(Grupo grupo) {

		Response.ResponseBuilder builder = null;

		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("insert into GRUPO (cod_grupo,nombre,objetivo,longitud,latitud,cod_usuario_creacion,fecha_creacion) "
			 										   + "values (?,?,?,?,?,?,?)")
			 ) {
			
			ps.setLong(1, grupo.getCodGrupo());
			ps.setString(2, grupo.getNombre());
			ps.setString(3, grupo.getObjetivo());
			ps.setInt(4, grupo.getLongitud());
			ps.setInt(5, grupo.getLatitud());
			ps.setString(6, grupo.getCodUsuarioCreacion());
			ps.setDate(7, new Date(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
			builder = Response.ok();
		
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error","Ocurrio el siguiente error: " +  e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}
	public static ArrayList<Grupo> cargarGrupos(ResultSet rs) throws Exception {
		ArrayList<Grupo> listaGrupos = new ArrayList<Grupo>();
		
		while(rs.next()) {
			Grupo grupoActual = new Grupo();
		
			grupoActual.setCodGrupo(rs.getLong("cod_grupo"));
			grupoActual.setNombre(rs.getString("nombre"));
			grupoActual.setObjetivo(rs.getString("objetivo"));
			grupoActual.setLongitud(rs.getInt("longitud"));
			grupoActual.setLatitud(rs.getInt("latitud"));
			grupoActual.setCodUsuarioCreacion(rs.getString("cod_usuario_creacion"));
			grupoActual.setFechaCreacion(rs.getDate("fecha_creacion")); 
			
			listaGrupos.add(grupoActual);
		}
		return listaGrupos;
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modificarGrupo (Grupo grupo) {
		Response.ResponseBuilder builder = null;

		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("update GRUPO set  nombre = ?,objetivo = ?,latitud=?,longitud=? ,cod_usuario_creacion = ?, fecha_creacion = ?"
			 										   + "where cod_grupo = ?")
			 ) {
			ps.setString(1, grupo.getNombre());
			ps.setString(2, grupo.getObjetivo());
			ps.setInt(3, grupo.getLatitud());
			ps.setInt(4, grupo.getLongitud());
			ps.setString(5, grupo.getCodUsuarioCreacion());
			ps.setDate(6, new Date(System.currentTimeMillis()));
			ps.setLong(7, grupo.getCodGrupo());

			ps.executeUpdate();
			
			builder = Response.ok();
		
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error","Ocurrio el siguiente error: " +  e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminarGrupo(@PathParam("id") Long codGrupo) {

		Response.ResponseBuilder builder = null;

		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("delete from GRUPO  "
			 										   + "where cod_grupo = ?")
			 ) {
						
			ps.setLong(1, codGrupo);
			
			ps.executeUpdate();
			
			builder = Response.ok();
		
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error","Ocurrio el siguiente error: " +  e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}
	@GET
	@Path("/{id}/integrantes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> obtenerUsuarios(@PathParam("id") Long codGrupo) {
		Usuarios usuarios = new Usuarios();
		
		try (Connection con = ds.getConnection();
			 PreparedStatement ps = con.prepareStatement("select usu.nombre, usu.telefono , usu.email "
			 		+ "from integrantes_grupo as inte "
			 		+ "inner join usuario as usu "
			 		+ "on inte.cod_usuario = usu.cod_usuario;"
			 	    + "where cod_grupo = ?")
			 ) {
			
			ps.setLong(1, codGrupo);

			
			ResultSet rs = ps.executeQuery();
			return usuarios.cargarUsuarios(rs);			
		} catch (Exception e) {
			// Handle generic exceptions.
			e.printStackTrace();
			return null;
		}}
		@DELETE
		@Path("/{id}/integrantes/{id2}")
		@Produces(MediaType.APPLICATION_JSON)
	public Response abandonarGrupo(@PathParam("id") Long codGrupo,@PathParam("id2") String token) {

			Response.ResponseBuilder builder = null;

			try (Connection con = ds.getConnection();
				 PreparedStatement ps = con.prepareStatement("delete from integrantes_grupo  "
				 										   + "where cod_grupo = ? and cod_usuario = ? ")
				 ) {
							
				ps.setLong(1, codGrupo);
				ps.setString(2, Sesion.metodoSesion(token));
				
				ps.executeUpdate();
				
				builder = Response.ok();
			
			} catch (Exception e) {
				// Handle generic exceptions.
				e.printStackTrace();
				Map<String, String> responseObj = new HashMap<String, String>();
				responseObj.put("error","Ocurrio el siguiente error: " +  e.getMessage());
				builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			}

			return builder.build();
	}
		@POST
		@Path("/{id}/integrantes/{id2}")
		@Produces(MediaType.APPLICATION_JSON)
	public Response unirGrupo(@PathParam("id") Long codGrupo,@PathParam("id2") String token) {
			Response.ResponseBuilder builder = null;
	

			
			try (Connection con = ds.getConnection();
				 PreparedStatement ps = con.prepareStatement("insert into integrantes_grupo (cod_grupo,cod_usuario) values (?,?) ")
				 ) {
							
				ps.setLong(1, codGrupo);
				ps.setString(2, Sesion.metodoSesion(token));
				
				ps.executeUpdate();
				
				builder = Response.ok();
			
			} catch (Exception e) {
				// Handle generic exceptions.
				e.printStackTrace();
				Map<String, String> responseObj = new HashMap<String, String>();
				responseObj.put("error","Ocurrio el siguiente error: " +  e.getMessage());
				builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			}

			return builder.build();
	}
}
