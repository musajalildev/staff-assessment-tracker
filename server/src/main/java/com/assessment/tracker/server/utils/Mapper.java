package com.assessment.tracker.server.utils;

public interface Mapper<A, E> {
    A entityToApi(E entity);
    E apiToEntity(A a);
}