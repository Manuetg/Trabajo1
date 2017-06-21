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
	Date fechaCreacion;
	
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
		if(latitud==null){return 0;}
		return latitud;
	}
	public void setLatitud(Integer latitud) {
		this.latitud = latitud;
	}
	public Integer getLongitud() {
		if(longitud==null){return 0;}
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
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	
}
