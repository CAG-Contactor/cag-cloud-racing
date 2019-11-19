package se.caglabs.cloudracing.lambda.registercontestant.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.model.User;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegisterContestantHandlerTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private UserDao userDao;

    private RegisterContestantHandler registerContestantHandler;

    private String jsonBody = "{\"name\":\"stefan\", \"password\":\"aik4ever\", \"type\":\"CONTESTANT\"}";

    @Before
    public void setup() {
        initMocks(this);
        registerContestantHandler = new RegisterContestantHandler(userDao);
    }

    @Test
    public void shouldReturn_201Created() throws UserDaoException {
        when(request.getBody()).thenReturn(jsonBody);

        APIGatewayProxyResponseEvent event = registerContestantHandler.createContestant(request);

        assertThat(event.getStatusCode(), is(201));
        assertThat(event.getBody(), is("Created"));
    }

    @Test
    public void shouldReturn_400BadRequest() throws UserDaoException {
        String jsonBody = "{\"wrong\":\"stefan\", \"password\":\"aik4ever\", \"type\":\"CONTESTANT\"}";
        when(request.getBody()).thenReturn(jsonBody);

        APIGatewayProxyResponseEvent event = registerContestantHandler.createContestant(request);

        assertThat(event.getStatusCode(), is(400));
        assertThat(event.getBody(), is("Bad input values!"));
    }

    @Test
    public void shouldReturn_409BadRequest() throws UserDaoException {
        when(request.getBody()).thenReturn(jsonBody);
        doThrow(UserDaoException.class).when(userDao).saveUser(any(User.class));
        APIGatewayProxyResponseEvent event = registerContestantHandler.createContestant(request);

        assertThat(event.getStatusCode(), is(409));
        assertThat(event.getBody(), is("Contestant already exists!"));
    }
}

