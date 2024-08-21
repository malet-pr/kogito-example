package org.acme.kogitoexample.adapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.acme.kogitoexample.model.ActividadModel;
import org.acme.kogitoexample.model.OrdenTrabajoActividadModel;
import org.acme.kogitoexample.model.OrdenTrabajoModel;
import org.acme.kogitoexample.model.OTReglaA;
import org.acme.kogitoexample.model.ReglaTipo;
import org.acme.util.Constante;

public class AdapterBuilder {

    // FACTS con los datos para que se ejecute la regla A
    public static ReglasTipoAAdapter reglasTipoAAdapterBuilder(ReglaTipo tipo, OrdenTrabajoModel ot) {
        ReglasTipoAAdapter adapter = new ReglasTipoAAdapter(tipo);
        adapter.setReglaTipo(tipo);
        adapter.setOtEvaluada(AdapterBuilder.buildOTReglaAdapter(ot));
        return adapter;
    }

    // Buildea el adapter con los datos mas cocinados para ejecutar las reglas
    public static OTReglaAdapter buildOTReglaAdapter(OrdenTrabajoModel entity) {
        OTReglaAdapter adapter = new OTReglaAdapter();
        List<OrdenTrabajoActividadModel> actividadesOt = entity.getActividades().stream()
                .filter(a -> a.getActivo().equals(Constante.SI))
                .toList();
        List<String> actividades = actividadesOt.stream()
                .map(OrdenTrabajoActividadModel::getActividadModel)
                .filter(a -> a != null && a.getActivo().equals(Constante.SI))
                .map(ActividadModel::getCodigo)
                .collect(Collectors.toList());
        adapter.setActividades(actividades);
        adapter.setDomiPais(entity.getDomiPais());
        if (entity.getFechaCreacion() != null)
            adapter.setFechaCreacionOT(toLocalDate(entity.getFechaCreacion()));
        adapter.setFechaCierreOT(toLocalDate(entity.getFechaCierreOt()));
        adapter.setNumeroOt(entity.getNroOT());
        adapter.setOt(entity);
        return adapter;
    }

    public static OTReglaAdapter buildOTReglaAdapter(OTReglaA entity) {
        OTReglaAdapter adapter = new OTReglaAdapter();
        if (entity.getCodigoActividades() != null && !entity.getCodigoActividades().isBlank()) {
            adapter.setActividades(Arrays.asList((entity.getCodigoActividades().split(";"))));
        }
        adapter.setReglaFlujo(entity.getReglaFlujo());
        adapter.setDomiPais(entity.getDomiPais());
        adapter.setDomiDireccion(entity.getDomicilioDireccion());
        adapter.setDomiLocalidad(entity.getDomicilioLocalidad());
        if (entity.getFechaCreacionOT() != null) {
            adapter.setFechaCreacionOT(toLocalDate(entity.getFechaCreacionOT()));
            adapter.setFechaHoraCreacionOT(toLocalDateTime(entity.getFechaCreacionOT()));
        } else {
            System.out.println("Falta fecha de Creacion: " + entity.getNumeroOt());
        }
        if (entity.getFechaCierreOT() != null) {
            adapter.setFechaCierreOT(toLocalDate(entity.getFechaCierreOT()));
            adapter.setFechaHoraCierreOT(toLocalDateTime(entity.getFechaCierreOT()));
        } else {
            System.out.println("Falta fecha de Cierre: " + entity.getNumeroOt());
        }
        adapter.setOt(entity.getOrdenTrabajo());
        adapter.setNumeroOt(entity.getNumeroOt());
        adapter.setClienteId(entity.getClienteId());
        adapter.setActividades(new ArrayList<String>());
        if (entity.getCodigoActividades() != null && !entity.getCodigoActividades().isBlank()) {
            String[] actividades = entity.getCodigoActividades().split(";");
            adapter.getActividades().addAll(Arrays.asList(actividades));
        }
        adapter.setNumeroOt(entity.getNumeroOt());
        adapter.setOt(entity.getOrdenTrabajo());
        return adapter;
    }

    public static LocalDate toLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}