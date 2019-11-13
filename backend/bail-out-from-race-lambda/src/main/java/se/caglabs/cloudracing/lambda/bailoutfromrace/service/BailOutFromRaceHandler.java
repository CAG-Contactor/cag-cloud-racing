package se.caglabs.cloudracing.lambda.bailoutfromrace.service;
import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.restpayload.UserNamePayload;

@Slf4j
public class BailOutFromRaceHandler {

    public APIGatewayProxyResponseEvent bailOutFromRace(APIGatewayProxyRequestEvent request, Context context) {

        UserNamePayload userNamePayload = null;
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            userNamePayload = mapper.readValue(body, UserNamePayload.class);
            log.info("User bailing out, name: " + userNamePayload.getUserName());
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hi " + userNamePayload.getUserName() + " are you man or mice?");
        } catch (JsonProcessingException  e) {
            e.printStackTrace();
            log.warn("Error bailing out user, name: " + userNamePayload.getUserName(), e);
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("userName required to bail out of race!");
        }
    }
}
