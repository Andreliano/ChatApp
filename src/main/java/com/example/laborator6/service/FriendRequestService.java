package com.example.laborator6.service;

import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.User;
import com.example.laborator6.utils.events.ChangeEventType;
import com.example.laborator6.utils.events.FriendRequestChangeEvent;
import com.example.laborator6.utils.observer.Observable;
import com.example.laborator6.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestService implements Observable<FriendRequestChangeEvent> {

    private final List<Observer<FriendRequestChangeEvent>> observers = new ArrayList<>();

    private final FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(new UserDataBaseRepository());
    private List<User> usersWithoutFriendRequest;

    @Override
    public void addObserver(Observer<FriendRequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendRequestChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendRequestChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }

    public List<User> getListOfUsersWithoutFriendRequest(){
        return usersWithoutFriendRequest;
    }

    public void setListOfUsersWithoutFriendRequest(List<User> usersWithoutFriendRequest){
        this.usersWithoutFriendRequest = usersWithoutFriendRequest;
    }

    public void saveFriendRequest(Long idUser, User potentialFriend){
        friendShipDataBaseRepository.saveFriendRequest(idUser, potentialFriend.getId());
        friendShipDataBaseRepository.updateDeletedFriendShip(false, idUser, potentialFriend.getId());
        friendShipDataBaseRepository.updateDeletedFriendShip(false, potentialFriend.getId(), idUser);
        usersWithoutFriendRequest.remove(potentialFriend);
        notifyObservers(new FriendRequestChangeEvent(ChangeEventType.DELETE, potentialFriend));
    }

    public void deleteFriendRequest(Long idUser, User potentialFriend){
        friendShipDataBaseRepository.deleteFriendRequest(idUser, potentialFriend.getId());
        usersWithoutFriendRequest.add(potentialFriend);
        notifyObservers(new FriendRequestChangeEvent(ChangeEventType.ADD, potentialFriend));
    }


}
