package com.example.laborator6.service;

import com.example.laborator6.domain.User;

public class UserVerifier implements Exception<User> {

    @Override
    public void verifySave(User entity) throws ServiceException {
        if (entity != null)
            throw new ServiceException("This user already exist in list");
    }

    @Override
    public void verifyDelete(User entity) throws ServiceException {
        if (entity == null)
            throw new ServiceException("This user doesn't exist in list");
    }


    @Override
    public void verifyUpdate(User entity) throws ServiceException {
        if (entity != null) {
            throw new ServiceException("This user doesn't exist in list");
        }
    }

    @Override
    public void verifyFindOne(User entity) throws ServiceException {
        if (entity == null) {
            throw new ServiceException("This user doesn't exist in list");
        }
    }

    @Override
    public void verifyEntitiesFromFriendShip(User entity1, User entity2) throws ServiceException {
        String errors = "";

        if (entity1 == null) {
            errors += "This user doesn't exist in list\n";
        }

        if (entity2 == null) {
            errors += "This friend doesn't exist in list\n";
        }

        if (errors.length() > 0) {
            throw new ServiceException(errors);
        }
    }

    @Override
    public void verifySaveFriend(User entity1, User entity2) throws ServiceException {
        if (entity1 == entity2) {
            throw new ServiceException("This friendship already exist");
        }
    }

    @Override
    public void verifyDeleteFriend(User entity1) throws ServiceException {
        if (entity1 == null) {
            throw new ServiceException("This friendship doesn't exist");
        }
    }
}
