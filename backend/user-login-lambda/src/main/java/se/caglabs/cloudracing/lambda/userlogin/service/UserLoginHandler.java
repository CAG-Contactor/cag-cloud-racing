package se.caglabs.cloudracing.lambda.userlogin.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDao;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDaoImpl;
import se.caglabs.cloudracing.common.persistence.session.model.Session;

import java.util.UUID;

public class UserLoginHandler {

    public APIGatewayProxyResponseEvent userLogin(APIGatewayProxyRequestEvent request, Context context) {
//        UserDao userDao = new UserDaoImpl(); //
        SessionDao sessionDao = new SessionDaoImpl();

        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
//        User user;
        try {
            Object user = mapper.readValue(body, Object.class); // User.class
            // Check user password
            if (true) {
                // Create and return session
                Session session = Session.of(UUID.randomUUID().toString(), "userId");
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
