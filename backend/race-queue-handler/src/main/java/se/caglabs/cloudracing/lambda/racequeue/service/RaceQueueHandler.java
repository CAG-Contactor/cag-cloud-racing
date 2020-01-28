package se.caglabs.cloudracing.lambda.racequeue.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class RaceQueueHandler {

    private RaceQueueService raceQueueService = new RaceQueueService();

    public APIGatewayProxyResponseEvent route(APIGatewayProxyRequestEvent request, Context context) {
        if (Objects.equals(request.getResource(), "ping")) {
            System.out.println("Got ping");
            return new APIGatewayProxyResponseEvent().withStatusCode(200);
        }

        switch (request.getHttpMethod()) {
            case "GET":
                return raceQueueService.getRaceQueue();
            case "DELETE":
                return raceQueueService.bailOut(request);
            case "POST":
                return raceQueueService.signUpForRace(request);
            default:
                throw new RuntimeException("Endpoint with HTTP method " + request.getHttpMethod() + " does not exist");
        }
    }
}
