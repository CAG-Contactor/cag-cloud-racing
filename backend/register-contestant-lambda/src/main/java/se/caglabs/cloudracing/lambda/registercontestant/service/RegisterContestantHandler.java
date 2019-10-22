package se.caglabs.cloudracing.lambda.registercontestant.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.caglabs.cloudracing.lambda.registercontestant.dao.UserDao;
import se.caglabs.cloudracing.lambda.registercontestant.dao.UserDaoImpl;
import se.caglabs.cloudracing.lambda.registercontestant.model.User;

public class RegisterContestantHandler {
    private UserDao dao;

    public APIGatewayProxyResponseEvent createContestant(APIGatewayProxyRequestEvent request, Context context) {

        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        User user;
        System.out.println("Stage: " + System.getenv("Stage"));
        try {
            user = mapper.readValue(body, User.class);
            this.getUserDao().saveUser(user);
            return new APIGatewayProxyResponseEvent().withStatusCode(201);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    private UserDao getUserDao() {
        if(this.dao == null) {
            this.dao = new UserDaoImpl();
        }
        return this.dao;
    }
}
