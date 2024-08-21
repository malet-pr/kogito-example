package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ReglaFlujo;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ReglaFlujoDAO extends PanacheRepository<ReglaFlujo> {
	
	public ReglaFlujo findByNombreIgnoreCase(String nombre);
	public ReglaFlujo findByNombreCortoIgnoreCase(String nombreCorto);

}
