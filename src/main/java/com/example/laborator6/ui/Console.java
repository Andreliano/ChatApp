package com.example.laborator6.ui;

import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.domain.User;
import com.example.laborator6.domain.validators.ValidationException;
import com.example.laborator6.service.Service;
import com.example.laborator6.service.ServiceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Console {


    private long idUser;
    private User friend;
    private final Service service;
    private final Scanner scanner = new Scanner(System.in);

    public Console(Service service) {
        this.service = service;
    }

    public void menu() {
        String command;

        System.out.println("add - Add a new user in list");
        System.out.println("delete - Delete a user from list");
        System.out.println("update - Update a user from list");
        System.out.println("findOne - Find a user from list");
        System.out.println("users - Print all users from list");
        System.out.println("addFriend - Add a new friendship between two users");
        System.out.println("deleteFriend - Delete the friendship between two users");
        System.out.println("friends - Print all friendships");
        System.out.println("communities - Print number of connected components");
        System.out.println("biggestCommunity - Print longest connected component");

        label:
        while (true) {
            System.out.print("Read command: ");
            command = scanner.nextLine();

            switch (command) {
                case "add":
                    saveUser();
                    break;
                case "delete":
                    deleteUser();
                    break;
                case "update":
                    updateUser();
                    break;
                case "findOne":
                    findOneUser();
                    break;
                case "users":
                    printAllUsers();
                    break;
                case "addFriend":
                    saveFriend();
                    break;
                case "deleteFriend":
                    deleteFriend();
                    break;
                case "friends":
                    printAllFriendShips();
                    break;
                case "communities":
                    printNumberOfCommunities();
                    break;
                case "biggestCommunity":
                    printBiggestCommunity();
                    break;
                case "exit":
                    break label;
                default:
                    System.out.println("This command doesn't exist");
                    break;
            }
        }

    }

    private void saveUser() {
        String firstName;
        String lastName;

        System.out.print("Read first name: ");
        firstName = scanner.nextLine();

        System.out.print("Read last name: ");
        lastName = scanner.nextLine();

        User newUser = new User(firstName, lastName);

        try {
            service.save(newUser);
            //uniqueId++;
        } catch (ValidationException | IllegalArgumentException v) {
            System.out.print(v.getMessage());
        } catch (ServiceException s) {
            System.out.println(s.getMessage());
        }

    }

    private void deleteUser() {
        long idUser;

        System.out.print("Read id: ");

        try {
            idUser = scanner.nextLong();
            service.delete(idUser);
        } catch (IllegalArgumentException | ServiceException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Id must be long");
        }

        scanner.nextLine();

    }

    private void updateUser() {
        long idUser = 0L;
        String firstName;
        String lastName;

        System.out.print("Read id User: ");

        try {
            idUser = scanner.nextLong();
        } catch (IllegalArgumentException | ServiceException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Id must be long");
        }

        scanner.nextLine();

        System.out.print("Read first name: ");
        firstName = scanner.nextLine();

        System.out.print("Read last name: ");
        lastName = scanner.nextLine();

        User user = new User(firstName, lastName);

        user.setId(idUser);

        try {
            service.update(user);
        } catch (ValidationException | IllegalArgumentException v) {
            System.out.print(v.getMessage());
        } catch (ServiceException s) {
            System.out.println(s.getMessage());
        }

    }

    private void findOneUser() {
        long idUser;

        System.out.print("Read id: ");

        try {
            idUser = scanner.nextLong();
            User user = service.findOne(idUser);
            System.out.println(user);
        } catch (IllegalArgumentException | ServiceException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Id must be long");
        }

        scanner.nextLine();

    }

    private void printAllUsers() {
        Iterable<User> users = service.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    private void readDataFriendShip() {
        long idFriend;

        try {
            System.out.print("Read id user: ");
            idUser = scanner.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Id user must be long");
            scanner.nextLine();
            return;
        }

        scanner.nextLine();

        try {
            System.out.print("Read id friend: ");
            idFriend = scanner.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Id new friend must be not null");
            scanner.nextLine();
            return;
        }

        scanner.nextLine();

        for (User user : service.findAll()) {
            if (Objects.equals(user.getId(), idFriend)) {
                friend = user;
                break;
            }
        }
    }

    private void saveFriend() {
        friend = new User("", "");
        friend.setId(0L);

        readDataFriendShip();

        String friendsFrom = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            service.saveFriend(idUser, friend, friendsFrom);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

    }

    private void deleteFriend() {
        readDataFriendShip();

        try {
            service.deleteFriend(idUser, friend);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

    }

    private void printAllFriendShips() {
        List<FriendShip<Long>> friendShips = service.getFriendsWithDate();
        Map<Long, User> users = service.getEntities();
        for (FriendShip<Long> f : friendShips) {
            if (users.get(f.getIdUser1()) != null && users.get(f.getIdUser2()) != null) {
                System.out.println("(" + users.get(f.getIdUser1()) + "," + users.get(f.getIdUser2()) + "," + f.getFriendsFrom() + ")");
            }
        }
    }

    private void printNumberOfCommunities() {
        System.out.println("Number of communities is " + service.numberOfComponents());
    }

    private void printBiggestCommunity() {
        System.out.println(service.longestComponent());
    }

}
