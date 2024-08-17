package org.acme.testEntity;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestEntityResource {

    @Inject
    TestEntityService testEntityService;

    @GET
    @Path("entity")
    public List<TestEntity> getAllEntities() {
       return testEntityService.findAll();
    }

}
