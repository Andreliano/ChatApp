package com.example.laborator6.database;

import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.domain.User;
import com.example.laborator6.friendship.Component;
import com.example.laborator6.friendship.Friend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendShipDataBaseRepository implements Friend<Long, User> {

    private final JDBCUtils jdbcUtils = new JDBCUtils();

    private final UserDataBaseRepository userDataBaseRepository;

    public FriendShipDataBaseRepository(UserDataBaseRepository userDataBaseRepository) {
        this.userDataBaseRepository = userDataBaseRepository;
    }


    @Override
    public User saveFriend(Long idUser, User newFriend, String friendsFrom) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT \"idUser\" FROM users WHERE \"idUser\" IN (?, ?)");

            statement.setLong(1, idUser);

            statement.setLong(2, newFriend.getId());

            ResultSet resultSet = statement.executeQuery();

            int i = 0;

            long userId = 0;
            long idNewFriend = 0;

            while (resultSet.next()) {

                if (i == 0) {
                    userId = resultSet.getLong("idUser");
                } else if (i == 1) {
                    idNewFriend = resultSet.getLong("idUser");
                }
                i++;
            }

            if ((userId == idUser && idNewFriend == newFriend.getId()) || (userId == newFriend.getId() && idNewFriend == idUser)) {
                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM friends WHERE \"idUser\" = ? AND \"idFriend\" = ?");

                statement1.setLong(1, idUser);

                statement1.setLong(2, newFriend.getId());

                ResultSet resultSet1 = statement1.executeQuery();

                if (resultSet1.next()) {
                    return newFriend;
                }

                PreparedStatement statement2 = connection.prepareStatement("INSERT INTO friends VALUES (?, ?, ?)");

                statement2.setLong(1, idUser);
                statement2.setLong(2, newFriend.getId());
                statement2.setString(3, friendsFrom);

                statement2.executeUpdate();

                return null;

            }

            return newFriend;


        } catch (SQLException e) {
            return newFriend;
        }
    }

    public void saveFriendRequest(Long idUser, Long idPotentialFriend) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"friendRequests\" VALUES (?, ?, ?, ?)");

            statement.setLong(1, idUser);
            statement.setLong(2, idPotentialFriend);


            statement.setString(3, "Pending");

            String dateOfSending = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            statement.setString(4, dateOfSending);

            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteEntity(Long idEntity) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM friends WHERE \"idUser\" = ? OR \"idFriend\" = ?");

            statement.setLong(1, idEntity);
            statement.setLong(2, idEntity);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User deleteFriend(Long idUser, User friend) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM friends WHERE \"idUser\" = ? AND \"idFriend\" = ?");

            statement.setLong(1, idUser);
            statement.setLong(2, friend.getId());

            int row = statement.executeUpdate();

            if (row > 0) {
                return friend;
            }

            return null;

        } catch (SQLException e) {
            return null;
        }
    }

    public void deleteFriendRequest(Long idUser, Long idPotentialFriend){
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"friendRequests\" WHERE \"idUser\" = ? AND \"idPotentialFriend\" = ?");

            statement.setLong(1, idUser);
            statement.setLong(2, idPotentialFriend);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long updateStatusFromFriendRequests(Long idUser, Long idPotentialFriend, String newStatus) {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE \"friendRequests\" SET \"status\" = ? WHERE \"idUser\" = ? AND \"idPotentialFriend\" = ?");


            statement.setString(1, newStatus);
            statement.setLong(2, idUser);
            statement.setLong(3, idPotentialFriend);


            int rowCount = statement.executeUpdate();

            if (rowCount == 0) {
                return idPotentialFriend;
            }

            return null;


        } catch (SQLException e) {
            return idPotentialFriend;
        }
    }


    @Override
    public Map<Long, List<User>> getFriends() {
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT \"idUser\", \"idFriend\" FROM friends");

            ResultSet resultSet = statement.executeQuery();

            Map<Long, List<User>> friends = new HashMap<>();

            while (resultSet.next()) {

                Long idUser = resultSet.getLong("idUser");
                Long idFriend = resultSet.getLong("idFriend");

                if (!friends.containsKey(idUser)) {
                    List<User> users = new ArrayList<>();
                    friends.put(idUser, users);
                }

                if (!friends.get(idUser).contains(userDataBaseRepository.getEntities().get(idFriend))) {
                    friends.get(idUser).add(userDataBaseRepository.getEntities().get(idFriend));
                }

            }

            return friends;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<FriendShip<Long>> getFriendsWithDate() {

        List<FriendShip<Long>> friendShips = new ArrayList<>();

        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friends");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long idUser = resultSet.getLong("idUser");
                Long idFriend = resultSet.getLong("idFriend");
                String friendsFrom = resultSet.getString("friendsFrom");

                FriendShip<Long> friendShip = new FriendShip<>(idUser, idFriend, friendsFrom);

                friendShips.add(friendShip);

            }

            return friendShips;


        } catch (SQLException e) {
            return null;
        }
    }

    public List<FriendShip<Long>> getUserFriends(Long idForUser) {
        List<FriendShip<Long>> friendShips = new ArrayList<>();

        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friends WHERE \"idUser\" = ?");

            statement.setLong(1, idForUser);

            ResultSet resSet = statement.executeQuery();

            while (resSet.next()) {
                Long idUser = resSet.getLong("idUser");
                Long idFriend = resSet.getLong("idFriend");
                String friendsFrom = resSet.getString("friendsFrom");

                FriendShip<Long> friendShip = new FriendShip<>(idUser, idFriend, friendsFrom);

                friendShips.add(friendShip);

            }

            return friendShips;


        } catch (SQLException e) {
            return null;
        }
    }

    public List<FriendShip<Long>> getUserFriendRequests(Long idUser) {
        List<FriendShip<Long>> friendRequests = new ArrayList<>();

        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"friendRequests\" WHERE \"idPotentialFriend\" = ?");

            statement.setLong(1, idUser);

            ResultSet resSet = statement.executeQuery();

            while (resSet.next()) {
                Long idU = resSet.getLong("idUser");
                Long idFriend = resSet.getLong("idPotentialFriend");
                String status = resSet.getString("status");
                String dateOfSending = resSet.getString("dateOfSending");

                FriendShip<Long> friendShip = new FriendShip<>(idU, idFriend, status, dateOfSending);

                friendRequests.add(friendShip);

            }

            return friendRequests;


        } catch (SQLException e) {
            return null;
        }

    }

    public boolean updateDeletedFriendShip(boolean value, Long idUser, Long idFriend){
        try{
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE \"friendRequests\" SET \"deletedFriendShip\" = ? WHERE \"idUser\" = ? AND \"idPotentialFriend\" = ?");

            statement.setBoolean(1, value);
            statement.setLong(2, idUser);
            statement.setLong(3, idFriend);

            int rowNumber = statement.executeUpdate();

            return rowNumber > 0;

        }catch (SQLException e){
            return false;
        }
    }

    public List<User> getUsersWhoReceivedAndSentFriendRequest(Long idUser){
        List<User> users = new ArrayList<>();

        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT \"idPotentialFriend\" FROM \"friendRequests\" WHERE \"idUser\" = ? AND \"deletedFriendShip\" = ?");

            statement.setLong(1, idUser);
            statement.setBoolean(2, false);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Long idPotentialFriend = resultSet.getLong("idPotentialFriend");
                users.add(userDataBaseRepository.findOne(idPotentialFriend));
            }

        } catch (SQLException e){
            return null;
        }

        try{
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT \"idUser\" FROM \"friendRequests\" WHERE \"idPotentialFriend\" = ? AND \"deletedFriendShip\" = ?");

            preparedStatement.setLong(1, idUser);
            preparedStatement.setBoolean(2, false);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long idU = resultSet.getLong("idUser");
                users.add(userDataBaseRepository.findOne(idU));
            }

        }
        catch (SQLException e){
            return null;
        }

        return users;

    }

    public List<User> getUsersWhoReceivedFriendRequestWithPendingStatus(Long idUser){
        List<User> users = new ArrayList<>();
        try {
            Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT \"idPotentialFriend\" FROM \"friendRequests\" WHERE \"idUser\" = ? AND \"status\" = ?");

            statement.setLong(1, idUser);
            statement.setString(2, "Pending");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long idPotentialFriend = resultSet.getLong("idPotentialFriend");
                users.add(userDataBaseRepository.findOne(idPotentialFriend));
            }
        }
        catch (SQLException e){
            return null;
        }

        return users;

    }

    @Override
    public int numberOfComponents() {
        int size;
        Map<Long, User> users = userDataBaseRepository.getEntities();
        size = users.size();
        Boolean[][] matrix = new Boolean[size][size];
        buildMatrix(matrix);

        Component<Long, User> component = new Component<>(size, matrix, users);

        return component.Dfs1();
    }

    @Override
    public List<User> longestComponent() {
        Map<Long, User> users = userDataBaseRepository.getEntities();
        int size;
        size = users.size();
        Boolean[][] matrix = new Boolean[size][size];
        buildMatrix(matrix);

        Component<Long, User> component = new Component<>(size, matrix, users);

        List<List<User>> communities = component.Dfs2();

        List<User> biggestCommunity = null;
        int maxim = -1;

        for (List<User> community : communities) {
            if (community.size() > maxim) {
                maxim = community.size();
                biggestCommunity = community;
            }
        }

        return biggestCommunity;
    }

    @Override
    public Iterable<List<User>> findAll() {
        return null;
    }

    private void buildMatrix(Boolean[][] matrix) {
        int size;
        int j, k;

        Map<Long, User> users = userDataBaseRepository.getEntities();

        size = users.size();

        for (j = 0; j < size; j++) {
            for (k = 0; k < size; k++) {
                matrix[j][k] = false;
            }
        }

        Map<Long, Integer> identification = new HashMap<>();
        j = 0;

        for (Map.Entry<Long, User> entry : users.entrySet()) {
            identification.put(entry.getKey(), j);
            j++;
        }

        Map<Long, List<User>> friends = getFriends();

        for (Map.Entry<Long, List<User>> entry : friends.entrySet()) {
            for (User friend : entry.getValue()) {
                matrix[identification.get(entry.getKey())][identification.get(friend.getId())] = true;
            }
        }

    }

}
