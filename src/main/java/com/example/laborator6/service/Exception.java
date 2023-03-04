package com.example.laborator6.service;

public interface Exception<T> {
    void verifySave(T entity) throws ServiceException;

    void verifyDelete(T entity) throws ServiceException;

    void verifyUpdate(T entity) throws ServiceException;

    void verifyFindOne(T entity) throws ServiceException;

    void verifyEntitiesFromFriendShip(T entity1, T entity2) throws ServiceException;

    void verifySaveFriend(T entity1, T entity2) throws ServiceException;

    void verifyDeleteFriend(T entity1) throws ServiceException;

}
