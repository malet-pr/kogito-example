package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ErrorOts;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public interface ErrorOtsDao extends PanacheRepository<ErrorOts> {

    @Query("SELECT e FROM ErrorOts e WHERE e.nroOt = :nroOt AND e.status = :status")
    Optional<List<ErrorOts>> findErrorOtsActiveAndNroOt(@Param("nroOt") String nroOt, @Param("status") String status);

    @Query("SELECT e FROM ErrorOts e WHERE e.domiDireccionCompleta = :domiDireccionCompleta AND e.status = :status")
    Optional<List<ErrorOts>> findErrorOtsActiveAndDomiDireccionCompleta(@Param("domiDireccionCompleta") String domiDireccionCompleta, @Param("status") String status);

    @Query("SELECT e FROM ErrorOts e WHERE e.status = :status")
    Optional<List<ErrorOts>> findErrorOtsActive(@Param("status") String status);

    void save(ErrorOts errorOt);
}
