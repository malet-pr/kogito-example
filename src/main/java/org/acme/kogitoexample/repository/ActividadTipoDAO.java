package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ActividadTipo;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ActividadTipoDAO extends PanacheRepository<ActividadTipo> {
	
	public ActividadTipo findByNombreAndNombreCorto(String nombre, String nombreCorto);
	public ActividadTipo findByNombreCorto(String string);
}
