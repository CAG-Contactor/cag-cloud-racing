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
    private static final String RACE = "/race";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";

    private RegisteredUserService registeredUserService = new RegisteredUserService();

    public RegisteredUserHandler(){}

    public APIGatewayProxyResponseEvent route(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {

        String path = mapPath(request);

        if(REGISTER_USER.equals(path)) {
            switch (request.getHttpMethod()) {
                case POST:
                    return registeredUserService.registerUser(request);
                case GET:
                    return registeredUserService.getRegisteredUser(request);
                case DELETE:
                    return registeredUserService.deleteRegisteredUser(request);
                default:
                    return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request for user!");
            }
        } else if (REGISTER_USERS.equals(path)) {
            if (GET.equals(request.getHttpMethod())) {
                return registeredUserService.getRegisteredUsers();
            }
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request for users!");
        } else if(RACE.equals(path)) {
            if (GET.equals(request.getHttpMethod())) {
                return registeredUserService.getUserRace(request);
            }
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request for user races!");
        }

        return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Bad request!");
    }

    private String mapPath(APIGatewayProxyRequestEvent request) {
        return request.getPathParameters() != null ? REGISTER_USER : request.getPath();
    }
}
