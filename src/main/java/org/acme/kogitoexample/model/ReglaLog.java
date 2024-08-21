package org.acme.kogitoexample.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CT_REGLA_LOG")
public class ReglaLog {

	@Id
	@Column(name = "ID_REGLA_LOG", nullable = false)
	@SequenceGenerator(name="seq_regla_log", sequenceName="CTS_REGLA_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_regla_log")
	private Long idReglaLog;
	
	@Column(name = "ID_CORRIDA", nullable = false)
	private String idCorrida;
	
	@Column(name = "REGLA_NOMBRE", nullable = false)
	private String reglaNombre;
	
	@Column(name = "NRO_OT", nullable = false)
	private String nroOT;
	
	@Column(name = "FECHA_INICIO", nullable = false)
	private Date fechaInicio;
	
	@Column(name = "FECHA_FIN", nullable = false)
	private Date fechaFin;
	
	@Column(name = "ACTIVIDAD_ACTIVA_INICIAL", nullable = true)
	private String actividadesActivasInicial;
	
	@Column(name = "ACTIVIDAD_DESACTIVADA_INICIAL", nullable = true)
	private String actividadesDesactivadasInicial;
	
	@Column(name = "ACTIVIDAD_ACTIVA_FINAL", nullable = true)
	private String actividadesActivasFinal;
	
	@Column(name = "ACTIVIDAD_DESACTIVADA_FINAL", nullable = true)
	private String actividadesDesactivadasFinal;
	
	@Column(name = "PAIS", nullable = true)
	private String pais;
	
	@Column(name = "TAREA_CODIGO", nullable = true)
	private String tareaJobTypeCodigo;
	
	@Column(name = "FECHA_CREACION_OT", nullable = false)
	private Date fechaCreacionOt;
	
	@Column(name = "FECHA_CIERRE_OT", nullable = false)
	private Date fechaCierreOt;


	public Long getIdReglaLog() {
		return idReglaLog;
	}

	public void setIdReglaLog(Long idReglaLog) {
		this.idReglaLog = idReglaLog;
	}

	public String getIdCorrida() {
		return idCorrida;
	}

	public void setIdCorrida(String idCorrida) {
		this.idCorrida = idCorrida;
	}

	public String getReglaNombre() {
		return reglaNombre;
	}

	public void setReglaNombre(String reglaNombre) {
		this.reglaNombre = reglaNombre;
	}

	public String getNroOT() {
		return nroOT;
	}

	public void setNroOT(String nroOT) {
		this.nroOT = nroOT;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getActividadesActivasInicial() {
		return actividadesActivasInicial;
	}

	public void setActividadesActivasInicial(String actividadesActivasInicial) {
		this.actividadesActivasInicial = actividadesActivasInicial;
	}

	public String getActividadesDesactivadasInicial() {
		return actividadesDesactivadasInicial;
	}

	public void setActividadesDesactivadasInicial(String actividadesDesactivadasInicial) {
		this.actividadesDesactivadasInicial = actividadesDesactivadasInicial;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTareaJobTypeCodigo() {
		return tareaJobTypeCodigo;
	}

	public void setTareaJobTypeCodigo(String tareaJobTypeCodigo) {
		this.tareaJobTypeCodigo = tareaJobTypeCodigo;
	}

	public Date getFechaCreacionOt() {
		return fechaCreacionOt;
	}

	public void setFechaCreacionOt(Date fechaCreacionOt) {
		this.fechaCreacionOt = fechaCreacionOt;
	}

	public Date getFechaCierreOt() {
		return fechaCierreOt;
	}

	public void setFechaCierreOt(Date fechaCierreOt) {
		this.fechaCierreOt = fechaCierreOt;
	}

	public String getActividadesActivasFinal() {
		return actividadesActivasFinal;
	}

	public void setActividadesActivasFinal(String actividadesActivasFinal) {
		this.actividadesActivasFinal = actividadesActivasFinal;
	}

	public String getActividadesDesactivadasFinal() {
		return actividadesDesactivadasFinal;
	}

	public void setActividadesDesactivadasFinal(String actividadesDesactivadasFinal) {
		this.actividadesDesactivadasFinal = actividadesDesactivadasFinal;
	}

}
