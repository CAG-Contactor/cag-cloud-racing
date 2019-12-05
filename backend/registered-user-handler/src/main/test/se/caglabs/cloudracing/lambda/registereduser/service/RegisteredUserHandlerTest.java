package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.mockito.Mock;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDao;

public class RegisteredUserHandlerTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private UserDao userDao;

    private RegisteredUserHandler registeredUserHandler;

    private String jsonBody = "{\"name\":\"stefan\", \"password\":\"aik4ever\", \"type\":\"CONTESTANT\"}";

    /*@Before
    public void setup() {
        initMocks(this);
        registeredUserHandler = new RegisteredUserHandler(userDao);
    }

    @Test
    public void shouldReturn_201Created() throws UserDaoException {
        when(request.getBody()).thenReturn(jsonBody);

        APIGatewayProxyResponseEvent event = registeredUserHandler.registerUser(request);

        assertThat(event.getStatusCode(), is(201));
        assertThat(event.getBody(), is("Created"));
    }

    @Test
    public void shouldReturn_400BadRequest() throws UserDaoException {
        String jsonBody = "{\"wrong\":\"stefan\", \"password\":\"aik4ever\", \"type\":\"CONTESTANT\"}";
        when(request.getBody()).thenReturn(jsonBody);

        APIGatewayProxyResponseEvent event = registeredUserHandler.registerUser(request);

        assertThat(event.getStatusCode(), is(400));
        assertThat(event.getBody(), is("Bad input values!"));
    }

    @Test
    public void shouldReturn_409BadRequest() throws UserDaoException {
        when(request.getBody()).thenReturn(jsonBody);
        doThrow(UserDaoException.class).when(userDao).saveUser(any(User.class));
        APIGatewayProxyResponseEvent event = registeredUserHandler.registerUser(request);

        assertThat(event.getStatusCode(), is(409));
        assertThat(event.getBody(), is("Contestant already exists!"));
    }*/
}

