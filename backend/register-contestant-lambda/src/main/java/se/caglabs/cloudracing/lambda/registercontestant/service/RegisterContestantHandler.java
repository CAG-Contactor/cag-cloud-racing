package se.caglabs.cloudracing.lambda.registercontestant.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.digest.PasswordDigest;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDaoImpl;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.model.User;

@Slf4j
public class RegisterContestantHandler {

    private UserDao dao;

    public APIGatewayProxyResponseEvent createContestant(APIGatewayProxyRequestEvent request) {

        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(body, User.class);
            user.setPassword(PasswordDigest.digest(user.getPassword()));
            log.info("Creating new contestant: {}", user.getName());
            getUserDao().saveUser(user);

            return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody("Created");
        } catch (JsonProcessingException  e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad input values!");
        } catch (UserDaoException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("Contestant already exists!");
        }
    }

    private UserDao getUserDao() {
        if(this.dao == null) {
            this.dao = new UserDaoImpl();
        }
        return this.dao;
    }
}
