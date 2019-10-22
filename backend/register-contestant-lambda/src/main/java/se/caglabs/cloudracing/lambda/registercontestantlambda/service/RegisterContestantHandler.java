package se.caglabs.cloudracing.lambda.registercontestant.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class RegisterContestantHandler {
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello");
    }
}
