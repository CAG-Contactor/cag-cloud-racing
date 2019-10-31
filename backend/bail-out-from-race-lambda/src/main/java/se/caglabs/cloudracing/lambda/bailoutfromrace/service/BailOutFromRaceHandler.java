package se.caglabs.cloudracing.lambda.bailoutfromrace.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class BailOutFromRaceHandler {
    public APIGatewayProxyResponseEvent bailOutFromRace(APIGatewayProxyRequestEvent request, Context context) {
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello  all");
    }
}
