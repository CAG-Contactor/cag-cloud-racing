package se.caglabs.cloudracing.lambda.registereduser.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
        registeredUserHandler = new RegisteredUserHandler(service);
    }

    @Test
    public void shouldRegisterANewUser() throws Exception {
        when(request.getResource()).thenReturn("/registered-user");
        when(request.getHttpMethod()).thenReturn("POST");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).registerUser(request);
    }

    @Test
    public void shouldGetAUser() throws Exception {
        when(request.getResource()).thenReturn("/registered-user/{name}");
        when(request.getHttpMethod()).thenReturn("GET");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).getRegisteredUser(request);
    }

    @Test
    public void shouldDeleteAUser() throws Exception {
        when(request.getResource()).thenReturn("/registered-user/{name}");
        when(request.getHttpMethod()).thenReturn("DELETE");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).deleteRegisteredUser(request);
    }

    @Test
    public void shouldReturnBadRequest_GetRegisteredUSer() throws Exception {
        when(request.getResource()).thenReturn("/registered-user");
        when(request.getHttpMethod()).thenReturn("PUT");

        registeredUserHandler.route(request, context);

        APIGatewayProxyResponseEvent event = registeredUserHandler.route(request, context);
        assertThat(event.getBody(), is("Bad request for user!"));
        assertThat(event.getStatusCode(), is(400));
    }


    @Test
    public void shouldGetAllUsers() throws Exception {
        when(request.getResource()).thenReturn("/registered-users");
        when(request.getHttpMethod()).thenReturn("GET");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).getRegisteredUsers();
    }

    @Test
    public void shouldReturnBadRequest_GetAllUsers() throws Exception {
        when(request.getResource()).thenReturn("/registered-users");
        when(request.getHttpMethod()).thenReturn("POST");

        registeredUserHandler.route(request, context);

        APIGatewayProxyResponseEvent event = registeredUserHandler.route(request, context);
        assertThat(event.getBody(), is("Bad request for registered users!"));
        assertThat(event.getStatusCode(), is(400));
    }

    @Test
    public void shouldGetRacesForUsers() throws Exception {
        when(request.getResource()).thenReturn("/registered-user/{name}/race");
        when(request.getHttpMethod()).thenReturn("GET");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).getUserRace(request);
    }

    @Test
    public void shouldReturnBadRequest_GetRacesForUsers() throws Exception {
        when(request.getResource()).thenReturn("/registered-user/{name}/race");
        when(request.getHttpMethod()).thenReturn("POST");

        registeredUserHandler.route(request, context);

        APIGatewayProxyResponseEvent event = registeredUserHandler.route(request, context);
        assertThat(event.getBody(), is("Bad request for user races!"));
        assertThat(event.getStatusCode(), is(400));
    }

    @Test
    public void shouldLoginUser() throws Exception {
        when(request.getResource()).thenReturn("/user-login");
        when(request.getHttpMethod()).thenReturn("POST");

        registeredUserHandler.route(request, context);

        verify(service, times(1)).userLogin(request);
    }

    @Test
    public void shouldReturnBadRequest_LoginUser() throws Exception {
        when(request.getResource()).thenReturn("/user-login");
        when(request.getHttpMethod()).thenReturn("GET");

        registeredUserHandler.route(request, context);

        APIGatewayProxyResponseEvent event = registeredUserHandler.route(request, context);
        assertThat(event.getBody(), is("Bad request for login user!"));
        assertThat(event.getStatusCode(), is(400));
    }


    @Test
    public void shouldReturnBadRequest() throws Exception {
        when(request.getResource()).thenReturn("/unknown}");
        when(request.getHttpMethod()).thenReturn("NOT_APPLICABLE");

        APIGatewayProxyResponseEvent event = registeredUserHandler.route(request, context);
        assertThat(event.getBody(), is("Bad request!"));
        assertThat(event.getStatusCode(), is(400));
    }

}

