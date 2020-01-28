package se.caglabs.cloudracing.lambda.leaderboard.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.stuff.CorsHeaders;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class LeaderBoardHandler {
    private RaceDao raceDao;

    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {
        if (Objects.equals(request.getResource(), "ping")) {
            System.out.println("Got ping");
            return new APIGatewayProxyResponseEvent().withStatusCode(200);
        }
        try {
            List<Race> races = getRaceDao().findAll().stream()
                    .filter(r -> r.getRaceStatus() == Race.RaceStatus.FINISHED)
                    .sorted(Comparator.comparingLong(Race::getRaceTime))
                    .collect(Collectors.toList());
            return new APIGatewayProxyResponseEvent()
              .withHeaders(new CorsHeaders())
              .withBody(new ObjectMapper().writeValueAsString(races));
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(500).withBody("Error mapping object to json string");
        }
    }

    private RaceDao getRaceDao() {
        if (raceDao == null) {
            raceDao = new RaceDaoImpl();
        }
        return raceDao;
    }
}
