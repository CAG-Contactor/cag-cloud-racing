package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.stuff.CorsHeaders;

@Slf4j
public class RegisteredUserHandler {

    private static final String REGISTER_NEW_USER = "/registered-user";
    private static final String REGISTER_USER = "/registered-user/{name}";
    private static final String REGISTER_USERS = "/registered-users";
    private static final String RACE = "/registered-user/{name}/race";
    private static final String USER_LOGIN = "/user-login";
    private static final String USER_LOGOUT = "/user-login/{sessionToken}";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";

    private RegisteredUserService service = new RegisteredUserService();

    public RegisteredUserHandler() {
    }

    public RegisteredUserHandler(RegisteredUserService service) {
        this.service = service;
    }

    public APIGatewayProxyResponseEvent route(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        String resource = request.getResource();

        if (resource.equalsIgnoreCase(REGISTER_USER) || resource.equalsIgnoreCase(REGISTER_NEW_USER) ) {
            switch (request.getHttpMethod()) {
                case POST:
                    return service.registerUser(request);
                case GET:
                    return service.getRegisteredUser(request);
                case DELETE:
                    return service.deleteRegisteredUser(request);
                default:
                    return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(400).withBody("Bad request for user!");
            }
        } else if (resource.equalsIgnoreCase(REGISTER_USERS)) {
            if (GET.equals(request.getHttpMethod())) {
                return service.getRegisteredUsers();
            }
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(400).withBody("Bad request for registered users!");
        } else if (resource.equalsIgnoreCase(RACE)) {
            if (GET.equals(request.getHttpMethod())) {
                return service.getUserRace(request);
            } else {
                return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(400).withBody("Bad request for user races!");
            }
        } else if(resource.equalsIgnoreCase(USER_LOGIN) || resource.equalsIgnoreCase(USER_LOGOUT)) {
            if (POST.equals(request.getHttpMethod())) {
                return service.userLogin(request);
            } else if (DELETE.equals(request.getHttpMethod())) {
                return service.userLogout(request);
            }
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(400).withBody("Bad request for login user!");
        }

        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(400).withBody("Bad request!");
    }
}
