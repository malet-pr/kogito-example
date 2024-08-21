package sampleCode.testEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TestEntityService {

    @Inject
    TestEntityRepository testEntityRepository;

    public List<TestEntity> findAll() {
        return testEntityRepository.listAll();
    }

}




