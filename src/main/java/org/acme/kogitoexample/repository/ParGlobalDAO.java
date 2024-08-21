package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ParGlobal;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ParGlobalDAO extends PanacheRepository<ParGlobal> {
	public ParGlobal findByNombre(String nombre);
}
