package com.example.laborator6.repository;

import com.example.laborator6.domain.User;

import java.util.List;

public class UserFile extends AbstractFileRepository<Long, User> {

    public UserFile(String fileName) {
        super(fileName);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(Long.valueOf(attributes.get(0)));
        return user;
    }

    @Override
    public String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}
