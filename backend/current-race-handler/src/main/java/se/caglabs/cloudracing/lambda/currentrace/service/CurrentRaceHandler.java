package se.caglabs.cloudracing.lambda.currentrace.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class CurrentRaceHandler {
    private CurrentRaceService currentRaceService = new CurrentRaceService();

    public APIGatewayProxyResponseEvent route(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        if (Objects.equals(request.getResource(), "ping")) {
            System.out.println("Got ping");
            return new APIGatewayProxyResponseEvent().withStatusCode(200);
        }
        switch (request.getHttpMethod()) {
            case "GET":
                return currentRaceService.getCurrentRace();
            case "DELETE":
                return currentRaceService.abortActiveRace(request);
            case "POST":
                return currentRaceService.armRace(request);
            default:
                throw new RuntimeException("Endpoint with HTTP method " + request.getHttpMethod() + " does not exist");
        }
    }
}
