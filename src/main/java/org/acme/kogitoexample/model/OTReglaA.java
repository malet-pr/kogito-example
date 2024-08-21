package org.acme.kogitoexample.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "CTV_OT_REGLA_A")
public class OTReglaA {

	@Id
	@Column(name = "ID", nullable=false)
	private Long id;

	@Column(name = "NRO_OT", nullable=false)
	private String numeroOt;
	
	@Column(name = "FECHA_CREACION_OT", nullable=false)
	private Date fechaCreacionOT;

	@Column(name = "FECHA_CIERRE_OT", nullable=false)
	private Date fechaCierreOT;

	@Column(name = "REGLA_FLUJO", nullable=false)
	private String reglaFlujo;

	@Column(name = "TAREA_CODIGO", nullable=false)
	private String tareaCodigo;
	
	@Column(name = "TAREA_NOMBRE", nullable=false)
	private String tareaNombre;
	
	@Column(name = "PAIS", nullable=false)
	private String domiPais;
	
	@Column(name = "DOMI_DIRECCION", nullable=false)
	private String domicilioDireccion;
	
	@Column(name = "DOMI_DESC_LOCALIDAD", nullable=false)
	private String domicilioLocalidad;

	@Column(name = "ID_CLIENTE", nullable=false)
	private String clienteId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ORDEN_TRABAJO")
	private OrdenTrabajoModel ordenTrabajo;
	
	@Column(name = "CODIGO_ACTIVIDAD", nullable=false)
	private String codigoActividades;

	@Column(name = "ID_CLIENTE")
	private String idCliente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getCodigoActividades() {
		return codigoActividades;
	}

	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividades = codigoActividad;
	}

	public OrdenTrabajoModel getOrdenTrabajo() {
		return ordenTrabajo;
	}

	public void setOrdenTrabajo(OrdenTrabajoModel ordenTrabajo) {
		this.ordenTrabajo = ordenTrabajo;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getDomicilioLocalidad() {
		return domicilioLocalidad;
	}

	public void setDomicilioLocalidad(String domicilioLocalidad) {
		this.domicilioLocalidad = domicilioLocalidad;
	}

	public String getDomicilioDireccion() {
		return domicilioDireccion;
	}

	public void setDomicilioDireccion(String domicilioDireccion) {
		this.domicilioDireccion = domicilioDireccion;
	}

	public String getDomiPais() {
		return domiPais;
	}

	public void setDomiPais(String domiPais) {
		this.domiPais = domiPais;
	}

	public String getTareaNombre() {
		return tareaNombre;
	}

	public void setTareaNombre(String tareaNombre) {
		this.tareaNombre = tareaNombre;
	}

	public String getTareaCodigo() {
		return tareaCodigo;
	}

	public void setTareaCodigo(String tareaCodigo) {
		this.tareaCodigo = tareaCodigo;
	}

	public String getReglaFlujo() {
		return reglaFlujo;
	}

	public void setReglaFlujo(String reglaFlujo) {
		this.reglaFlujo = reglaFlujo;
	}

	public Date getFechaCierreOT() {
		return fechaCierreOT;
	}

	public void setFechaCierreOT(Date fechaCierreOT) {
		this.fechaCierreOT = fechaCierreOT;
	}

	public Date getFechaCreacionOT() {
		return fechaCreacionOT;
	}

	public void setFechaCreacionOT(Date fechaCreacionOT) {
		this.fechaCreacionOT = fechaCreacionOT;
	}

	public String getNumeroOt() {
		return numeroOt;
	}

	public void setNumeroOt(String numeroOt) {
		this.numeroOt = numeroOt;
	}
}
