package com.example.laborator6.domain;

import java.util.Objects;

public class FriendRequest {

    private final String firstName;

    private final String lastName;

    private final String status;

    private final String dateOfSending;

    private final Long idUser;

    public FriendRequest(Long idUser, String firstName, String lastName, String status, String dateOfSending){
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.dateOfSending = dateOfSending;
    }

    public Long getIdUser() {
        return idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public String getDateOfSending() {
        return dateOfSending;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", dateOfSending='" + dateOfSending + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(status, that.status) && Objects.equals(dateOfSending, that.dateOfSending);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, status, dateOfSending);
    }
}
