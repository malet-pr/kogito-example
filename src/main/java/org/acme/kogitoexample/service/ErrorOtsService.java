package org.acme.kogitoexample.service;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.acme.kogitoexample.model.ErrorOts;
import org.acme.kogitoexample.model.OrdenTrabajoModel;
import org.acme.kogitoexample.repository.ErrorOtsDao;
import org.acme.kogitoexample.repository.OrdenTrabajoDAO;
import org.acme.kogitoexample.repository.ParGlobalDAO;
import org.acme.kogitoexample.util.Constante;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@ApplicationScoped
public class ErrorOtsService {
    @Inject
    private ErrorOtsDao errorOtsDao;
    @Inject
    private OrdenTrabajoDAO otDAO;
    @Inject
    private ErrorOtsService errorOtsService;
    @Inject
    private DroolsRuleService drservice;
    @Inject
    private ParGlobalDAO pgDAO;


    private static final Logger log = LoggerFactory.getLogger(ErrorOtsService.class);

    public void add(Long idReglaTipo, String nroOt, String domiDireccionCompleta, RuntimeException error, String usuario) {
        List<ErrorOts> errorOts = !nroOt.equals("-") ? errorOtsDao.findErrorOtsActiveAndNroOt(nroOt, Constante.NOK).orElse(Arrays.asList(new ErrorOts())) :
                errorOtsDao.findErrorOtsActiveAndDomiDireccionCompleta(domiDireccionCompleta, Constante.NOK).orElse(Arrays.asList(new ErrorOts()));
        ErrorOts errorOt = errorOts.get(0);
        errorOt.setMensajeError(controlMensajeError(messageException(error)));
        errorOt.setCantReintentos(errorOt.getCantReintentos() == null || errorOt.getCantReintentos() == 0 ? 1 : errorOt.getCantReintentos() + 1);
        errorOt.setStatus(Constante.NOK);
        errorOt.setIdReglaTipo(idReglaTipo);
        errorOt.setNroOt(nroOt);
        errorOtsDao.save(errorOt);
    }

    public List<ErrorOts> getErrorOtsReprocesos() {
        return errorOtsDao.findErrorOtsActive(Constante.NOK).orElse(null);
    }

    public void reprocesoOk(String nroOt) {
        List<ErrorOts> errorOts = errorOtsDao.findErrorOtsActiveAndNroOt(nroOt, Constante.NOK).orElse(null);

        if (errorOts != null && errorOts.size() > 0) {
//            errorOts.get(0);
            System.out.println("OT:  "+errorOts.get(0).getNroOt());
            errorOts.get(0).setStatus(Constante.OK);
            errorOtsDao.save(errorOts.get(0));
        }
    }

    protected String controlMensajeError(String mensajeError) {
        if (mensajeError != null && mensajeError.length() > 0)
            return mensajeError.substring(0, mensajeError.length() <= 900 ? mensajeError.length() : 1000 - 100);
        return "";
    }

    private Date calculationTimeOutStopReglasA(String nameParGlobal) {
        String tiempoCorteParam = pgDAO.findByNombre(nameParGlobal).getValor();
        System.out.println("tiempoCorteParam:   "+tiempoCorteParam);
        return calculoFechaBusqueda(Integer.valueOf(tiempoCorteParam));
    }
    protected Date calculoFechaBusqueda(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public String messageException(RuntimeException e){
        return e.getMessage()!=null ? e.getMessage() : e.getStackTrace()!=null && e.getStackTrace().length>0 ? e.getStackTrace()[0].toString() : e.getStackTrace().toString() ;
    }
}
