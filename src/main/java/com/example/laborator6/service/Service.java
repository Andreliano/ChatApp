package com.example.laborator6.service;

import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.domain.User;
import com.example.laborator6.domain.validators.UserValidator;
import com.example.laborator6.friendship.Friend;
import com.example.laborator6.repository.Repository;

import java.util.List;
import java.util.Map;

public class Service {

    private final Repository<Long, User> repository;

    private long uniqueId;
    private final UserValidator userValidator;

    private final UserVerifier userVerifier = new UserVerifier();
    private final Friend<Long, User> friends;

    public Service(Repository<Long, User> repository, Friend<Long, User> friends, UserValidator userValidator) {
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

    public void saveFriend(Long idEntity, User newFriend, String friendsFrom) {
        User result = friends.saveFriend(idEntity, newFriend, friendsFrom);
        userVerifier.verifyEntitiesFromFriendShip(repository.getEntities().get(idEntity), repository.getEntities().get(newFriend.getId()));
        userVerifier.verifySaveFriend(result, newFriend);
        friends.saveFriend(newFriend.getId(), repository.getEntities().get(idEntity), friendsFrom);
    }

    public void deleteFriend(Long idEntity, User friend) {
        User result = friends.deleteFriend(idEntity, friend);
        userVerifier.verifyEntitiesFromFriendShip(repository.getEntities().get(idEntity), repository.getEntities().get(friend.getId()));
        userVerifier.verifyDeleteFriend(result);
        friends.deleteFriend(friend.getId(), repository.getEntities().get(idEntity));
    }

    public int numberOfComponents() {
        return friends.numberOfComponents();
    }

    public List<User> longestComponent() {
        return friends.longestComponent();
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }

    public User findOne(Long idEntity) {
        User entity = repository.findOne(idEntity);
        userVerifier.verifyFindOne(entity);
        return entity;
    }

    public Map<Long, List<User>> getFriends() {
        return friends.getFriends();
    }

    public List<FriendShip<Long>> getFriendsWithDate() {
        return friends.getFriendsWithDate();
    }

    public Map<Long, User> getEntities() {
        return repository.getEntities();
    }
}
