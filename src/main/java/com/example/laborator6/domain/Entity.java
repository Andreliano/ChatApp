package com.example.laborator6.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1122L;

    private ID id;

    public ID getId() {
        return this.id;
    }

    public void setId(ID otherId) {
        this.id = otherId;
    }

}
