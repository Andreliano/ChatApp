package com.example.laborator6.friendship;

import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.domain.User;
import com.example.laborator6.repository.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class AbstractFileFriendShip extends RepositoryFriendShip<Long, User> {

    private final String fileName;

    private final InMemoryRepository<Long, User> inMemoryRepository;

    public AbstractFileFriendShip(String fileName, InMemoryRepository<Long, User> inMemoryRepository) {
        super(inMemoryRepository);
        this.fileName = fileName;
        this.inMemoryRepository = inMemoryRepository;
        loadFromFile();
    }

    private void loadFromFile() {
        Long idEntity;
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.length() > 0) {
                    List<String> ids = Arrays.asList(line.split(";"));
                    if (ids.size() == 3) {
                        idEntity = Long.valueOf(ids.get(0));
                        Long idFriend = Long.valueOf(ids.get(1));
                        String friendsFrom = ids.get(2);

                        User friend = inMemoryRepository.getEntities().get(idFriend);
                        User entity = inMemoryRepository.getEntities().get(idEntity);

                        super.saveFriend(idEntity, friend, friendsFrom);
                        super.saveFriend(idFriend, entity, friendsFrom);

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public User saveFriend(Long idEntity, User newFriend, String friendsFrom) {
        User user = super.saveFriend(idEntity, newFriend, friendsFrom);
        if (user == null) {
            appendToFile(idEntity, newFriend.getId(), friendsFrom);
        }
        return user;
    }

    @Override
    public User deleteFriend(Long idEntity, User friend) {
        User f = super.deleteFriend(idEntity, friend);
        if (f != null) {
            deleteFriendShipFromFile(idEntity, friend);
        }
        return f;
    }

    @Override
    public void deleteEntity(Long idEntity) {
        super.deleteEntity(idEntity);
        deleteEntityFromFile(idEntity);
    }

    @Override
    public Iterable<List<User>> findAll() {
        return super.findAll();
    }


    private void appendToFile(Long idEntity, Long idFriend, String friendsFrom) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, true))) {
            buffer.write(idEntity.toString() + ";" + idFriend.toString() + ";" + friendsFrom);
            buffer.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void deleteEntityFromFile(Long idEntity) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName))) {

            for (FriendShip<Long> f : super.getFriendsWithDate()) {
                if (!f.getIdUser1().equals(idEntity) && !f.getIdUser2().equals(idEntity)) {
                    buffer.write(f.getIdUser1() + ";" + f.getIdUser2() + ";" + f.getFriendsFrom());
                    buffer.newLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void deleteFriendShipFromFile(Long idEntity, User friend) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName))) {

            for (FriendShip<Long> f : super.getFriendsWithDate()) {
                if (!((f.getIdUser1().equals(idEntity) && f.getIdUser2().equals(friend.getId())) || (f.getIdUser2().equals(idEntity) && f.getIdUser1().equals(friend.getId())))) {
                    buffer.write(f.getIdUser1() + ";" + f.getIdUser2() + ";" + f.getFriendsFrom());
                    buffer.newLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
