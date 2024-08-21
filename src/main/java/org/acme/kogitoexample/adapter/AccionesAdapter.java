package org.acme.kogitoexample.adapter;

import java.util.Date;
import java.util.List;

public class AccionesAdapter {
	
	private String accion;
	private List<OTReglaAdapter> otsMatch;		// nroOT
	private String actividadNueva;
	private String actividadVieja;
	private int cantidad;
	private String regla;
	private Date fechaCreacion;


	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getActividadNueva() {
		return actividadNueva;
	}
	public void setActividadNueva(String actividadNueva) {
		this.actividadNueva = actividadNueva;
	}
	public String getActividadVieja() {
		return actividadVieja;
	}
	public void setActividadVieja(String actividadVieja) {
		this.actividadVieja = actividadVieja;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getRegla() {
		return regla;
	}
	public void setRegla(String regla) {
		this.regla = regla;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public List<OTReglaAdapter> getOtsMatch() {
		return otsMatch;
	}
	public void setOtsMatch(List<OTReglaAdapter> otsMatch) {
		this.otsMatch = otsMatch;
	}

}
