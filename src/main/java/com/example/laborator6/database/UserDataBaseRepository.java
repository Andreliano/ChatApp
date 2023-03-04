package com.example.laborator6.database;

import com.example.laborator6.domain.ConnectionInfo;
import com.example.laborator6.domain.User;
import com.example.laborator6.repository.Repository;
import com.example.laborator6.utils.PasswordEncryption;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class UserDataBaseRepository implements Repository<Long, User> {

    JDBCUtils jdbcUtils = new JDBCUtils();

    public UserDataBaseRepository() {
    }


    @Override
    public User save(User entity) {

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?)")) {

            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, PasswordEncryption.toHexString(PasswordEncryption.getSHA(entity.getPassword())));

            statement.executeUpdate();

            return null;

        } catch (SQLException | NoSuchAlgorithmException e) {
            return entity;
        }
    }

    @Override
    public User delete(Long idEntity) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE \"idUser\" = ?");

            statement.setLong(1, idEntity);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                User user = new User(firstName, lastName);
                user.setId(idUser);

                String queryForDeleteRecord = "DELETE FROM users WHERE \"idUser\" = ?";

                PreparedStatement statement1 = connection.prepareStatement(queryForDeleteRecord);
                statement1.setLong(1, idEntity);
                statement1.executeUpdate();

                return user;
            }
            return null;

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User update(User entity) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET \"firstName\" = ?, \"lastName\" = ? WHERE \"idUser\" = ?");

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());

            int rowCount = statement.executeUpdate();

            if (rowCount == 0) {
                return entity;
            }

            return null;


        } catch (SQLException e) {
            return entity;
        }
    }

    @Override
    public User findOne(Long idEntity) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE \"idUser\" = ?");

            statement.setLong(1, idEntity);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                User user = new User(firstName, lastName);
                user.setId(idUser);

                return user;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean checkUserAccountExistence(String email, String password) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE \"email\" = ? AND \"password\" = ?");

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public User findUserByEmail(String emailAddress) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE \"email\" = ?");

            statement.setString(1, emailAddress);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");


                User user = new User(firstName, lastName, email, password);
                user.setId(idUser);

                return user;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean checkEmailExistenceInUsers(String email){
        try{
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT email FROM users WHERE \"email\" = ?");

            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Iterable<User> findAll() {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");

            ResultSet resultSet = statement.executeQuery();

            Set<User> users = new HashSet<>();

            while (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                User user = new User(firstName, lastName);
                user.setId(idUser);

                users.add(user);

            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<Long, User> getEntities() {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");

            ResultSet resultSet = statement.executeQuery();

            Map<Long, User> entities = new HashMap<>();

            while (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                User user = new User(firstName, lastName);
                user.setId(idUser);

                entities.put(idUser, user);

            }

            return entities;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
