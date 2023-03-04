package com.example.laborator6.utils.events;

import com.example.laborator6.domain.User;

public class UserEntityChangeEvent implements Event {
    private final ChangeEventType type;
    private final User data;
    private User oldData;

    public UserEntityChangeEvent(ChangeEventType type, User data) {
        this.type = type;
        this.data = data;
    }

    public UserEntityChangeEvent(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
