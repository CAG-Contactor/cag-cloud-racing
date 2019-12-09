package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDao;

public class UserLoginTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private UserDao userDao;
    @Mock
    private SessionDao sessionDao;

    private RegisteredUserHandler registeredUserHandler;

    private String jsonBody = "{\"name\":\"satan\", \"password\":\"anklever\"}";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registeredUserHandler = new RegisteredUserHandler();
    }

    @Test
    public void userLoginExpect401() throws JsonProcessingException {
        Mockito.when(request.getBody()).thenReturn(jsonBody);
        APIGatewayProxyResponseEvent event = registeredUserHandler.route(request, null);
        Assert.assertEquals(new Integer(401), event.getStatusCode());
    }
}
