package com.example.laborator6.domain;

public class ConnectionInfo {

    private final Long idUser;

    private final String IP;

    private final String dateOfConnection;

    private final String dateOfSignOut;

    public ConnectionInfo(Long idUser, String IP, String dateOfConnection, String dateOfSignOut){
        this.idUser = idUser;
        this.IP = IP;
        this.dateOfConnection = dateOfConnection;
        this.dateOfSignOut = dateOfSignOut;
    }

    public Long getIdUser() {
        return idUser;
    }

    public String getIP() {
        return IP;
    }

    public String getDateOfConnection() {
        return dateOfConnection;
    }

    public String getDateOfSignOut() { return dateOfSignOut; }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "idUser=" + idUser +
                ", IP='" + IP + '\'' +
                ", dateOfConnection='" + dateOfConnection + '\'' +
                '}';
    }
}
