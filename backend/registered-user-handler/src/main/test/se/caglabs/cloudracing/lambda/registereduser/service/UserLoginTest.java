package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UserLoginTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private Context context;
    @Mock
    private RegisteredUserService service;

    private RegisteredUserHandler registeredUserHandler;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registeredUserHandler = new RegisteredUserHandler(service);
    }

    @Test
    public void userLogin() throws Exception {
        when(request.getResource()).thenReturn("/user-login");
        when(request.getHttpMethod()).thenReturn("POST");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).userLogin(request);
    }
}
