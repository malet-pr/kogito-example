package org.acme.kogitoexample.model;

import javax.persistence.*;

@Entity
@Table(name = "CT_ACTIVIDAD")
public class ActividadModel {
	@Id 
	@Column(name = "ID_ACTIVIDAD")
	@SequenceGenerator(name="seq_actividad", sequenceName="cts_actividad", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_actividad")
	private Long 	actividadId;

	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "ACTIVO")
	private Character activo;

	public Long getActividadId() {
		return actividadId;
	}

	public void setActividadId(Long actividadId) {
		this.actividadId = actividadId;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Character getActivo() {
		return activo;
	}

	public void setActivo(Character activo) {
		this.activo = activo;
	}
}

