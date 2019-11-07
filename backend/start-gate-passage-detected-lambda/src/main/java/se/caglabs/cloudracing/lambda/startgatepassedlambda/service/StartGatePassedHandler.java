package se.caglabs.cloudracing.lambda.startgatepassedlambda.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;


import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDao;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;


import java.sql.Timestamp;
import java.util.Optional;


public class StartGatePassedHandler {

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;

    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {

        String timestampParam = request.getQueryStringParameters().get("timestamp");
        if (timestampParam == null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("timestamp required");
        }
        try {
            int timestamp = Integer.parseInt(timestampParam);
            Optional<CurrentRace> currentRaceOpt = getCurrentRaceDao().getCurrentRace();
            if (!currentRaceOpt.isPresent()) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("no race is ongoing");
            }
            CurrentRace currentRace = currentRaceOpt.get();
            Optional<Race> raceOpt = raceDao.findById(currentRace.getRaceId());
            if (!raceOpt.isPresent()) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("could not find ongoing race");
            }
            Race race = raceOpt.get();
            if (race.getStatus().equals(Race.Status.ARMED)) {
                race.setStartTime(timestamp);
                race.setStatus(Race.Status.STARTED);
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("race is started");
            } else {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("race could not be started");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }

    private CurrentRaceDao getCurrentRaceDao() {
        if (currentRaceDao == null) {
            currentRaceDao = new CurrentRaceDaoImpl();
        }
        return currentRaceDao;
    }
    private RaceDao getRaceDao() {
        if (raceDao == null) {
            raceDao = new RaceDaoImpl();
        }
        return raceDao;
    }
}
