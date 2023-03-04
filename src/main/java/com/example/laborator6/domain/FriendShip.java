package com.example.laborator6.domain;

public class FriendShip<ID> extends Entity<ID> {

    private final ID idUser1;
    private final ID idUser2;
    private String friendsFrom;

    private String status;

    private String dateOfSending;

    public FriendShip(ID idUser1, ID idUser2, String friendsFrom) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendsFrom = friendsFrom;
    }

    public FriendShip(ID idUser1, ID idUser2, String status, String dateOfSending){
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
        this.dateOfSending = dateOfSending;
    }

    public String getStatus() {
        return status;
    }

    public String getDateOfSending() {
        return dateOfSending;
    }

    public ID getIdUser1() {
        return idUser1;
    }

    public ID getIdUser2() {
        return idUser2;
    }

    public String getFriendsFrom() {
        return friendsFrom;
    }
}
