package org.acme.kogitoexample.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CT_REGLA")
public class Regla {
	
	@Id
	@Column(name = "ID_REGLA", nullable = false)
	@SequenceGenerator(name = "seq_regla", sequenceName = "CTS_REGLA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_regla")
	private Long reglaId;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;

	@ManyToOne
	@JoinColumn(name = "ID_REGLA_TIPO")
	private ReglaTipo reglaTipo;
	
	@Column(name = "REGLA_JSON")
	private String reglaJson;

	@Column(name = "REGLA_DRL")
	private String reglaDRL;

	@Column(name = "FECHA_VIGENCIA_DESDE")
	private Date fechaVigenciaDesde;

	@Column(name = "FECHA_VIGENCIA_HASTA")
	private Date fechaVigenciaHasta;
	
	@Column(name = "ACTIVO")
	private Character activo;
	
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;


	public Long getReglaId() {
		return reglaId;
	}

	public void setReglaId(Long reglaId) {
		this.reglaId = reglaId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ReglaTipo getReglaTipo() {
		return reglaTipo;
	}

	public void setReglaTipo(ReglaTipo reglaTipo) {
		this.reglaTipo = reglaTipo;
	}

	public String getReglaJson() {
		return reglaJson;
	}

	public void setReglaJson(String reglaJson) {
		this.reglaJson = reglaJson;
	}

	public String getReglaDRL() {
		return reglaDRL;
	}

	public void setReglaDRL(String reglaDRL) {
		this.reglaDRL = reglaDRL;
	}

	public Date getFechaVigenciaDesde() {
		return fechaVigenciaDesde;
	}

	public void setFechaVigenciaDesde(Date fechaVigenciaDesde) {
		this.fechaVigenciaDesde = fechaVigenciaDesde;
	}

	public Date getFechaVigenciaHasta() {
		return fechaVigenciaHasta;
	}

	public void setFechaVigenciaHasta(Date fechaVigenciaHasta) {
		this.fechaVigenciaHasta = fechaVigenciaHasta;
	}

	public Character getActivo() {
		return activo;
	}

	public void setActivo(Character activo) {
		this.activo = activo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
