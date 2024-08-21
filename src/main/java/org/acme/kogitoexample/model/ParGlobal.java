package org.acme.kogitoexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CT_PAR_GLOBAL")
public class ParGlobal {
	
	@Id 
	@Column(name = "ID_PAR_GLOBAL", nullable=false)
	public Long id;
	
	@Column(name = "NOMBRE")
	public String nombre;
	
	@Column(name = "VALOR")
	public String valor;
	
	@Column(name = "DESCRIPCION")
	public String descripcion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
