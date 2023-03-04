package com.example.laborator6.service;

import com.example.laborator6.domain.User;
import com.example.laborator6.domain.validators.UserValidator;
import com.example.laborator6.friendship.Friend;
import com.example.laborator6.repository.Repository;

import java.net.InetAddress;

public class UserService {
    private final Repository<Long, User> repository;

    private final Friend<Long, User> friends;
    private long uniqueId;
    private final UserValidator userValidator;

    private final UserVerifier userVerifier = new UserVerifier();

    public UserService(Repository<Long, User> repository, Friend<Long, User> friends, UserValidator userValidator) {
        this.repository = repository;
        this.friends = friends;
        this.userValidator = userValidator;

        Long idMax = 0L;

        for (Long key : repository.getEntities().keySet()) {
            if (key > idMax) {
                idMax = key;
            }
        }

        uniqueId = idMax + 1;
    }

    public void save(User entity) {
        this.userValidator.validate(entity);

        entity.setId(uniqueId);

        User result = repository.save(entity);

        uniqueId++;

        this.userVerifier.verifySave(result);
    }

    public void delete(Long idEntity) {
        User entity = repository.delete(idEntity);
        if (entity != null) {
            friends.deleteEntity(idEntity);
        }

        this.userVerifier.verifyDelete(entity);
    }

    public void update(User entity) {
        this.userValidator.validate(entity);

        User user = repository.update(entity);

        this.userVerifier.verifyUpdate(user);
    }

    public boolean checkEmailExistenceInUsers(String email) {
        return repository.checkEmailExistenceInUsers(email);
    }

    public boolean checkUserAccountExistence(String email, String password) {
        return repository.checkUserAccountExistence(email, password);
    }

    public User findUserByEmail(String emailAddress) {
        return repository.findUserByEmail(emailAddress);
    }

    public User findOne(Long idEntity) {
        return repository.findOne(idEntity);
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }

}
