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

import java.util.List;

@Slf4j
public class RegisteredUserService {
    private static final String NAME = "name";
    private UserDao dao;
    private ObjectMapper mapper;

    public RegisteredUserService() {
        mapper = new ObjectMapper();

    }

    APIGatewayProxyResponseEvent registerUser(APIGatewayProxyRequestEvent request) {

        log.info("Registering new user " + request.getBody());
        String body = request.getBody();
        try {
            User user = mapper.readValue(body, User.class);
            user.setPassword(PasswordDigest.digest(user.getPassword()));
            getUserDao().saveUser(user);
            return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody(mapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad input values!");
        } catch (UserDaoException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("User already exists!");
        }
    }

    /**
     * Fetches a user with a specified name/id
     */
    APIGatewayProxyResponseEvent getRegisteredUser(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        User user = getUserDao().getUser(request.getPathParameters().get(NAME));
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(mapper.writeValueAsString(user));
    }

    /**
     * Deletes a registered user
     */
    APIGatewayProxyResponseEvent deleteRegisteredUser(APIGatewayProxyRequestEvent request) {
        User user = getUserDao().getUser(request.getPathParameters().get(NAME));
        getUserDao().deleteUser(user);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("User deleted!");
    }

    /**
     * Fetches all registered users
     */
     APIGatewayProxyResponseEvent getRegisteredUsers() throws JsonProcessingException {
        List users = getUserDao().listUsers();
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(mapper.writeValueAsString(users));
    }

    /**
     * Fetches all races a user user has taken part in.
     */
    APIGatewayProxyResponseEvent getUserRace(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        User user = getUserDao().getUser(request.getPathParameters().get(NAME));

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(mapper.writeValueAsString(user));
    }

    private UserDao getUserDao() {
        if(this.dao == null) {
            this.dao = new UserDaoImpl();
        }
        return this.dao;
    }
}
