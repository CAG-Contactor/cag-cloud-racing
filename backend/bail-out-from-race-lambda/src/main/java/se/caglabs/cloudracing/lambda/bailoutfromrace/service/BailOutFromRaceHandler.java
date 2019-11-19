package se.caglabs.cloudracing.lambda.bailoutfromrace.service;
import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.dto.UserDTO;

@Slf4j
public class BailOutFromRaceHandler {

    public APIGatewayProxyResponseEvent bailOutFromRace(APIGatewayProxyRequestEvent request, Context context) {

        UserDTO userId = null;
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            userId = mapper.readValue(body, UserDTO.class);
            log.info("User bailing out, id: " + userId.getName());

            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hi " + userId.getName() + " are you man or mice?");
        } catch (JsonProcessingException  e) {
            log.warn("Error bailing out user: " + userId.getName(), e);
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error bailing out user: " + userId.getName());
        }
    }
}
