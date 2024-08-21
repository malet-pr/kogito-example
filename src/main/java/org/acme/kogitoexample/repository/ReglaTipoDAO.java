package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ReglaTipo;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;


@ApplicationScoped
public interface ReglaTipoDAO extends PanacheRepository<ReglaTipo> {
	
	public ReglaTipo findByNombreIgnoreCase(String nombre);
	public ReglaTipo findByNombreCortoIgnoreCase(String nombreCorto);
	public List<ReglaTipo> findByVisible(Character visible);
	public List<ReglaTipo> findByAgrupadorOrderByOrdenAsc(String agrupador);

}
