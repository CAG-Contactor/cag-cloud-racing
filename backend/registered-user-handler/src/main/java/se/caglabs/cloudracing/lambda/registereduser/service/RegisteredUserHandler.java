package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisteredUserHandler {

    private static final String REGISTER_USER = "/registered-user";
    private static final String REGISTER_USERS = "/registered-users";
    private static final String POST = "POST";
    private static final String GET = "GET";

    private RegisteredUserService registeredUserService = new RegisteredUserService();

    public RegisteredUserHandler(){}

    public APIGatewayProxyResponseEvent route(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {

        String path = request.getPath();
        if(request.getPathParameters() != null) {
            path = REGISTER_USER;
        }

        if(REGISTER_USER.equals(path)) {
            switch (request.getHttpMethod()) {
                case POST:
                    return registeredUserService.registerUser(request);
                case GET:
                    return registeredUserService.getRegisteredUser(request);
                default:
                    return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request!");
            }
        } else if (REGISTER_USERS.equals(path)) {
                log.info("Request for /registered-users");
            switch (request.getHttpMethod()) {
                case GET:
                    return registeredUserService.getRegisteredUsers();
                default:
                    return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request!!");
            }
        }

        return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request!!!");
    }
}
