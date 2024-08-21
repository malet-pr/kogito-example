package org.acme.kogitoexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CT_REGLA_FLUJO")
public class ReglaFlujo {

	@Id
	@Column(name = "ID_REGLA_FLUJO", nullable = false)
	private Long reglaFlujoId;

	@Column(name = "Nombre")
	private String nombre;
	
	@Column(name = "Nombre_Corto")
	private String nombreCorto;

	public Long getReglaFlujoId() {
		return reglaFlujoId;
	}

	public void setReglaFlujoId(Long reglaFlujoId) {
		this.reglaFlujoId = reglaFlujoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombre = nombreCorto;
	}
}
