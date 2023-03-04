package com.example.laborator6.friendship;

import com.example.laborator6.domain.Entity;
import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryFriendShip<ID, E extends Entity<ID>> implements Friend<ID, E> {

    private final Map<ID, List<E>> friends;
    private final InMemoryRepository<ID, E> inMemoryRepository;

    private final List<FriendShip<ID>> friendsWithDate;

    public RepositoryFriendShip(InMemoryRepository<ID, E> inMemoryRepository) {
        this.friends = new HashMap<>();
        this.inMemoryRepository = inMemoryRepository;
        friendsWithDate = new ArrayList<>();
    }

    @Override
    public void deleteEntity(ID idEntity) {
        friends.remove(idEntity);
    }

    @Override
    public E saveFriend(ID idEntity, E newFriend, String friendsFrom) {
        if (idEntity == null || newFriend == null)
            return newFriend;

        if (idEntity != newFriend.getId()) {
            if (inMemoryRepository.getEntities().containsKey(idEntity) && inMemoryRepository.getEntities().containsKey(newFriend.getId())) {
                if (!friends.containsKey(idEntity)) {
                    List<E> users1 = new ArrayList<>();
                    friends.put(idEntity, users1);
                }
                if (!friends.get(idEntity).contains(newFriend)) {
                    friends.get(idEntity).add(newFriend);
                    friendsWithDate.add(new FriendShip<>(idEntity, newFriend.getId(), friendsFrom));
                    return null;
                }
            }
        }
        return newFriend;
    }

    @Override
    public E deleteFriend(ID idEntity, E friend) {
        if (idEntity == null || friend == null)
            return null;

        // friend - if idEntity and newFriend exist in entities list and friends list
        // null else

        if (idEntity != friend.getId()) {
            if (inMemoryRepository.getEntities().containsKey(idEntity) && inMemoryRepository.getEntities().containsKey(friend.getId())) {
                if (friends.containsKey(idEntity) && friends.get(idEntity).contains(friend)) {
                    friends.get(idEntity).remove(friend);

                    for (int i = 0; i < friendsWithDate.size(); i++) {
                        if (friendsWithDate.get(i).getIdUser1().equals(idEntity) && friendsWithDate.get(i).getIdUser2().equals(friend.getId())) {
                            friendsWithDate.remove(i);
                            break;
                        }
                    }

                    return friend;
                }
            }
        }

        return null;

    }

    @Override
    public Iterable<List<E>> findAll() {
        return friends.values();
    }

    @Override
    public Map<ID, List<E>> getFriends() {
        return this.friends;
    }

    @Override
    public List<FriendShip<ID>> getFriendsWithDate() {
        return this.friendsWithDate;
    }

    @Override
    public int numberOfComponents() {
        int size;
        size = inMemoryRepository.getEntities().size();
        Boolean[][] matrix = new Boolean[size][size];
        buildMatrix(matrix);

        Component<ID, E> component = new Component<>(size, matrix, inMemoryRepository.getEntities());

        return component.Dfs1();
    }

    @Override
    public List<E> longestComponent() {
        int size;
        size = inMemoryRepository.getEntities().size();
        Boolean[][] matrix = new Boolean[size][size];
        buildMatrix(matrix);

        Component<ID, E> component = new Component<>(size, matrix, inMemoryRepository.getEntities());

        List<List<E>> communities = component.Dfs2();

        List<E> biggestCommunity = null;
        int maxim = -1;

        for (List<E> community : communities) {
            if (community.size() > maxim) {
                maxim = community.size();
                biggestCommunity = community;
            }
        }

        return biggestCommunity;
    }

    private void buildMatrix(Boolean[][] matrix) {
        int i, j;
        int size;
        size = inMemoryRepository.getEntities().size();

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                matrix[i][j] = false;
            }
        }

        Map<ID, Integer> identification = new HashMap<>();
        i = 0;

        for (Map.Entry<ID, E> entry : inMemoryRepository.getEntities().entrySet()) {
            identification.put(entry.getKey(), i);
            i++;
        }

        for (Map.Entry<ID, List<E>> entry : friends.entrySet()) {
            for (E friend : entry.getValue()) {
                matrix[identification.get(entry.getKey())][identification.get(friend.getId())] = true;
            }
        }

    }

}
