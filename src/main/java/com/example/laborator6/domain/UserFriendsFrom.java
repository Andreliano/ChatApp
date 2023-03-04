package com.example.laborator6.domain;

import java.util.Objects;

public class UserFriendsFrom extends Entity<Long> {

    private final User user;

    private final String friendsFrom;

    public UserFriendsFrom(User user, String friendsFrom) {
        this.user = user;
        this.friendsFrom = friendsFrom;
    }

    public User getUser(){
        return user;
    }

    public Long getId(){
        return user.getId();
    }

    public void setId(Long otherI){
        user.setId(otherI);
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    public String getLastName() {
        return user.getLastName();
    }

    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
       user.setPassword(password);
    }

    public String getFriendsFrom(){
        return this.friendsFrom;
    }

    @Override
    public String toString() {
        return "User: {\"" + user.getId() + '\"' + ", \"" + user.getFirstName() + '\"' + ", \"" + user.getLastName() + '\"' + ", \"" + friendsFrom + '\"' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFriendsFrom user = (UserFriendsFrom) o;
        //return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName);
        return Objects.equals(user.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId());
    }

}
