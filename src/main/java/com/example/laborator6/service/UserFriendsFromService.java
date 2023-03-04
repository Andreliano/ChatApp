package com.example.laborator6.service;

import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.User;
import com.example.laborator6.domain.UserFriendsFrom;
import com.example.laborator6.utils.events.ChangeEventType;
import com.example.laborator6.utils.events.UserFriendsFromEntityChangeEvent;
import com.example.laborator6.utils.observer.Observable;
import com.example.laborator6.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class UserFriendsFromService implements Observable<UserFriendsFromEntityChangeEvent> {

    private final List<Observer<UserFriendsFromEntityChangeEvent>> observers = new ArrayList<>();

    private final UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();
    private final FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(userDataBaseRepository);
    private List<UserFriendsFrom> userFriendsFromList;

    public UserFriendsFromService(){
    }

    @Override
    public void addObserver(Observer<UserFriendsFromEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserFriendsFromEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserFriendsFromEntityChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }

    public List<UserFriendsFrom> getUserFriendsFromList(){
        return this.userFriendsFromList;
    }

    public void deleteFriendFromDataBase(Long idUser, User friend){
        UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();
        User user = friendShipDataBaseRepository.deleteFriend(idUser, friend);
        friendShipDataBaseRepository.updateDeletedFriendShip(true, idUser, friend.getId());
        friendShipDataBaseRepository.deleteFriend(friend.getId(), userDataBaseRepository.findOne(idUser));
        friendShipDataBaseRepository.updateDeletedFriendShip(true, friend.getId(), idUser);
        String friendsFromDate = getFriendsFromDate(user.getId());
        UserFriendsFrom userFriendsFrom = new UserFriendsFrom(user, friendsFromDate);
        deleteFriendFromList(friend.getId());
        notifyObservers(new UserFriendsFromEntityChangeEvent(ChangeEventType.DELETE, userFriendsFrom));
    }

    public void saveFriendInDataBase(Long idUser, User friend, String friendsFrom){
        UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();
        User user = userDataBaseRepository.findOne(idUser);
        friendShipDataBaseRepository.saveFriend(idUser, friend, friendsFrom);
        friendShipDataBaseRepository.saveFriend(friend.getId(), user, friendsFrom);
        UserFriendsFrom userFriendsFrom = new UserFriendsFrom(friend, friendsFrom);
        this.userFriendsFromList.add(userFriendsFrom);
        notifyObservers(new UserFriendsFromEntityChangeEvent(ChangeEventType.ADD, userFriendsFrom));
    }

    public void setUserFriendsFromList(List<UserFriendsFrom> userFriendsFromList){
        this.userFriendsFromList = userFriendsFromList;
    }

    private void deleteFriendFromList(Long idFriend){
        for(UserFriendsFrom userFriendsFrom : this.userFriendsFromList){
            if(userFriendsFrom.getId().equals(idFriend)){
                userFriendsFromList.remove(userFriendsFrom);
                break;
            }
        }
    }

    private String getFriendsFromDate(Long idFriend){
        for(UserFriendsFrom userFriendsFrom : userFriendsFromList){
            if(userFriendsFrom.getId().equals(idFriend)){
                return userFriendsFrom.getFriendsFrom();
            }
        }
        return null;
    }

}
