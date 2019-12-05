package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDao;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegisteredUserHandlerTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private Context context;
    @Mock
    private UserDao userDao;

    private RegisteredUserHandler registeredUserHandler;

    private String jsonBody = "{\"name\":\"stefan\", \"password\":\"aik4ever\", \"type\":\"CONTESTANT\"}";

    @Before
    public void setup() {
        initMocks(this);
        registeredUserHandler = new RegisteredUserHandler();
    }

    @Test
    public void shouldTriggerRegisterUser1() throws Exception {

        when(request.getResource()).thenReturn("/registered-user/Duderino");

        registeredUserHandler.route(request, context);
    }

    @Test
    public void shouldTriggerRegisterUser() throws Exception {

        when(request.getResource()).thenReturn("/registered-user/Duderino/race");

        registeredUserHandler.route(request, context);
    }
}

