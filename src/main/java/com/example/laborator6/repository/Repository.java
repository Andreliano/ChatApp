package com.example.laborator6.repository;

import com.example.laborator6.domain.Entity;

import java.util.Map;

public interface Repository<ID, E extends Entity<ID>> {

    E save(E entity);

    E delete(ID idEntity);

    E update(E entity);

    E findOne(ID idEntity);

    E findUserByEmail(String emailAddress);

    boolean checkEmailExistenceInUsers(String email);

    boolean checkUserAccountExistence(String email, String password);

    Iterable<E> findAll();

    Map<ID, E> getEntities();


}
