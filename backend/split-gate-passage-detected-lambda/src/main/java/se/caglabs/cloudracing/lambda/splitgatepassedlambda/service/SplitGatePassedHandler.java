package se.caglabs.cloudracing.lambda.splitgatepassedlambda.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class SplitGatePassedHandler {
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello  all");
    }
}
