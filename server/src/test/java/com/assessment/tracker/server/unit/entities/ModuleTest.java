package com.assessment.tracker.server.unit.entities;

import com.assessment.tracker.server.persistence.entities.Module;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ModuleTest extends EntityTest<Module>{

    @Override
    protected Module createEntity() {
        return new Module();
    }

    @Override
    protected Object getId(Module entity) {
        return entity.getID();
    }
}
