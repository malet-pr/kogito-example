package org.acme.kogitoexample.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.OrdenTrabajoModel;

@Transactional
@ApplicationScoped
public interface OrdenTrabajoDAO  extends PanacheRepository<OrdenTrabajoModel> {

	public List<OrdenTrabajoModel> findByNroOTIn(List<String>  nroOTs);	

	@Query("SELECT o FROM OrdenTrabajoModel o WHERE o.nroOT = :nroOT")
	public Optional<OrdenTrabajoModel> findByNroOTOptional(@Param("nroOT") String nroOT);

	@Query("SELECT o FROM OrdenTrabajoModel o WHERE o.nroOT in :nroOTs AND o.excluirOtActa = :excluida")
	public List<OrdenTrabajoModel> findByNroOTs(@Param("nroOTs")List<String> ordenesTrabajo, @Param("excluida") Character excluida);

	public List<OrdenTrabajoModel> findByIdIn(List<Long> id);

	@Query("SELECT o FROM OrdenTrabajoModel o WHERE o.reglaFlujo.reglaFlujoId = :idReglaFlujo AND o.fechaCreacion <= :fechaCreacion")
	public Page<OrdenTrabajoModel> findByOTsInitialPage(@Param("idReglaFlujo") Long reglaFlujoId, @Param("fechaCreacion") Date fechaCreacion, Pageable pageRequest);//99

	@Query("SELECT o.nroOT FROM OrdenTrabajoModel o WHERE o.reglaFlujo.reglaFlujoId = :idReglaFlujo AND o.fechaCreacion <= :fechaCreacion ORDER BY id ASC")
	public Page<String> findByOTsInitialPage1(@Param("idReglaFlujo") Long reglaFlujoId, @Param("fechaCreacion") Date fechaCreacion, Pageable pageRequest);//99

	@Query("SELECT o FROM OrdenTrabajoModel o WHERE o.reglaFlujo.reglaFlujoId = :idReglaFlujo AND o.fechaCreacion <= :fechaCreacion")
	Optional<List<OrdenTrabajoModel>> findByOTsInitialOptional(@Param("idReglaFlujo") Long reglaFlujoId, @Param("fechaCreacion") Date fechaCreacion);//99

	@Query("SELECT count(o) FROM OrdenTrabajoModel o WHERE o.reglaFlujo.reglaFlujoId = :idReglaFlujo AND o.fechaCreacion <= :fechaCreacion")
	Long findByOTsInitialCant(@Param("idReglaFlujo") Long reglaFlujoId, @Param("fechaCreacion") Date fechaCreacion);
	
	public List<OrdenTrabajoModel> findTop100ByReglaFlujo_ReglaFlujoIdAndFechaCreacionLessThanEqual(Long reglaFlujo, Date fechaCreacion);

	public Long countByReglaFlujo_ReglaFlujoIdAndFechaCreacionLessThanEqual(long code, Date fechaCreacion);

	public List<OrdenTrabajoModel> findTop100ByReglaFlujo_ReglaFlujoIdAndFechaCreacionLessThanEqualOrderByFechaCreacionAsc(Long reglaFlujo, Date fechaCreacion);

}


