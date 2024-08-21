package org.acme.kogitoexample.adapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.acme.kogitoexample.model.OrdenTrabajoActividadModel;
import org.acme.kogitoexample.model.OrdenTrabajoModel;


public class OTReglaAdapter {

	private Long id;
	private String numeroOt;
	private LocalDateTime fechaHoraCreacionOT;
	private LocalDateTime fechaHoraCierreOT;
	private LocalDate fechaCreacionOT;
	private LocalDate fechaCierreOT;
	private int fechaCierreOTYear;
	private int fechaCierreOTDay;
	private String reglaFlujo;
	private String tareaCodigo;
	private String tareaNombre;
	private String domiDireccion;
	private String domiLocalidad;
	private String domiPais;
	private Long idOt;
	private List<String> actividades; // Contiene codigos de actividades de esta OT
	private String clienteId;
	private OrdenTrabajoModel ot;
	
	public OTReglaAdapter() {}

	public OrdenTrabajoActividadModel getActividadOT (String actividadCodigo) {
		OrdenTrabajoActividadModel actividadOT = this.ot.getActividades().stream().filter(x -> x.getActividadModel().getCodigo().equals("actividad")).findFirst().get();		
		return actividadOT;		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroOt() {
		return numeroOt;
	}

	public void setNumeroOt(String numeroOt) {
		this.numeroOt = numeroOt;
	}

	public LocalDateTime getFechaHoraCreacionOT() {
		return fechaHoraCreacionOT;
	}

	public void setFechaHoraCreacionOT(LocalDateTime fechaHoraCreacionOT) {
		this.fechaHoraCreacionOT = fechaHoraCreacionOT;
	}

	public LocalDateTime getFechaHoraCierreOT() {
		return fechaHoraCierreOT;
	}

	public void setFechaHoraCierreOT(LocalDateTime fechaHoraCierreOT) {
		this.fechaHoraCierreOT = fechaHoraCierreOT;
	}

	public LocalDate getFechaCreacionOT() {
		return fechaCreacionOT;
	}

	public void setFechaCreacionOT(LocalDate fechaCreacionOT) {
		this.fechaCreacionOT = fechaCreacionOT;
	}

	public LocalDate getFechaCierreOT() {
		return fechaCierreOT;
	}

	public void setFechaCierreOT(LocalDate fechaCierreOT) {
		this.fechaCierreOT = fechaCierreOT;
	}

	public int getFechaCierreOTYear() {
		return fechaCierreOTYear;
	}

	public void setFechaCierreOTYear(int fechaCierreOTYear) {
		this.fechaCierreOTYear = fechaCierreOTYear;
	}

	public int getFechaCierreOTDay() {
		return fechaCierreOTDay;
	}

	public void setFechaCierreOTDay(int fechaCierreOTDay) {
		this.fechaCierreOTDay = fechaCierreOTDay;
	}

	public String getReglaFlujo() {
		return reglaFlujo;
	}

	public void setReglaFlujo(String reglaFlujo) {
		this.reglaFlujo = reglaFlujo;
	}

	public String getTareaCodigo() {
		return tareaCodigo;
	}

	public void setTareaCodigo(String tareaCodigo) {
		this.tareaCodigo = tareaCodigo;
	}

	public String getTareaNombre() {
		return tareaNombre;
	}

	public void setTareaNombre(String tareaNombre) {
		this.tareaNombre = tareaNombre;
	}

	public String getDomiDireccion() {
		return domiDireccion;
	}

	public void setDomiDireccion(String domiDireccion) {
		this.domiDireccion = domiDireccion;
	}

	public String getDomiLocalidad() {
		return domiLocalidad;
	}

	public void setDomiLocalidad(String domiLocalidad) {
		this.domiLocalidad = domiLocalidad;
	}

	public String getDomiPais() {
		return domiPais;
	}

	public void setDomiPais(String domiPais) {
		this.domiPais = domiPais;
	}

	public Long getIdOt() {
		return idOt;
	}

	public void setIdOt(Long idOt) {
		this.idOt = idOt;
	}

	public List<String> getActividades() {
		return actividades;
	}

	public void setActividades(List<String> actividades) {
		this.actividades = actividades;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public OrdenTrabajoModel getOt() {
		return ot;
	}

	public void setOt(OrdenTrabajoModel ot) {
		this.ot = ot;
	}
}
