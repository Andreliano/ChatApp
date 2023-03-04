package com.example.laborator6.database;

import com.example.laborator6.domain.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDataBaseRepository {

    private final JDBCUtils jdbcUtils = new JDBCUtils();

    public Message saveMessage(Message message){
        try (Connection connection = jdbcUtils.getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO messages VALUES (?, ?, ?)")){

            statement.setString(1, message.getMessage());
            statement.setLong(2, message.getSentBy());
            statement.setLong(3, message.getSentTo());

            statement.executeUpdate();

            return null;

        } catch (SQLException e){
            return message;
        }
    }

    public List<Message> getMessages(){
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages");

            ResultSet resultSet = statement.executeQuery();

            List<Message> messages = new ArrayList<>();

            while (resultSet.next()) {

                String subject = resultSet.getString("message");
                Long sentBy = resultSet.getLong("sentBy");
                Long sentTo = resultSet.getLong("sentTo");

                Message message = new Message(subject, sentBy, sentTo);

                messages.add(message);

            }

            return messages;

        } catch (SQLException e) {
            return null;
        }

    }


}
