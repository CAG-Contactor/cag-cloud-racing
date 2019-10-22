package se.caglabs.cloudracing.lambda.registercontestant.dao;

import se.caglabs.cloudracing.lambda.registercontestant.model.User;

import java.util.List;

public interface UserDao {
    List<User> listUsers();
    User getUser(String name);
    void saveUser(User user);
    void deleteUser(String name);
}
