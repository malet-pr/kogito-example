package org.acme.kogitoexample.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.kogitoexample.model.ReglaLog;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public interface ReglaLogDAO extends PanacheRepository<ReglaLog> {

    void saveAll(List<ReglaLog> logs);
}
