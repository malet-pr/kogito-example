package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ActividadModel;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@Transactional
@ApplicationScoped
public interface ActividadDAO extends PanacheRepository<ActividadDAO>, ActividadCustomDAO {

	public List<ActividadModel> findByNombreStartsWithIgnoreCaseAndActivo(String nombre, Character activo);
	public List<ActividadModel> findByTelefonia(Character activo);
	public ActividadModel findByActividadIdAndActivo(Long id, Character activo);
	public ActividadModel findByActividadId(Long id);
	public ActividadModel findByCodigo(String string);
	public ActividadModel findByCodigoAndActivo(String string, Character activo);
	public List<ActividadModel> findByCategoria(String categoria);

}