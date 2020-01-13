package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.digest.PasswordDigest;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDaoImpl;
import se.caglabs.cloudracing.common.persistence.registereduser.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registereduser.model.User;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDao;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDaoImpl;
import se.caglabs.cloudracing.common.persistence.session.model.Session;
import se.caglabs.cloudracing.common.persistence.stuff.CorsHeaders;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
public class RegisteredUserService {
    private static final String NAME = "name";
    private static final String SESSION_TOKEN = "sessionToken";
    private UserDao userDao;
    private RaceDao raceDao;
    private SessionDao sessionDao;
    private ObjectMapper mapper;

    public RegisteredUserService() {
        mapper = new ObjectMapper();

    }

    /**
     * Register a new user.
     *
     */
    APIGatewayProxyResponseEvent registerUser(APIGatewayProxyRequestEvent request) {

        log.info("Registering new user " + request.getBody());
        String body = request.getBody();
        try {
            User user = mapper.readValue(body, User.class);
            user.setPassword(PasswordDigest.digest(user.getPassword()));
            getUserDao().saveUser(user);
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(200).withBody(mapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(400).withBody("Bad input values!");
        } catch (UserDaoException e) {
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(409).withBody("User already exists!");
        }
    }

    /**
     * Fetches a user with a specified name/id
     */
    APIGatewayProxyResponseEvent getRegisteredUser(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        User user = getUserDao().getUser(request.getPathParameters().get(NAME));
        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(200).withBody(mapper.writeValueAsString(user));
    }

    /**
     * Deletes a registered user
     */
    APIGatewayProxyResponseEvent deleteRegisteredUser(APIGatewayProxyRequestEvent request) {
        User user = getUserDao().getUser(request.getPathParameters().get(NAME));
        getUserDao().deleteUser(user);
        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(200).withBody("User deleted!");
    }

    /**
     * Fetches all registered users
     */
     APIGatewayProxyResponseEvent getRegisteredUsers() throws JsonProcessingException {
        List users = getUserDao().listUsers();
        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(200).withBody(mapper.writeValueAsString(users));
    }

    /**
     * Fetches all races a user user has taken part in.
     */
    APIGatewayProxyResponseEvent getUserRace(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        User user = getUserDao().getUser(request.getPathParameters().get(NAME));
        List<Race> races = getRaceDao().findAllByUserName(user.getName());

        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(200).withBody(mapper.writeValueAsString(races));
    }

    /**
     * Login user
     * @param request is passed
     * @return the response
     */
    APIGatewayProxyResponseEvent userLogin(APIGatewayProxyRequestEvent request) {
        String body = request.getBody();
        try {
            User userRequest = mapper.readValue(body, User.class);
            if (getUserDao().userExist(userRequest.getName())) {
                // Read user and check user password
                User user = getUserDao().getUser(userRequest.getName());
                boolean userPasswordIsOk = PasswordDigest.digest(userRequest.getPassword())
                        .equals(user.getPassword());
                if (userPasswordIsOk) {
                    // Create and return session
                    Session session = new Session(UUID.randomUUID().toString(), user.getName());
                    getSessionDao().saveSession(session);
                    return new APIGatewayProxyResponseEvent()
                            .withBody(mapper.writeValueAsString(session))
                            .withHeaders(new CorsHeaders())
                            .withStatusCode(200);
                }
            }
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(401);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(500);
        }
    }

    /**
     * Logout user
     * @param request is passed
     * @return the response
     */
    APIGatewayProxyResponseEvent userLogout(APIGatewayProxyRequestEvent request) {
        log.error("DELETE USER SESSION");
        log.error("PRE SESSION: "+request.getPathParameters().get(SESSION_TOKEN));
        Session session = getSessionDao().findSessionByToken(request.getPathParameters().get(SESSION_TOKEN));
        log.error("POST SESSION: "+session.toString());
        getSessionDao().deleteSession(session);
        log.error("SESSION REMOVED FROM DATABASE: "+session.toString());
        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withBody("Session deleted!").withStatusCode(200);
    }

    private RaceDao getRaceDao() {
        if(this.raceDao == null) {
            this.raceDao = new RaceDaoImpl();
        }

        return this.raceDao;
    }

    private UserDao getUserDao() {
        if(this.userDao == null) {
            this.userDao = new UserDaoImpl();
        }
        return this.userDao;
    }

    private SessionDao getSessionDao() {
        if(isNull(sessionDao)) {
            sessionDao = new SessionDaoImpl();
        }
        return sessionDao;
    }
}
