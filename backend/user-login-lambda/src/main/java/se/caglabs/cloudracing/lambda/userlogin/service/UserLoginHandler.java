package se.caglabs.cloudracing.lambda.userlogin.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDaoImpl;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.model.User;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDao;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDaoImpl;
import se.caglabs.cloudracing.common.persistence.session.model.Session;

import java.util.UUID;

public class UserLoginHandler {

    public APIGatewayProxyResponseEvent userLogin(APIGatewayProxyRequestEvent request, Context context) {
        UserDao userDao = new UserDaoImpl();
        SessionDao sessionDao = new SessionDaoImpl();

        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User userRequest = mapper.readValue(body, User.class);
            // Read user and check user password
            User user = userDao.getUser(userRequest.getName());
            if (true) {
                // Create and return session
                Session session = Session.of(UUID.randomUUID().toString(), userRequest.getName());
                System.out.println("Session: " + session.getToken() + "::" + session.getUserName());
                sessionDao.saveSession(session);
                return new APIGatewayProxyResponseEvent()
                        .withBody(mapper.writeValueAsString(session))
                        .withStatusCode(201);
            }
            return new APIGatewayProxyResponseEvent().withStatusCode(401);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }
}
