package se.caglabs.cloudracing.lambda.registercontestant.dao;

import se.caglabs.cloudracing.lambda.registercontestant.exception.UserDaoException;
import se.caglabs.cloudracing.lambda.registercontestant.model.User;

import java.util.List;

public interface UserDao {
    User getUser(String name);
    void saveUser(User user) throws UserDaoException;
    List<User> listUsers();
}
