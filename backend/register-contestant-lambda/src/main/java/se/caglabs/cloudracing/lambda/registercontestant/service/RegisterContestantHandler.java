package se.caglabs.cloudracing.lambda.registercontestant.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.caglabs.cloudracing.lambda.registercontestant.dao.UserDao;
import se.caglabs.cloudracing.lambda.registercontestant.dao.UserDaoImpl;
import se.caglabs.cloudracing.lambda.registercontestant.exception.UserDaoException;
import se.caglabs.cloudracing.lambda.registercontestant.model.User;

public class RegisterContestantHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterContestantHandler.class);
    private UserDao dao;

    public APIGatewayProxyResponseEvent createContestant(APIGatewayProxyRequestEvent request) {

        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(body, User.class);
            LOGGER.info("Creating new contestant: {}", user.getName());
            getUserDao().saveUser(user);

            return new APIGatewayProxyResponseEvent().withStatusCode(201);
        } catch (JsonProcessingException | UserDaoException e) {
            LOGGER.warn("Error creating new contestant!", e);

            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error creating new contestant!");
        }
    }

    private UserDao getUserDao() {
        if(this.dao == null) {
            this.dao = new UserDaoImpl();
        }
        return this.dao;
    }
}
