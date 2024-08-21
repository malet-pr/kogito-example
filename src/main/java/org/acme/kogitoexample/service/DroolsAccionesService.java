package org.acme.kogitoexample.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.acme.kogitoexample.adapter.AccionesAdapter;
import org.acme.kogitoexample.adapter.OTReglaAdapter;
import org.acme.kogitoexample.adapter.ReglasTipoAAdapter;
import org.acme.kogitoexample.model.*;
import org.acme.kogitoexample.repository.ActividadDAO;
import org.acme.kogitoexample.repository.ActividadTipoDAO;
import org.acme.kogitoexample.repository.ReglaLogDAO;
import org.acme.kogitoexample.repository.ReglaTipoDAO;
import org.acme.kogitoexample.util.Constante;
import org.acme.kogitoexample.service.ErrorOtsService;
import org.apache.commons.lang3.StringUtils;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DroolsAccionesService {

    @Inject
    private DroolsRuleService droolsService;

    @Inject
    private ActividadTipoDAO actividadTipoDAO;

    @Inject
    private ReglaTipoDAO reglaTipoDAO;

    @Inject
    private ActividadDAO actividadDAO;

    @Inject
    private ReglaLogDAO reglaLogDAO;
    
    @Inject
    private ErrorOtsService errorOtsService;

    public static Logger log = LoggerFactory.getLogger(DroolsAccionesService.class);


    private void actualizarActividadesEnOTReglaAdapter(OTReglaAdapter adapter) {
        OrdenTrabajoModel ot = adapter.getOt();
        List<String> actividades = ot.getActividades().stream()
                .filter(a -> a != null && a.getActivo().equals(Constante.SI))
                .map(OrdenTrabajoActividadModel::getActividadModel).map(ActividadModel::getCodigo)
                .collect(Collectors.toList());
        adapter.setActividades(actividades);
    }

    public OrdenTrabajoModel recoverImpactarReglaTipoA(RuntimeException e, ReglasTipoAAdapter facts) {
        return new OrdenTrabajoModel();
    }

    public OrdenTrabajoModel impactarReglaTipoA(ReglasTipoAAdapter facts) {
        String uuid = UUID.randomUUID().toString();
        OrdenTrabajoModel ot = facts.getOtEvaluada().getOt();
        List<AccionesAdapter> acciones = facts.getAcciones().stream()
        		.sorted(Comparator.comparing(AccionesAdapter::getFechaCreacion))
                .toList();
        for (AccionesAdapter accion : acciones) {
            List<ReglaLog> logs = getLogInicial(accion, uuid);
            String nombreAccion = accion.getAccion();
			if (nombreAccion.equalsIgnoreCase("agregarActividad")) {
            	agregarActividad(accion, ot, facts.getReglaTipo());
            } else if (nombreAccion.equalsIgnoreCase("desactivarActividades")) {
                desactivarActividades(accion, ot);
            } else if (nombreAccion.equalsIgnoreCase("desactivarActividadesTodas")) {
                desactivarActividadesTodas(accion, ot);
            } else if (nombreAccion.equalsIgnoreCase("desactivarActividadesMenosUna")) {
                desactivarActividadesMenosUna(accion, ot);
            } else if (nombreAccion.equalsIgnoreCase("desactivarActividadesTodasMenos")) {
                desactivarActividadesTodasMenos(accion, ot);
            } else if (nombreAccion.equalsIgnoreCase("reemplazarActividades")) {
                reemplazarActividades(accion, ot);
            } else if (nombreAccion.equalsIgnoreCase("reemplazarActividadesMenosUna")) {
                reemplazarActividadesMenosUna(accion, ot);
            }
            logs = getLogFinal(logs, accion.getOtsMatch());
            printLogs(logs);
            reglaLogDAO.saveAll(logs);
        }
        return ot;
    }

	public boolean agregarActividad (AccionesAdapter accion, OrdenTrabajoModel ot, ReglaTipo rTipo) {
		boolean status = true;
		String actividadNueva = accion.getActividadNueva();
		String reglaNombre = accion.getRegla();
		ActividadTipo actividadTipo = actividadTipoDAO.findByNombreCorto(Constante.NC_ACTIVIDAD_TIPO);
		Regla regla = droolsService.getRegla(reglaNombre);
		ReglaTipo reglaTipo = rTipo;//este metodo deberia decodificar que tipo es y buscar lo en la bd ya que siempre va a estar ahi, al crearlo en el aire rompe luego
		ActividadModel actiModel = actividadDAO.findByCodigo(actividadNueva);
		for (int i = 0; i < accion.getCantidad(); i++) {
			OrdenTrabajoActividadModel actividadActivar = buildNuevaActividad(ot, actividadTipo, regla, actiModel);
			actividadActivar.setReglaTipo(regla != null ? regla.getReglaTipo() : reglaTipo); // TODO WARNING NO COMMITEAR reglaTipoDAO.findByNombreCortoIgnoreCase("REA2")
			ot.getActividades().add(actividadActivar);
		}
		return status;
	}

    private ReglaTipo buildNuevaReglaTipo(String reglaNombre) {
        ReglaTipo reglaTipo = new ReglaTipo();
        if (reglaNombre != null) {
            String reglaTipoNombre = reglaNombre.substring(0, 2);
            reglaTipo.setReglaTipoid(20L);
            reglaTipo.setNombre(reglaTipoNombre.equalsIgnoreCase("GI") ? "Globales" : reglaTipoNombre);
            reglaTipo.setNombreCorto(reglaTipoNombre.equalsIgnoreCase("GI") ? "REGL" : "RE" + reglaTipoNombre);
            reglaTipo.setHeader(reglaTipoNombre.equalsIgnoreCase("GI") ? "REGL.drl" : "RE" + reglaTipoNombre + ".drl");
            reglaTipo.setVisible(Constante.SI);
            reglaTipo.setAgrupador(reglaTipoNombre.equalsIgnoreCase("GI") ? "A" : reglaTipoNombre.substring(0, 1));
        }
        return reglaTipo;
    }

    private OrdenTrabajoActividadModel buildNuevaActividad(OrdenTrabajoModel ot, ActividadTipo actividadTipo, Regla regla, ActividadModel actiModel) {
        OrdenTrabajoActividadModel actividad = new OrdenTrabajoActividadModel();
        Date now = new Date();
        actividad.setActividadModel(actiModel);
        actividad.setActividadTipo(actividadTipo);
        actividad.setActivo(Constante.SI);
        actividad.setCantidad(1L);
        actividad.setFechaCreacion(now); //eliminar cuando se cambie el campo en la tabla
        actividad.setOrdenTrabajo(ot);
        actividad.setReglaAplicada(regla);
        actividad.setReglaTipo(regla != null ? regla.getReglaTipo() : null);
        return actividad;
    }

    public boolean desactivarActividades(AccionesAdapter accion, OrdenTrabajoModel ot) {
        int count = 0;
        boolean status = true;
        String actividadVieja = accion.getActividadVieja();
        String reglaNombre = accion.getRegla();
        Date now = new Date();
        List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
        Regla regla = droolsService.getRegla(reglaNombre);
        List<OrdenTrabajoActividadModel> actividadesViejas =
                actividadesOt.stream()
                        .filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja)
                                && a.getActivo().equals(Constante.ACTIVO_S)
                                && (a.getReglaTipo() ==  null))
                        .toList();
        count = actividadesViejas.size();
        status = (count == 0);
        for (int i = 0; i < count; i++) {
            OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
            actividadDesactivar.setActivo(Constante.NO);
            actividadDesactivar.setReglaAplicada(regla);
        }
        return status;
    }

    public boolean desactivarActividadesTodas(AccionesAdapter accion, OrdenTrabajoModel ot) {
        int count = 0;
        boolean status = true;
        String reglaNombre = accion.getRegla();
        Date now = new Date();
        List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
        Regla regla = droolsService.getRegla(reglaNombre);
        List<OrdenTrabajoActividadModel> actividadesViejas = actividadesOt.stream()
                        .filter(a -> a.getActivo().equals(Constante.ACTIVO_S) && (a.getReglaTipo() ==  null))
                        .toList();
        count = actividadesViejas.size();
        status = (count == 0);
        for (int i = 0; i < count; i++) {
            OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
            actividadDesactivar.setActivo(Constante.NO);
            actividadDesactivar.setReglaAplicada(regla);
        }
        return status;
    }

    public boolean desactivarActividadesTodasMenos(AccionesAdapter accion, OrdenTrabajoModel ot) {
        // Devolver true si borro algo?
        int count = 0;
        boolean status = true;

        String reglaNombre = accion.getRegla();
        String actividadVieja = accion.getActividadVieja();
        Date now = new Date();

        List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();

        Regla regla = droolsService.getRegla(reglaNombre);

        List<OrdenTrabajoActividadModel> actividadesViejas = new ArrayList<OrdenTrabajoActividadModel>();

        if (!StringUtils.isAllBlank(actividadVieja)) {
            actividadesViejas.addAll(
                    actividadesOt.stream()
                            .filter(a -> !a.getActividadModel().getCodigo().equals(actividadVieja)
                                        && a.getActivo().equals(Constante.ACTIVO_S)
                                        && (a.getReglaTipo() ==  null))
                            .toList());
        }

        count = actividadesViejas.size();
        status = (count != 0);

        for (int i = 0; i < count; i++) {
            OrdenTrabajoActividadModel actividad = actividadesViejas.get(i);
            actividad.setActivo(Constante.NO);
            actividad.setReglaAplicada(regla);
        }

        return status;
    }

    public boolean desactivarActividadesMenosUna(AccionesAdapter accion, OrdenTrabajoModel ot) {

        int count = 0;
        boolean status = true;

        String reglaNombre = accion.getRegla();
        String actividadVieja = accion.getActividadVieja();
        Date now = new Date();

        List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();

        Regla regla = droolsService.getRegla(reglaNombre);

        List<OrdenTrabajoActividadModel> actividadesViejas = new ArrayList<OrdenTrabajoActividadModel>();

//		if (StringUtils.isAllBlank(actividadVieja)) {
        if (actividadVieja != null && !actividadVieja.isEmpty()) {
            // Se desactiva solo la especifica MENOS UNA
            // Que este activa y NO haya sido creada de forma manual
            actividadesViejas.addAll(
                    actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja) &&
                            a.getActivo().equals(Constante.ACTIVO_S) &&
                            (a.getReglaTipo() ==  null ||
                            !a.getReglaTipo().getNombreCorto().equals(Constante.REGLA_TIPO_MANUAL))).collect(Collectors.toList()));
        } else {
            // Se desactivan TODAS TODAS MENOS UNA

            // Que este activa y NO haya sido creada de forma manual
            actividadesViejas.addAll(
                    actividadesOt.stream().filter(a -> a.getActivo().equals(Constante.ACTIVO_S) &&
                    		(a.getReglaTipo() ==  null ||
                            !a.getReglaTipo().getNombreCorto().equals(Constante.REGLA_TIPO_MANUAL))).collect(Collectors.toList()));
        }

        count = actividadesViejas.size();
        status = (count == 0);

        // Si hay solo una, vive y deja vivir
        if (count < 2) {
            return status;
        }

        for (int i = 1; i < count; i++) {

            OrdenTrabajoActividadModel actividad = actividadesViejas.get(i);
            actividad.setActivo(Constante.NO);
            actividad.setFechaUltMod(now);
            actividad.setFechaUltModCT(now);
            actividad.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
            actividad.setUltimaReglaAplicada(regla);

            if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {

                desactivarOtActividadEnNotaCred(ot, actividad);

            }
        }
        // TODO: Confirmar si hay act manual que se hace.
        // Por ahora si hay alguna manual devuelve FALSE
        return status;
    }

    @Override
    public boolean reemplazarActividades(AccionesAdapter accion, OrdenTrabajoModel ot) {

        // #REEMPLAZAR
        // FMR-2473: Verificar el funcionamiento del Reemplazar (reemplaza todas las ocurrencias 1 a 1)
        //	reemplazarActividades("800033","800018", drools.getRule().getName())

        int count = 0;
        boolean status = true;

        String actividadVieja = accion.getActividadVieja();
        String actividadNueva = accion.getActividadNueva();
        int cantidad = accion.getCantidad();
        String reglaNombre = accion.getRegla();
        Date now = new Date();

        Regla regla = droolsService.getRegla(reglaNombre);
        List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
        ActividadTipo actividadTipo = actividadTipoDAO.findByNombreCorto(Constante.NC_ACTIVIDAD_TIPO);
        ActividadModel actiModel = actividadDAO.findByCodigo(actividadNueva);
		
		/*// Se pidio que si esta desactivada igualmente se pueda agregar
		// Si no se encontro es porque la actividad no existe o esta desactivada
		if (actiModel == null || actiModel.getActivo().equals(Constante.ACTIVO_N)) {
			status = false;
			return status;
		}
		 */

        List<OrdenTrabajoActividadModel> actividadesViejas =
                actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja) &&
                        a.getActivo().equals(Constante.ACTIVO_S) &&
                        (a.getReglaTipo() ==  null ||
                        !a.getReglaTipo().getNombreCorto().equals(Constante.REGLA_TIPO_MANUAL))).collect(Collectors.toList());

        if (cantidad > 0 && actividadesViejas.size() >= cantidad) {
            actividadesViejas = actividadesViejas.subList(0, cantidad);
        }

        count = actividadesViejas.size();
        status = (count == 0);

        // Si no esta activa
        if (actiModel == null) {
            return status;
        }

        for (int i = 0; i < count; i++) {

            OrdenTrabajoActividadModel actividad = actividadesViejas.get(i);
            actividad.setActivo(Constante.NO);
            actividad.setFechaUltMod(now);
            actividad.setFechaUltModCT(now);
            actividad.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
            actividad.setUltimaReglaAplicada(regla);

            /**
             * ya desactivamos la ot actividad
             * si el acta esta certificada, ademas desactivamos la relacion en la tabla loca
             * si no hay relacion en la tabla loca debemos crear una nota credito para hacerlo
             */
//			if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {
//
//				desactivarOtActividadEnNotaCred(ot, actividad);
//
//			}

            // esta lista es para el registro de CHANO
            // actividadesReemplazadas.add(actividad);

            actividad = new OrdenTrabajoActividadModel();

            actividad.setActividadModel(actiModel);
            actividad.setActividadTipo(actividadTipo);
            actividad.setActivo(Constante.SI);
            actividad.setCantidad(1L);
            actividad.setFechaCreacionCT(now); //eliminar cuando se cambie el campo en la tabla
            actividad.setFechaUltMod(now); //eliminar cuando se cambie el campo en la tabla
            actividad.setFechaUltModCT(now); //eliminar cuando se cambie el campo en la tabla
            actividad.setUsuarioCreacion(Constante.USUARIO_SISTEMA);
            actividad.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
            actividad.setOrdenTrabajo(ot);
            actividad.setUltimaReglaAplicada(regla);
            actividad.setReglaTipo(regla != null ? regla.getReglaTipo() : null);
            // actividad.setReglaTipo(regla != null ? regla.getReglaTipo() : reglaTipoDAO.findByNombreCortoIgnoreCase("REA2")); // TODO WARNING NO COMMITEAR reglaTipoDAO.findByNombreCortoIgnoreCase("REA2")

            ot.getActividades().add(actividad);


            if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {

                this.activarActividadEnNotaDebito(ot, actividad);
                this.desactivarOtActividadEnNotaCred(ot, actividad);

            }

        }
        return status;
    }

    @Override
    public boolean reemplazarActividadesMenosUna(AccionesAdapter accion, OrdenTrabajoModel ot) {

        // #REEMPLAZAR
        // FMR-2471: Reemplazar todos menos 1 (reemplazar todas las ocurrencias de la actividad menos 1)
        //	reemplazarActividadesMenosUna("TS1113","1440","AFMR2384")

        int count = 0;
        boolean status = true;

        String actividadVieja = accion.getActividadVieja();
        String actividadNueva = accion.getActividadNueva();
        String reglaNombre = accion.getRegla();
        Date now = new Date();

        Regla regla = droolsService.getRegla(reglaNombre);
        List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
        ActividadTipo actividadTipo = actividadTipoDAO.findByNombreCorto(Constante.NC_ACTIVIDAD_TIPO);
        ActividadModel actiModel = actividadDAO.findByCodigoAndActivo(actividadNueva, Constante.ACTIVO_S);
		
		/*// Se pidio que si esta desactivada igualmente se pueda agregar
		// Si no se encontro es porque la actividad no existe o esta desactivada
		if (actiModel == null || actiModel.getActivo().equals(Constante.ACTIVO_N)) {
			status = false;
			return status;
		}
		 */

        List<OrdenTrabajoActividadModel> actividadesViejas =
                actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja) &&
                        a.getActivo().equals(Constante.ACTIVO_S) &&
                        (a.getReglaTipo() ==  null ||
                        !a.getReglaTipo().getNombreCorto().equals(Constante.REGLA_TIPO_MANUAL))).collect(Collectors.toList());

        count = actividadesViejas.size();
        status = (count == 0);

        // Si tiene 0 no reemplaza
        // Si tiene 1 no reemplaza
        // Si tiene mas de una, reemplaza todas menos una

        if (actiModel == null || count < 2) {
            return status;
        }

        for (int i = 1; i < count; i++) {

            OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
            actividadDesactivar.setActivo(Constante.NO);
            actividadDesactivar.setFechaUltMod(now);
            actividadDesactivar.setFechaUltModCT(now);
            actividadDesactivar.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
            actividadDesactivar.setUltimaReglaAplicada(regla);


            OrdenTrabajoActividadModel actividadActivar = new OrdenTrabajoActividadModel();

            actividadActivar.setActividadModel(actiModel);
            actividadActivar.setActividadTipo(actividadTipo);
            actividadActivar.setActivo(Constante.SI);
            actividadActivar.setCantidad(1L);
            actividadActivar.setFechaCreacionCT(now); //eliminar cuando se cambie el campo en la tabla
            actividadActivar.setFechaUltMod(now); //eliminar cuando se cambie el campo en la tabla
            actividadActivar.setFechaUltModCT(now); //eliminar cuando se cambie el campo en la tabla
            actividadActivar.setUsuarioCreacion(Constante.USUARIO_SISTEMA);
            actividadActivar.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
            actividadActivar.setOrdenTrabajo(ot);
            actividadActivar.setUltimaReglaAplicada(regla);
            actividadActivar.setReglaTipo(regla != null ? regla.getReglaTipo() : null);

            ot.getActividades().add(actividadActivar);


            if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {

                this.activarActividadEnNotaDebito(ot, actividadActivar);
                this.desactivarOtActividadEnNotaCred(ot, actividadDesactivar);
            }

        }

        return status;
    }

    @Override
    public OrdenTrabajoModel recover(RuntimeException e, ReglasTipoAAdapter facts) {
        errorOtsService.add(facts.getReglaTipo().getReglaTipoid(), facts.getOtEvaluada().getNumeroOt(), "", e, Constante.USUARIO_SISTEMA);

        return facts.getOtEvaluada().getOt();
    }

    private HashSet<OrdenTrabajoModel> agregarActividad(AccionesAdapter accion, List<OTReglaAdapter> otsMatch) {
        log.info("agregarActividad");

        HashSet<OrdenTrabajoModel> otsModificadas = new HashSet<OrdenTrabajoModel>();

        String actividadNueva = accion.getActividadNueva();
        String reglaNombre = accion.getRegla();
        ActividadTipo actividadTipo = actividadTipoDAO.findByNombreCorto(Constante.NC_ACTIVIDAD_TIPO);

        Regla regla = droolsService.getRegla(reglaNombre);
        ActividadModel actiModel = actividadDAO.findByCodigo(actividadNueva);

        for (OTReglaAdapter otMatch : otsMatch) {
            log.info("OT A IMPACTAR : " + otMatch.getNumeroOt());

            OrdenTrabajoModel ot = otMatch.getOt();

//			if (ot.getMarcaManual() != Constante.SI) {
            if (Character.compare(ot.getMarcaManual(), Constante.SI) != 0) {
                log.info("MARCA MANUAL : " + ot.getMarcaManual());

                for (int i = 0; i < accion.getCantidad(); i++) {

                    OrdenTrabajoActividadModel actividadActivar = this.buildNuevaActividad(ot, actividadTipo, regla, actiModel);

                    ot.getActividades().add(actividadActivar);
                    log.info("ACTIVAR ACTIVIDAD : " + actividadActivar.getActividadModel().getNombre());


                    if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {
                        log.info("ACTA CERTIFICADA : SI");

                        this.activarActividadEnNotaDebito(ot, actividadActivar);
                    }
                }

                otsModificadas.add(ot);

            }
        }

        return otsModificadas;
    }

    private HashSet<OrdenTrabajoModel> desactivarActividades(AccionesAdapter accion, List<OTReglaAdapter> otsMatch) {
        log.info("desactivarActividades");

        HashSet<OrdenTrabajoModel> otsModificadas = new HashSet<OrdenTrabajoModel>();

        int count = 0;


        String actividadVieja = accion.getActividadVieja();
        String reglaNombre = accion.getRegla();
        Date now = new Date();

        for (OTReglaAdapter otMatch : otsMatch) {
            OrdenTrabajoModel ot = otMatch.getOt();
//			if (ot.getMarcaManual() != Constante.SI) {
            if (Character.compare(ot.getMarcaManual(), Constante.SI) != 0) {
                log.info("MARCA MANUAL : " + ot.getMarcaManual());

                List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
                Regla regla = droolsService.getRegla(reglaNombre);
                List<OrdenTrabajoActividadModel> actividadesViejas =
                        actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja) &&
                                a.getActivo().equals(Constante.ACTIVO_S)).collect(Collectors.toList());
                count = actividadesViejas.size();
                for (int i = 0; i < count; i++) {
                    OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
                    actividadDesactivar.setActivo(Constante.NO);
                    actividadDesactivar.setFechaUltMod(now);
                    actividadDesactivar.setFechaUltModCT(now);
                    actividadDesactivar.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
                    actividadDesactivar.setUltimaReglaAplicada(regla);
                    log.info("DESACTIVAR ACTIVIDAD(OT_ACTIVIDAD) : " + actividadDesactivar.getActividadModel().getNombre());

                    if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {
                        log.info("ACTA CERTIFICADA : SI");

                        this.desactivarOtActividadEnNotaCred(ot, actividadDesactivar);
                    }


                }
                otsModificadas.add(ot);
            }
        }
        return otsModificadas;
    }

    private HashSet<OrdenTrabajoModel> desactivarActividadesTodas(AccionesAdapter accion, List<OTReglaAdapter> otsMatch) {
        HashSet<OrdenTrabajoModel> otsModificadas = new HashSet<OrdenTrabajoModel>();
        int count = 0;
        String reglaNombre = accion.getRegla();
        Date now = new Date();
        for (OTReglaAdapter otMatch : otsMatch) {
            OrdenTrabajoModel ot = otMatch.getOt();
            if (Character.compare(ot.getMarcaManual(), Constante.SI) != 0) {
                List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
                Regla regla = droolsService.getRegla(reglaNombre);
                List<OrdenTrabajoActividadModel> actividadesViejas =
                        actividadesOt.stream().filter(a -> a.getActivo().equals(Constante.ACTIVO_S)).collect(Collectors.toList());
                count = actividadesViejas.size();
                for (int i = 0; i < count; i++) {
                    OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
                    actividadDesactivar.setActivo(Constante.NO);
                    actividadDesactivar.setFechaUltMod(now);
                    actividadDesactivar.setFechaUltModCT(now);
                    actividadDesactivar.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
                    actividadDesactivar.setUltimaReglaAplicada(regla);

                    if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {

                        this.desactivarOtActividadEnNotaCred(ot, actividadDesactivar);
                    }

                }
                otsModificadas.add(ot);
            }
        }

        return otsModificadas;
    }

    private HashSet<OrdenTrabajoModel> desactivarActividadesMenosUna(AccionesAdapter accion, List<OTReglaAdapter> otsMatch) {
        HashSet<OrdenTrabajoModel> otsModificadas = new HashSet<OrdenTrabajoModel>();
        int count = 0;
        String reglaNombre = accion.getRegla();
        String actividadVieja = accion.getActividadVieja();
        Date now = new Date();
        for (OTReglaAdapter otMatch : otsMatch) {
            OrdenTrabajoModel ot = otMatch.getOt();
//			if (ot.getMarcaManual() != Constante.SI) {

            if (Character.compare(ot.getMarcaManual(), Constante.SI) != 0) {

                List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
                Regla regla = droolsService.getRegla(reglaNombre);
                List<OrdenTrabajoActividadModel> actividadesViejas = new ArrayList<OrdenTrabajoActividadModel>();

//				if (StringUtils.isAllBlank(actividadVieja)) {

                if (actividadVieja != null && !actividadVieja.isEmpty()) {
                    // Se desactiva solo la especifica MENOS UNA
                    actividadesViejas.addAll(
                            actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja) &&
                                    a.getActivo().equals(Constante.ACTIVO_S)).collect(Collectors.toList()));
                } else {
                    // Se desactivan TODAS TODAS MENOS UNA
                    actividadesViejas.addAll(
                            actividadesOt.stream().filter(a -> a.getActivo().equals(Constante.ACTIVO_S) &&
                            		(a.getReglaTipo() ==  null ||
                                    !a.getReglaTipo().getNombreCorto().equals(Constante.REGLA_TIPO_MANUAL))).collect(Collectors.toList()));
                }

                count = actividadesViejas.size();
                // Si hay solo una, vive y deja vivir
                if (count < 2) {
                    return otsModificadas;
                }
                for (int i = 1; i < count; i++) {
                    OrdenTrabajoActividadModel actividad = actividadesViejas.get(i);
                    actividad.setActivo(Constante.NO);
                    actividad.setFechaUltMod(now);
                    actividad.setFechaUltModCT(now);
                    actividad.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
                    actividad.setUltimaReglaAplicada(regla);
                    // esta lista es para el registro de CHANO
                    //otActElimlst.add(actividadVieja);
                }
                otsModificadas.add(ot);
            }
        }

        return otsModificadas;
    }

    private HashSet<OrdenTrabajoModel> reemplazarActividades(AccionesAdapter accion, List<OTReglaAdapter> otsMatch) {

        log.info("reemplazarActividades");

        HashSet<OrdenTrabajoModel> otsModificadas = new HashSet<OrdenTrabajoModel>();
        int count = 0;
        String actividadVieja = accion.getActividadVieja();
        String actividadNueva = accion.getActividadNueva();
        int cantidad = accion.getCantidad();
        String reglaNombre = accion.getRegla();
        Date now = new Date();
        ActividadTipo actividadTipo = actividadTipoDAO.findByNombreCorto(Constante.NC_ACTIVIDAD_TIPO);
        ActividadModel actiModel = actividadDAO.findByCodigo(actividadNueva);

        for (OTReglaAdapter otMatch : otsMatch) {
            OrdenTrabajoModel ot = otMatch.getOt();

            if (Character.compare(ot.getMarcaManual(), Constante.SI) != 0) {
                List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
                Regla regla = droolsService.getRegla(reglaNombre);
                List<OrdenTrabajoActividadModel> actividadesViejas =
                        actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(actividadVieja) &&
                                a.getActivo().equals(Constante.ACTIVO_S)).collect(Collectors.toList());
                if (cantidad > 0 && actividadesViejas.size() >= cantidad) {
                    actividadesViejas = actividadesViejas.subList(0, cantidad);
                }
                count = actividadesViejas.size();
                // Si no esta activa
                if (actiModel == null) {
                    return otsModificadas;
                }
                for (int i = 0; i < count; i++) {
                    OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
                    actividadDesactivar.setActivo(Constante.NO);
                    actividadDesactivar.setFechaUltMod(now);
                    actividadDesactivar.setFechaUltModCT(now);
                    actividadDesactivar.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
                    actividadDesactivar.setUltimaReglaAplicada(regla);


                    OrdenTrabajoActividadModel actividadActivar = this.buildNuevaActividad(ot, actividadTipo, regla, actiModel);

                    ot.getActividades().add(actividadActivar);

                    if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {

                        this.activarActividadEnNotaDebito(ot, actividadActivar);
                        this.desactivarOtActividadEnNotaCred(ot, actividadDesactivar);
                    }
                }
                otsModificadas.add(ot);
            }
        }
        return otsModificadas;
    }

    private HashSet<OrdenTrabajoModel> reemplazarActividadesMenosUna(AccionesAdapter accion, List<OTReglaAdapter> otsMatch, Date impactarReglaDT) {
        HashSet<OrdenTrabajoModel> otsModificadas = new HashSet<OrdenTrabajoModel>();
        // #REEMPLAZAR
        // FMR-2471: Reemplazar todos menos 1 (reemplazar todas las ocurrencias de la actividad menos 1)
        //	reemplazarActividadesMenosUna("TS1113","1440","AFMR2384")

        int count = 0;
        String codigoActividadVieja = accion.getActividadVieja();
        String codigoActividadNueva = accion.getActividadNueva();
        String reglaNombre = accion.getRegla();
        Date now = new Date();
        ActividadModel actiModel = actividadDAO.findByCodigo(codigoActividadNueva);
		/*// Se pidio que si esta desactivada igualmente se pueda agregar
		// Si no se encontro es porque la actividad no existe o esta desactivada
		if (actiModel == null || actiModel.getActivo().equals(Constante.ACTIVO_N)) {
			status = false;
			return status;
		}
		 */
        Regla regla = droolsService.getRegla(reglaNombre);
        for (OTReglaAdapter otMatch : otsMatch) {
            OrdenTrabajoModel ot = otMatch.getOt();
            if (Character.compare(ot.getMarcaManual(), Constante.SI) != 0) {
                List<OrdenTrabajoActividadModel> actividadesOt = ot.getActividades();
                List<OrdenTrabajoActividadModel> actividadesViejas =
                        actividadesOt.stream().filter(a -> a.getActividadModel().getCodigo().equals(codigoActividadVieja) &&
                                a.getActivo().equals(Constante.ACTIVO_S)).collect(Collectors.toList());
                count = actividadesViejas.size();
                // Si tiene 0 no reemplaza
                // Si tiene 1 no reemplaza
                // Si tiene mas de una, reemplaza todas menos una
                if (actiModel == null || count < 2) {
                    return otsModificadas;
                }
                for (int i = 1; i < count; i++) {
                    OrdenTrabajoActividadModel actividadDesactivar = actividadesViejas.get(i);
                    actividadDesactivar.setActivo(Constante.NO);
                    actividadDesactivar.setFechaUltMod(now);
                    actividadDesactivar.setFechaUltModCT(now);
                    actividadDesactivar.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
                    actividadDesactivar.setUltimaReglaAplicada(regla);

                    OrdenTrabajoActividadModel actividadActivar = this.otActividadBuilder(codigoActividadNueva, ot, regla, impactarReglaDT);
                    ot.getActividades().add(actividadActivar);

                    if (ot.getActa() != null && ot.getActa().getActaEstado().getNombreCorto().equalsIgnoreCase(Constante.ACTA_ESTADO_CERTIFICADA_NC)) {

                        this.activarActividadEnNotaDebito(ot, actividadActivar);
                        this.desactivarOtActividadEnNotaCred(ot, actividadDesactivar);
                    }

                }
                otsModificadas.add(ot);
            }
        }
        return otsModificadas;
    }

    private OrdenTrabajoActividadModel otActividadBuilder(String codigoACtividad, OrdenTrabajoModel ot, Regla regla, Date impactarReglaDT) {
        OrdenTrabajoActividadModel nuevaOtAct = new OrdenTrabajoActividadModel();
        ActividadModel actividad = actividadDAO.findByCodigo(codigoACtividad);
        ActividadTipo actividadTipo = actividadTipoDAO.findByNombreCorto(Constante.NC_ACTIVIDAD_TIPO);

        nuevaOtAct.setActividadModel(actividad);
        nuevaOtAct.setActividadTipo(actividadTipo);
        nuevaOtAct.setActivo(Constante.SI);
        nuevaOtAct.setCantidad(1L);
        nuevaOtAct.setFechaCreacionCT(impactarReglaDT); //eliminar cuando se cambie el campo en la tabla
        nuevaOtAct.setFechaUltMod(impactarReglaDT); //eliminar cuando se cambie el campo en la tabla
        nuevaOtAct.setFechaUltModCT(impactarReglaDT); //eliminar cuando se cambie el campo en la tabla
        nuevaOtAct.setUsuarioCreacion(Constante.USUARIO_SISTEMA);
        nuevaOtAct.setUsuarioUltMod(Constante.USUARIO_SISTEMA);
        nuevaOtAct.setOrdenTrabajo(ot);
        nuevaOtAct.setUltimaReglaAplicada(regla);
        nuevaOtAct.setReglaTipo(regla != null ? regla.getReglaTipo() : null);

        return nuevaOtAct;
    }

    private List<ReglaLog> getLogFinal(List<ReglaLog> logs, List<OTReglaAdapter> ots) {
        for (ReglaLog log : logs) {
            log.setFechaFin(new Date());
            log.setActividadesActivasFinal(getActividades(ots, log.getNroOT(), Constante.SI));
            log.setActividadesDesactivadasFinal(getActividades(ots, log.getNroOT(), Constante.NO));
        }
        return logs;
    }

    private List<ReglaLog> getLogInicial(AccionesAdapter accion, String uuid) {

        List<ReglaLog> logs = new ArrayList<ReglaLog>();

        for (OTReglaAdapter adapter : accion.getOtsMatch()) {
            ReglaLog log = new ReglaLog();

            log.setIdCorrida(uuid);
            log.setReglaNombre(accion.getRegla() + " - " + accion.getAccion());
            log.setNroOT(adapter.getNumeroOt());
            log.setFechaInicio(new Date());
            log.setActividadesActivasInicial(getActividades(adapter.getOt(), Constante.SI));
            log.setActividadesDesactivadasInicial(getActividades(adapter.getOt(), Constante.NO));
            log.setSerializables(getSerializables(adapter.getOt()));
            log.setPais(adapter.getDomiPais());
            log.setTareaJobTypeCodigo(adapter.getJobType());
            log.setContratista(adapter.getContratista() + " - " + adapter.getContratistaCodigo());
            log.setClaseOT(adapter.getClaseOt() + " - " +
                    adapter.getClaseOtCodigo() == null ? "" : adapter.getClaseOtCodigo()

            );
            if (adapter.getFechaCreacionOT() != null)
                log.setFechaCreacionOt(convertToDateViaInstant(adapter.getFechaCreacionOT()));
            log.setFechaCierreOt(convertToDateViaInstant(adapter.getFechaCierreOT()));

            logs.add(log);
        }

        return logs;
    }

    private void printLogs(List<ReglaLog> logs) {

        for (ReglaLog log : logs) {
            StringBuilder sb = new StringBuilder();

            sb.append(" reglaNombre: ").append(log.getReglaNombre());
            sb.append(" | NroOt: ").append(log.getNroOT());
            sb.append(" | FechaCreacionOt: ").append(log.getFechaCreacionOt());
            sb.append(" | FechaCierreOt: ").append(log.getFechaCierreOt());

            sb.append(" | ActividadesActivasIniciales: ").append(log.getActividadesActivasInicial());
            sb.append(" | ActividadesInActivasIniciales: ").append(log.getActividadesDesactivadasInicial());
            sb.append(" | ActividadesActivasFinales: ").append(log.getActividadesActivasFinal());
            sb.append(" | ActividadesInActivasFinales: ").append(log.getActividadesDesactivadasFinal());

            sb.append(" | Serializables: ").append(log.getSerializables());

            sb.append(" | Pais: ").append(log.getPais());
            sb.append(" | Tarea: ").append(log.getTareaJobTypeCodigo());
            sb.append(" | Contratista: ").append(log.getContratista());

            sb.append(" | ClaseOt: ").append(log.getClaseOT());

            sb.append(" | idCorrida: ").append(log.getIdCorrida());

            System.out.println(sb.toString());
        }
    }

    private String getActividades(List<OTReglaAdapter> ots, String nroOt, Character activo) {

        OTReglaAdapter ot = ots.stream().filter(x -> x.getNumeroOt().equalsIgnoreCase(nroOt)).findFirst().get();
        return getActividades(ot.getOt(), activo);
    }

    private String getActividades(OrdenTrabajoModel ot, Character activo) {

        StringBuilder sb = new StringBuilder();

        for (OrdenTrabajoActividadModel actividad : ot.getActividades()) {
//			if (actividad.getActivo().equals(activo)) {
            if (Character.compare(actividad.getActivo(), activo) == 0) {
                sb.append(actividad.getActividadModel().getCodigo());
                sb.append(";");
            }
        }
        return sb.toString();
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

}