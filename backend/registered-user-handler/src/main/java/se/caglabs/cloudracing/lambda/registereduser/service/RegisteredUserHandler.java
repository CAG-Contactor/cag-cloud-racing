package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.digest.PasswordDigest;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDaoImpl;
import se.caglabs.cloudracing.common.persistence.registereduser.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registereduser.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RegisteredUserHandler {

    private UserDao dao;

    public RegisteredUserHandler(){}

    /**
     * For testing purpose.
     */
     RegisteredUserHandler(UserDao userDao) {
         this.dao = userDao;
     }

    public APIGatewayProxyResponseEvent registerUser(APIGatewayProxyRequestEvent request) {

        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(body, User.class);
            user.setPassword(PasswordDigest.digest(user.getPassword()));
            log.info("Creating new user: {}", user.getName());
            getUserDao().saveUser(user);

            return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody("Created");
        } catch (JsonProcessingException  e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad input values!");
        } catch (UserDaoException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("User already exists!");
        }
    }

    /**
     * Fetches a user with a specified name/id
     */
    public User getRegisteredUser(String name) {
         return getUserDao().getUser(name);
    }

    /**
     * Fetches all registered users
     */
    public List getRegisteredUsers() {
        List users = getUserDao().listUsers();

        return users;
    }

    /**
     * Fetches all races that a user that a user has taken part in.
     */
    public List getRegisteredUsersInRace() {
        return new ArrayList();
    }

    private UserDao getUserDao() {
        if(this.dao == null) {
            this.dao = new UserDaoImpl();
        }
        return this.dao;
    }
}
