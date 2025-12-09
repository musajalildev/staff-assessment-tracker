package com.assessment.tracker.server.unit.entities;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public abstract class EntityTest<T>{

    @Autowired
    protected TestEntityManager entityManager;
    protected T entity;

    @BeforeEach
    void setUp(){
        entity = createEntity();
    }

    protected abstract T createEntity();
    protected abstract Object getId(T entity);

    @Test
    void testPersistence() {
        entityManager.persistAndFlush(entity);

        T loaded = (T) entityManager.find(entity.getClass(), getId(entity));
        assertNotNull(loaded);
    }

    @Test
    void findEntity() {
        entityManager.persistAndFlush(entity);

        T loaded = (T) entityManager.find(entity.getClass(), getId(entity));

        assertNotNull(loaded);
        Assertions.assertEquals(getId(entity), getId(loaded));
    }

    @Test
    void testRemoval() {
        entityManager.persistAndFlush(entity);
        entityManager.clear();

        T loaded = (T) entityManager.find(entity.getClass(), getId(entity));
        assertNotNull(loaded);

        entityManager.remove(entity);
        entityManager.flush();

        T deletedRecord = (T) entityManager.find(entity.getClass(), getId(entity));
        assertNull(deletedRecord);
    }
}
