package org.acme.kogitoexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CT_REGLA_TIPO")
public class ReglaTipo {

	@Id
	@Column(name = "ID_REGLA_TIPO", nullable = false)
	private Long reglaTipoid;

	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "NOMBRE_CORTO")
	private String nombreCorto;

	@Column(name = "HEADER")
	private String header;
	
	@Column(name = "VISIBLE")
	private Character visible;
	
	@Column(name = "AGRUPADOR")
	private String agrupador;
	
	@Column(name = "ORDEN")
	private Long orden;
	
	@Column(name = "HEADER_DEFAULT")
	private String headerDefault;

	public Long getReglaTipoid() {
		return reglaTipoid;
	}

	public void setReglaTipoid(Long reglaTipoid) {
		this.reglaTipoid = reglaTipoid;
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
		this.nombreCorto = nombreCorto;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Character getVisible() {
		return visible;
	}

	public void setVisible(Character visible) {
		this.visible = visible;
	}

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public String getHeaderDefault() {
		return headerDefault;
	}

	public void setHeaderDefault(String headerDefault) {
		this.headerDefault = headerDefault;
	}
}
