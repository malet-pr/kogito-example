package org.acme.kogitoexample.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.acme.drools.service.DroolsCondicionesService;
import org.acme.kogitoexample.model.ReglaTipo;
import org.acme.kogitoexample.repository.ActividadDAO;

import javax.inject.Inject;

public class ReglasTipoAAdapter {

	@Inject
	ActividadDAO actividadDAO;

	@Inject
	DroolsCondicionesService droolsCondicionesService;

	private ReglaTipo reglaTipo;
	private OTReglaAdapter otActual;
	private List<AccionesAdapter> acciones;

	public ReglasTipoAAdapter(ReglaTipo reglaTipo) {
		this.reglaTipo = reglaTipo;
		this.acciones = new ArrayList<AccionesAdapter>();
		this.otActual = new OTReglaAdapter();
	}

	public ReglaTipo getReglaTipo() {
		return reglaTipo;
	}
	public void setReglaTipo(ReglaTipo reglaTipo) {
		this.reglaTipo = reglaTipo;
	}
	public OTReglaAdapter getOtEvaluada() {
		return otActual;
	}
	public void setOtEvaluada(OTReglaAdapter otEvaluada) {
		this.otActual = otEvaluada;
	}
	public List<AccionesAdapter> getAcciones() {
		return acciones;
	}
	public void setAcciones(List<AccionesAdapter> acciones) {
		this.acciones = acciones;
	}

	// Acciones soportadas:
	public void agregarActividad(String actividad, String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("agregarActividad");
		accion.setActividadNueva(actividad);
		accion.setRegla(reglaNombre);
		accion.setCantidad(1);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);

	}
	public void agregarActividades(String actividad, int cantidad, String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("agregarActividad");
		accion.setActividadNueva(actividad);
		accion.setRegla(reglaNombre);
		accion.setCantidad(cantidad);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}
	public void desactivarActividades(String actividad, String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("desactivarActividades");
		accion.setActividadVieja(actividad);
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}

	public void desactivarActividadesTodas(String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("desactivarActividadesTodas");
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}
	public void desactivarActividadesTodasMenos(String actividad, String reglaNombre) {
		// Desactiva todas las actividades menos las que tengan el codigo especificado
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("desactivarActividadesTodasMenos");
		accion.setActividadVieja(actividad);
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}
	public void desactivarActividadesMenosUna(String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("desactivarActividadesMenosUna");
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}
	public void desactivarActividadesMenosUna(String actividadVieja, String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("desactivarActividadesMenosUna");
		accion.setActividadVieja(actividadVieja);
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}
	public void reemplazarActividades(String actividadVieja, String actividadNueva, String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("reemplazarActividades");
		accion.setActividadVieja(actividadVieja);
		accion.setActividadNueva(actividadNueva);
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}

	public void reemplazarActividadesXCantidad(String actividadVieja, String actividadNueva, int cantidad,
			String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("reemplazarActividades");
		accion.setActividadVieja(actividadVieja);
		accion.setActividadNueva(actividadNueva);
		accion.setCantidad(cantidad);
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}

	public void reemplazarActividadesMenosUna(String actividadVieja, String actividadNueva, String reglaNombre) {
		AccionesAdapter accion = new AccionesAdapter();
		accion.setAccion("reemplazarActividadesMenosUna");
		accion.setActividadVieja(actividadVieja);
		accion.setActividadNueva(actividadNueva);
		accion.setRegla(reglaNombre);
		accion.setOtsMatch(Arrays.asList(otActual));
		accion.setFechaCreacion(new Date());
		acciones.add(accion);
	}

}
