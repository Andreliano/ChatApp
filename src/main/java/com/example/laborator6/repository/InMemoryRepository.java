package com.example.laborator6.repository;

import com.example.laborator6.domain.Entity;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Map<ID, E> entities;

    public InMemoryRepository() {
        this.entities = new HashMap<>();
    }

    @Override
    public E save(E entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity must be not null!");
        }

        // if entity already is in entities
        if (entities.get(entity.getId()) != null)
            return entity;
        else
            entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID idEntity) {
        if (idEntity == null) {
            throw new IllegalArgumentException("Id must be not null!");
        }
        if (entities.containsKey(idEntity))
            return entities.remove(idEntity);
        return null;
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null!");

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }

        return entity;
    }

//    @Override
//    public List<E> findByIdEven() {
//        List<E> users = new ArrayList<>();
//        for (Map.Entry<ID, E> entry : getEntities().entrySet()) {
//            String id = entry.getKey().toString();
//            String lastDigit = id.substring(id.length() - 1);
//            if (lastDigit.charAt(0) == '0' || lastDigit.charAt(0) == '2' || lastDigit.charAt(0) == '4' || lastDigit.charAt(0) == '6' || lastDigit.charAt(0) == '8') {
//                users.add(entry.getValue());
//            }
//        }
//        return users;
//    }
//
//    @Override
//    public List<E> findByIdOdd() {
//        List<E> users = new ArrayList<>();
//        for (Map.Entry<ID, E> entry : getEntities().entrySet()) {
//            String id = entry.getKey().toString();
//            String lastDigit = id.substring(id.length() - 1);
//            if (lastDigit.charAt(0) == '1' || lastDigit.charAt(0) == '3' || lastDigit.charAt(0) == '5' || lastDigit.charAt(0) == '7' || lastDigit.charAt(0) == '9') {
//                users.add(entry.getValue());
//            }
//        }
//        return users;
//    }

    @Override
    public E findOne(ID idEntity) {
        if (idEntity == null)
            throw new IllegalArgumentException("Id must be not null");
        return entities.get(idEntity);
    }

    @Override
    public E findUserByEmail(String emailAddress) {
        return null;
    }

    @Override
    public boolean checkEmailExistenceInUsers(String email) {
        return false;
    }

    @Override
    public boolean checkUserAccountExistence(String email, String password) {
        return false;
    }


    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Map<ID, E> getEntities() {
        return this.entities;
    }

}
