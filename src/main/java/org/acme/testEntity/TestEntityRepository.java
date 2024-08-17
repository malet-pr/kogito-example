package org.acme.testEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class TestEntityRepository implements PanacheRepository<TestEntity> {

}