package se.caglabs.cloudracing.lambda.startgatepassedlambda.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;


public class StartGatePassedHandler {
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {
        //return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello  all");

        String timestampParam = request.getQueryStringParameters().get("timestamp");
        //ObjectMapper mapper = new ObjectMapper();
        //System.out.println("Stage: " + System.getenv("Stage"));
        try {
            Timestamp timestamp = new Timestamp(Long.getLong(timestampParam));
            /*if (currentRace != null && curremtRace.status == Status.ARMED) {
                setCurrentState(Status.STARTED, timestamp);

                return new APIGatewayProxyResponseEvent().withStatusCode(201);
            }*/

            //return new APIGatewayProxyResponseEvent().withStatusCode(200);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(timestamp.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }
}
