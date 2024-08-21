package org.acme.kogitoexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CT_ACTIVIDAD_TIPO")
public class ActividadTipo {

	@Id
	@Column(name = "ID_ACTIVIDAD_TIPO", nullable = false)	
	private Long idActividad;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "NOMBRE_CORTO")
	private String nombreCorto;


	public Long getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(Long idActividad) {
		this.idActividad = idActividad;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
