package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.Regla;
import org.acme.kogitoexample.model.ReglaTipo;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public interface ReglaDAO extends PanacheRepository<Regla> {

	public List<Regla> findByNombreIgnoreCaseAndActivo(String nombre, Character activo);
	public List<Regla> findByReglaTipoAndActivo(ReglaTipo tipo, Character activo);
	public List<Regla> findByReglaTipoAndNombreIgnoreCaseAndActivo(ReglaTipo tipo, String nombre, Character activo);
	public List<Regla> findByReglaTipoAndNombreIgnoreCase(ReglaTipo tipo, String nombre);
	public List<Regla> findByActivo(Character activo);

}
