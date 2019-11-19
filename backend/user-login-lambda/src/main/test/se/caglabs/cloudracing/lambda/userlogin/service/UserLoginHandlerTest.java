package se.caglabs.cloudracing.lambda.userlogin.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.session.dao.SessionDao;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserLoginHandlerTest {

    @Mock
    private APIGatewayProxyRequestEvent request;
    @Mock
    private UserDao userDao;
    @Mock
    private SessionDao sessionDao;

    private UserLoginHandler userLoginHandler;

    private String jsonBody = "{\"name\":\"satan\", \"password\":\"anklever\"}";

    @Before
    public void setup() {
        initMocks(this);
        userLoginHandler = new UserLoginHandler();
    }

    @Test
    public void userLogin() {
        when(request.getBody()).thenReturn(jsonBody);
        APIGatewayProxyResponseEvent event = userLoginHandler.userLogin(request);
        assertThat(event.getStatusCode(), is(201));
    }
}
