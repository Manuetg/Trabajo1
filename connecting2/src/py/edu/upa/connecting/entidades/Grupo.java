package py.edu.upa.connecting.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Grupo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Long codGrupo;
	String nombre;
	String objetivo;
	Integer latitud;
	Integer longitud;
	String codUsuarioCreacion;
	Date fechaUsuarioCreacion;
	
	public Long getCodGrupo() {
		return codGrupo;
	}
	public void setCodGrupo(Long codGrupo) {
		this.codGrupo = codGrupo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public Integer getLatitud() {
		return latitud;
	}
	public void setLatitud(Integer latitud) {
		this.latitud = latitud;
	}
	public Integer getLongitud() {
		return longitud;
	}
	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}
	public String getCodUsuarioCreacion() {
		return codUsuarioCreacion;
	}
	public void setCodUsuarioCreacion(String codUsuarioCreacion) {
		this.codUsuarioCreacion = codUsuarioCreacion;
	}
	public Date getFechaUsuarioCreacion() {
		return fechaUsuarioCreacion;
	}
	public void setFechaUsuarioCreacion(Date fechaUsuarioCreacion) {
		this.fechaUsuarioCreacion = fechaUsuarioCreacion;
	}
	
	
}
