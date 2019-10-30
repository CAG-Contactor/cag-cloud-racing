package se.caglabs.cloudracing.lambda.finishgatepassedlambda.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDao;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;

import java.sql.Timestamp;
import java.util.Optional;

public class FinishGatePassedHandler {

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;

    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {
        String timeString = request.getQueryStringParameters().get("timestamp");
        if (timeString == null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("timestamp required");
        }
        try {
            Integer timestamp = Integer.parseInt(timeString);
            Optional<CurrentRace> currentRaceOpt = getCurrentRaceDao().getCurrentRace();
            if (! currentRaceOpt.isPresent()) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("no race is ongoing");
            }
            CurrentRace currentRace = currentRaceOpt.get();
            Optional<Race> raceOpt = raceDao.findById(currentRace.getRaceId());
            if (! raceOpt.isPresent()) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("could not find ongoing race");
            }
            Race race = raceOpt.get();
            if (! race.getStatus().equals(Race.Status.STARTED)) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("race is not started");
            }
            if (race.getStartTime() == null || race.getSplitTime() == null) {
                return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("start and/or middle gate not passed");
            }
            race.setFinishTime(timestamp);
            race.setStatus(Race.Status.FINISHED);
            raceDao.saveRace(race);
            currentRaceDao.deleteCurrentRace();
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("OK");
        } catch (NumberFormatException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("timestamp should be numeric");
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
