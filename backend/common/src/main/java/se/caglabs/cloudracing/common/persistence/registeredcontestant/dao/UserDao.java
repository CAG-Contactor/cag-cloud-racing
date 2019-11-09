package se.caglabs.cloudracing.common.persistence.registeredcontestant.dao;

import se.caglabs.cloudracing.common.persistence.registeredcontestant.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.model.User;

import java.util.List;

public interface UserDao {
    User getUser(String name);
    void saveUser(User user) throws UserDaoException;
    List<User> listUsers();
    boolean userExist(String name);
}
