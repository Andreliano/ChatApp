package com.example.laborator6.database;

import com.example.laborator6.domain.ConnectionInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionInfoDataBaseRepository {

    JDBCUtils jdbcUtils = new JDBCUtils();

    public Long saveConnectionInformation(Long idEntity, String IP, String dateOfConnection, String dateOfSignOut){
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"connectionInformation\" VALUES (?, ?, ?, ?)")) {

            statement.setLong(1, idEntity);
            statement.setString(2, IP);
            statement.setString(3, dateOfConnection);
            statement.setString(4, dateOfSignOut);

            statement.executeUpdate();

            return null;

        } catch (SQLException e){

            try (Connection connection = jdbcUtils.getConnection();
                 PreparedStatement statement = connection.prepareStatement("UPDATE \"connectionInformation\" SET ip = ?, \"dateOfConnection\" = ?, \"dateOfSignOut\" = ? WHERE \"idUser\" = ?")) {

                statement.setString(1, IP);
                statement.setString(2, dateOfConnection);
                statement.setString(3, dateOfSignOut);
                statement.setLong(4, idEntity);

                statement.executeUpdate();

            } catch (SQLException s){
                return idEntity;
            }

            return null;
        }
    }

    public Long updateDateOfSignOut(Long idUser, String dateOfSignOut){
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE \"connectionInformation\" SET \"dateOfSignOut\" = ? WHERE \"idUser\" = ?")) {

            statement.setString(1, dateOfSignOut);
            statement.setLong(2, idUser);

            statement.executeUpdate();

        } catch (SQLException s){
            return idUser;
        }

        return null;

    }

    public List<ConnectionInfo> getConnectionInfo(){
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"connectionInformation\"");

            ResultSet resultSet = statement.executeQuery();

            List<ConnectionInfo> connectionInformation = new ArrayList<>();

            while (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                String IP = resultSet.getString("ip");
                String dateOfConnection = resultSet.getString("dateOfConnection");
                String dateOfSignOut = resultSet.getString("dateOfSignOut");

                ConnectionInfo connectionInfo = new ConnectionInfo(idUser, IP.substring(1), dateOfConnection, dateOfSignOut);

                connectionInformation.add(connectionInfo);

            }

            return connectionInformation;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
