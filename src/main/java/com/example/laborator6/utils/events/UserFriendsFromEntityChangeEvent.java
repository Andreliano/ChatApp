package com.example.laborator6.utils.events;


import com.example.laborator6.domain.UserFriendsFrom;

public class UserFriendsFromEntityChangeEvent implements Event {
    private final ChangeEventType type;
    private final UserFriendsFrom data;
    private UserFriendsFrom oldData;

    public UserFriendsFromEntityChangeEvent(ChangeEventType type, UserFriendsFrom data) {
        this.type = type;
        this.data = data;
    }

    public UserFriendsFromEntityChangeEvent(ChangeEventType type, UserFriendsFrom data, UserFriendsFrom oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public UserFriendsFrom getData() {
        return data;
    }

    public UserFriendsFrom getOldData() {
        return oldData;
    }
}