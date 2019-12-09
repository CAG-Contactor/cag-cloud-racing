package se.caglabs.cloudracing.common.persistence.registereduser.dao;

import se.caglabs.cloudracing.common.persistence.registereduser.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registereduser.model.User;

import java.util.List;

public interface UserDao {
    User getUser(String name);
    void saveUser(User user) throws UserDaoException;
    List<User> listUsers();
    boolean userExist(String name);
    void deleteUser(User name);
}
