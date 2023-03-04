package com.example.laborator6.friendship;

import com.example.laborator6.domain.Entity;
import com.example.laborator6.domain.FriendShip;

import java.util.List;
import java.util.Map;

public interface Friend<ID, E extends Entity<ID>> {

    E saveFriend(ID idUser, E newFriend, String friendsFrom);

    void deleteEntity(ID idEntity);

    E deleteFriend(ID idUser, E friend);

    Iterable<List<E>> findAll();

    Map<ID, List<E>> getFriends();

    List<FriendShip<ID>> getFriendsWithDate();

    int numberOfComponents();

    public List<E> longestComponent();

}
