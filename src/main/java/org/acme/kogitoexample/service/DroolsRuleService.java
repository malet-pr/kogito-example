package org.acme.kogitoexample.service;

import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.acme.kogitoexample.model.Regla;
import org.acme.kogitoexample.model.ReglaTipo;
import org.acme.kogitoexample.repository.OrdenTrabajoDAO;
import org.acme.kogitoexample.repository.ReglaDAO;
import org.acme.kogitoexample.repository.ReglaFlujoDAO;
import org.acme.kogitoexample.repository.ReglaTipoDAO;
import org.acme.kogitoexample.util.Constante;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.service.ErrorOtsService;

import static java.nio.file.Files.copy;

@ApplicationScoped
public class DroolsRuleService {
    
    @Inject
    private DroolsAccionesService droolsAccionesService;

    @Inject
    private OrdenTrabajoDAO ordenTrabajoDAO;

    @Inject
    private ReglaDAO reglaDAO;

    @Inject
    private ReglaTipoDAO reglaTipoDAO;

    @Inject
    private ReglaFlujoDAO reglaFlujoDAO;
    
    @Inject
    private ErrorOtsService errorOtsService;

    
    public Regla getRegla(String reglaNombre) {
        List<Regla> reglas = reglaDAO.findByNombreIgnoreCaseAndActivo(reglaNombre, Constante.SI);
        Regla regla = reglas.stream().findFirst().orElse(null);
        return regla;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////
// # Obtencion de las Reglas a partir de la DB y el Header en el File System
/////////////////////////////////////////////////////////////////////////////////////////////////

    // Arma un String con el Header de Regla y las Reglas en los String de la lista
    
    public String getReglaCompleta(String header, List<Regla> reglas) {
        String reglaCompleta = "";
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        for (Regla regla : reglas) {
            sb.append(System.lineSeparator());
            sb.append(regla.getReglaDRL());
        }
        reglaCompleta = sb.toString();
        return reglaCompleta;
    }

    // Lee un Resource y devuelve el contenido en un string
    // Utilizado para leer el HEADER de una regla levantado del File System
    
    public String readAllText(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {

            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    } 

    public String getHeader(ReglaTipo tipo) {
        String header = "";
        if (StringUtils.isBlank(tipo.getHeaderDefault())) {
            Resource res = ResourceFactory.newClassPathResource(tipo.getHeader());
            header = readAllText(res);
        } else {
            header = tipo.getHeaderDefault();
        }
        return header;
    }


    // Trae todas las reglas del tipo determinado de la DB -- SIN HEADER --
    
    public List<Regla> getReglas(ReglaTipo tipo) {
        List<Regla> reglas = reglaDAO.findByReglaTipoAndActivo(tipo, Constante.SI);
        return reglas;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
//# Instancia de KieContainer para ejecucion
/////////////////////////////////////////////////////////////////////////////////////////////////
    
    public KieContainer getKieContainer(String key, String val) {

        KieServices kieServices = KieServices.Factory.get();
        KieBuilder kieBuilder = getKieBuilder(key, val);
        KieModule kieModule = kieBuilder.getKieModule();
        
//        ReleaseId relid = kieServices.newReleaseId("nameSpace", "reglab", "threadId");
        System.out.println("RELEASEID====================="+kieModule.getReleaseId());
        return kieServices.newKieContainer(kieModule.getReleaseId());
//        return kieServices.newKieContainer(relid);
    }

    
    public KieBuilder getKieBuilder(String key, String val) {

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();

        kfs.write("src/main/resources/" + key, val);
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        return kieBuilder;
    }
    
    //nuevo kie builder container para threads
    
    public KieContainer getKieContainer(String key, String val, String threadName) {

        KieServices kieServices = KieServices.Factory.get();
        KieBuilder kieBuilder = getKieBuilder(key, val, threadName);
        KieModule kieModule = kieBuilder.getKieModule();
        //ReleaseId releaseId = ks.newReleaseId("com.rule", "test" + taskCounter, "1.0.0");

        System.out.println("RELEASEID====================="+kieModule.getReleaseId());
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
    
	public KieBuilder getKieBuilder(String key, String val, String threadName) {
    	KieServices kieServices = KieServices.Factory.get();
    	KieFileSystem kfs = kieServices.newKieFileSystem();
    	kfs.write("src/main/resources/" + key, val);
    	
    	ReleaseId rId = kieServices.newReleaseId("com.rules", threadName,"1.0.0");
    	kfs.generateAndWritePomXML(rId);
    	
    	KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
    	return kieBuilder;
	}
    
    

    
    public Map<String, String> getRuleMetaData(String metaData) {

        Map<String, String> ruleMetaData = Arrays.stream(metaData.split(","))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(
                        a -> a[0].trim(),  //key
                        a -> a[1].trim()   //value
                ));

        return ruleMetaData;
    }

    
    public List<ReglaTipo> getReglaTipos(String agrupador) {
        List<ReglaTipo> tipos = new ArrayList<ReglaTipo>();
        // Buscamos todas las reglas de Tipo que queremos ejecutar segun el agrupador
        tipos = reglaTipoDAO.findByAgrupadorOrderByOrdenAsc(agrupador);

        return tipos;
    }

    // Instancia un adapter con la regla de un tipo determinado
    
    public ReglaAdapter getReglaAdapter(ReglaTipo tipo) {

        String header = getHeader(tipo);
        List<Regla> reglas = getReglas(tipo);
        String reglaCompleta = getReglaCompleta(header, reglas);
        // Resource val = ResourceFactory.newClassPathResource(reglaCompleta);
        KieContainer kieContainer = getKieContainer(tipo.getHeader(), reglaCompleta);

        ReglaAdapter adapter = new ReglaAdapter();
        adapter.setTipo(tipo);
        adapter.setHeader(header);
        adapter.setReglas(reglas);
        adapter.setReglaCompleta(reglaCompleta);
        adapter.setKieContainer(kieContainer);

        return adapter;
    }

    
    public HashMap<ReglaTipo, ReglaAdapter> getReglas(String agrupador) {

        // Se podria usar LinkedHashMap para preservar el orden, pero usa mas memoria
        HashMap<ReglaTipo, ReglaAdapter> reglas = new HashMap<ReglaTipo, ReglaAdapter>();

        List<ReglaTipo> reglasTipo = getReglaTipos(agrupador);

        for (ReglaTipo tipo : reglasTipo) {

            System.out.println("Ejecucion de Reglas: Obteniendo Reglas Tipo " + tipo.getNombre());
            ReglaAdapter regla = getReglaAdapter(tipo);
            reglas.put(tipo, regla);
        }


        return reglas;
    }
    
    public ConcurrentHashMap<ReglaTipo, ReglaAdapter> getReglasConc(String agrupador, String threadName) {

        // Se podria usar LinkedHashMap para preservar el orden, pero usa mas memoria
    	ConcurrentHashMap<ReglaTipo, ReglaAdapter> reglas = new ConcurrentHashMap<ReglaTipo, ReglaAdapter>();

        List<ReglaTipo> reglasTipo = getReglaTipos(agrupador);

        for (ReglaTipo tipo : reglasTipo) {

            System.out.println("Ejecucion de Reglas: Obteniendo Reglas Tipo " + tipo.getNombre());
            ReglaAdapter regla = getReglaAdapter(tipo, threadName);
            reglas.put(tipo, regla);
        }


        return reglas;
    }

    // Dispara la ejecución de reglas
    public void fireAllRules(ReglaAdapter adapter, ReglasTipoAAdapter facts) {

        KieContainer kieContainer = adapter.getKieContainer();
        KieSession kieSession = kieContainer.newKieSession();

        try {
            kieSession.insert(facts);
            kieSession.getAgenda().getAgendaGroup(adapter.getTipo().getNombreCorto()).setFocus();
            fireAllRules(adapter, kieSession);
        } finally {
            kieSession.dispose();
        }
    }


    public void fireAllRules(ReglaAdapter adapter, KieSession kieSession) {
        kieSession.fireAllRules(new AgendaFilter() {

            
            public boolean accept(Match match) {

                String agendaGroup = getRuleMetaData(match.getRule().toString()).get("agendaGroup");

                if (adapter.getTipo().getNombreCorto().equals(agendaGroup)) {
                    return true;
                }
                return false;
            }
        });
    }


    private String removeComments(String prgm) {
        int n = prgm.length();
        String res;

        // Flags to indicate that single line and multiple line comments
        // have started or not.
        Boolean s_cmt = false;
        Boolean m_cmt = false;


        // Traverse the given program
        for (int i = 0; i < n; i++) {
            // If single line comment flag is on, then check for end of it
            if (s_cmt == true && prgm.charAt(i) == '\n') {
                s_cmt = false;
            } else if (m_cmt == true && prgm.charAt(i) == '*' && prgm.charAt(i + 1) == '/') {
                // If multiple line comment is on, then check for end of it
                m_cmt = false;
                i++;
            }
        }

        return prgm;
    }

    
    public boolean ejecutarReglasA(OrdenTrabajoModel ot) {

        List<OrdenTrabajoModel> ots = new ArrayList<OrdenTrabajoModel>();
        ots.add(ot);
        return ejecutarReglasA("A", ots);
    }

    
    public boolean ejecutarReglasA(String agrupador, List<OrdenTrabajoModel> ots) {

//        System.out.println("Ejecucion metodo: ejecutarReglasA");
//        if(true)
//            throw new NullPointerException("Prueba demo :)");
        // Estado Final de las OTs
        ReglaFlujo reglaAAplicada = reglaFlujoDAO.findByNombreIgnoreCase(Constante.REGLAS_A_APLICADAS);

        ots = ots.stream().filter(ot -> ot != null && ot.getActividades() != null && !ot.getActividades().isEmpty())
                .collect(Collectors.toList());

        // Cachea la coleccion de reglas a ejecutar
        // Buscamos todas las reglas de Tipo que queremos ejecutar segun el agrupador
        HashMap<ReglaTipo, ReglaAdapter> reglas = this.getReglas(agrupador);

        List<ReglaTipo> reglasTipo = new ArrayList<ReglaTipo>(reglas.keySet())
                .stream().sorted(Comparator.comparing(ReglaTipo::getOrden)).collect(Collectors.toList());

        for (OrdenTrabajoModel ot : ots) {

            OrdenTrabajoModel otEvaluada = ot;
            System.out.println(" \r\n ### Evaluando OT: " + ot.getNroOT());

            // Solo para JUnit: Syso de las actividades
            actividadesEnOt(ot);

            // METODO FUMON DE CHANO PARA LLEVAR UN CONTROL DE QUE COSA SE REEMPLAZO POR OTRA
            // actividadesReemplazadas = new ArrayList<OrdenTrabajoActividadModel>();
            // actividadesReemplazantes = new ArrayList<OrdenTrabajoActividadModel>();

            for (ReglaTipo reglaTipo : reglasTipo) {

                // Se preparan los facts para ejecutar
                // HAY QUE GENERAR ESTE FACT A PARTIR DE LA OT Q ESTAS EVALUANDO
                // CADA VEZ Q TERMINA DE EJECUTARSE UNA REGLA, HAY QUE GENERAR UN FACT NUEVO
                // A PARTIR DE LA "OrdenTrabajoModel ot" QUE SALE AL TERMINAR DE EJECUTAR

                // TODO: VER DE HACER MENOS COSAS EN LA SIGUIENTE LINEA (HABRIA Q CAMBIAR SOLO EL TIPO?)
                ReglasTipoAAdapter facts = AdapterBuilder.reglasTipoAAdapterBuilder(reglaTipo, otEvaluada);

                // Se toma la regla cacheada y se ejecuta
                ReglaAdapter regla = reglas.get(reglaTipo);
                this.fireAllRules(regla, facts);

                otEvaluada = droolsAccionesService.impactarReglaTipoA(facts);

            }

            ot.setReglaFlujo(reglaAAplicada);
            ordenTrabajoDAO.save(otEvaluada);

            // Solo para JUnit: Syso de las actividades
            actividadesEnOt(ot);
        }
        return true;
    }

//    @Async
//    
//    public boolean ejecutarReglasAAsync(String agrupador, List<String> ots) {
//
//        // Estado Final de las OTs
//        ReglaFlujo reglaAAplicada = reglaFlujoDAO.findByNombreIgnoreCase(Constante.REGLAS_A_APLICADAS);
//
//        // Cachea la coleccion de reglas a ejecutar
//        // Buscamos todas las reglas de Tipo que queremos ejecutar segun el agrupador
//        HashMap<ReglaTipo, ReglaAdapter> reglas = this.getReglas(agrupador);
//
//        List<ReglaTipo> reglasTipo = new ArrayList<ReglaTipo>(reglas.keySet())
//                .stream().sorted(Comparator.comparing(ReglaTipo::getOrden)).collect(Collectors.toList());
//
//        for (String ot : ots) {
//
//            OrdenTrabajoModel otEvaluada = ordenTrabajoDAO.findByNroOT(ot);
//            if (otEvaluada != null && otEvaluada.getActividades() != null && !otEvaluada.getActividades().isEmpty()) {
//                System.out.println(" \r\n ### Evaluando OT: " + otEvaluada.getNroOT());
//
//                // Solo para JUnit: Syso de las actividades
//                actividadesEnOt(otEvaluada);
//
//                // METODO FUMON DE CHANO PARA LLEVAR UN CONTROL DE QUE COSA SE REEMPLAZO POR OTRA
//                // actividadesReemplazadas = new ArrayList<OrdenTrabajoActividadModel>();
//                // actividadesReemplazantes = new ArrayList<OrdenTrabajoActividadModel>();
//
//                for (ReglaTipo reglaTipo : reglasTipo) {
//
//                    // Se preparan los facts para ejecutar
//                    // HAY QUE GENERAR ESTE FACT A PARTIR DE LA OT Q ESTAS EVALUANDO
//                    // CADA VEZ Q TERMINA DE EJECUTARSE UNA REGLA, HAY QUE GENERAR UN FACT NUEVO
//                    // A PARTIR DE LA "OrdenTrabajoModel ot" QUE SALE AL TERMINAR DE EJECUTAR
//
//                    // TODO: VER DE HACER MENOS COSAS EN LA SIGUIENTE LINEA (HABRIA Q CAMBIAR SOLO EL TIPO?)
//                    ReglasTipoAAdapter facts = AdapterBuilder.reglasTipoAAdapterBuilder(reglaTipo, otEvaluada);
//
//                    // Se toma la regla cacheada y se ejecuta
//                    ReglaAdapter regla = reglas.get(reglaTipo);
//                    this.fireAllRules(regla, facts);
//
//                    otEvaluada = droolsAccionesService.impactarReglaTipoA(facts);
//
//                }
//
//                otEvaluada.setReglaFlujo(reglaAAplicada);
//                ordenTrabajoDAO.save(otEvaluada);
//
//                // Solo para JUnit: Syso de las actividades
//                actividadesEnOt(otEvaluada);
//            }
//        }
//        return true;
//    }

    
    public boolean recover(RuntimeException e, String agrupador, List<OrdenTrabajoModel> ots) {
        ots.stream().forEach(ot -> {
            errorOtsService.add(2L, ot.getNroOT(), "", e, Constante.USUARIO_SISTEMA);
        });
        return false;
    }

    
    public boolean recover(RuntimeException e, OrdenTrabajoModel ot) {
        errorOtsService.add(2L, ot.getNroOT(), "", e, Constante.USUARIO_SISTEMA);
        return false;
    }

    public void actividadesEnOt(OrdenTrabajoModel ot) {

        List<String> actividades = ot.getActividades().stream().filter(a -> a != null && a.getActivo().equals(Constante.SI))
                .map(OrdenTrabajoActividadModel::getActividadModel).map(ActividadModel::getCodigo)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("\r\n");
        sb.append(" OT: " + ot.getNroOT() + " - ACTIVIDADES:  " + actividades + "\r\n");
        System.out.println(sb.toString());
    }

	
	public ReglaAdapter getReglaAdapter(ReglaTipo tipo, String threadName) {

        String header = getHeader(tipo);
        List<Regla> reglas = getReglas(tipo);
        String reglaCompleta = getReglaCompleta(header, reglas);
        KieContainer kieContainer = getKieContainer(tipo.getHeader(), reglaCompleta, threadName);

        ReglaAdapter adapter = new ReglaAdapter();
        adapter.setTipo(tipo);
        adapter.setHeader(header);
        adapter.setReglas(reglas);
        adapter.setReglaCompleta(reglaCompleta);
        adapter.setKieContainer(kieContainer);

        return adapter;
    }

}



