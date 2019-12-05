package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegisteredUserHandlerTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private Context context;
    @Mock
    private RegisteredUserService service;

    private RegisteredUserHandler registeredUserHandler;

    private String jsonBody = "{\"name\":\"stefan\", \"password\":\"aik4ever\", \"type\":\"CONTESTANT\"}";

    @Before
    public void setup() {
        initMocks(this);
        registeredUserHandler = new RegisteredUserHandler();
    }

    @Test
    public void shouldCallRegisterUser() throws Exception {

        when(request.getResource()).thenReturn("/registered-user/{name}");
        when(request.getHttpMethod()).thenReturn("POST");
        registeredUserHandler.route(request, context);

        verify(service).registerUser(any(APIGatewayProxyRequestEvent.class));
    }

    @Test
    public void shouldTriggerRegisterUser() throws Exception {

        when(request.getResource()).thenReturn("/registered-user/Duderino/race");

        registeredUserHandler.route(request, context);
    }
}

