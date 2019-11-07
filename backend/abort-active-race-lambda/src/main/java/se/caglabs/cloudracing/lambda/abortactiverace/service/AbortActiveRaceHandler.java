package se.caglabs.cloudracing.lambda.abortactiverace.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDao;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;

import java.util.Optional;

public class AbortActiveRaceHandler {

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;

    public APIGatewayProxyResponseEvent abortActiveRace(APIGatewayProxyRequestEvent request, Context context) {
        CurrentRaceDao currentRaceDao = getCurrentRaceDao();
        Optional<CurrentRace> currentRace = currentRaceDao.getCurrentRace();
        RaceDao raceDao = getRaceDao();

        return currentRace.map(currRace -> {
            Optional<Race> race = raceDao.findById(currRace.getRaceId());
            return race.map(r -> {
                r.setCreateTime(null);
                r.setFinishTime(null);
                r.setSplitTime(null);
                r.setStartTime(null);
                r.setStatus(Race.Status.ABORTED);
                raceDao.saveRace(r);
                currentRaceDao.deleteCurrentRace();
                return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody("OK");
            }).orElse(new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Race could not be found"));
        }).orElse(new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("No ongoing race could be found"));
    }

    private CurrentRaceDao getCurrentRaceDao() {
        if (this.currentRaceDao == null) {
            this.currentRaceDao = new CurrentRaceDaoImpl();
        }
        return this.currentRaceDao;
    }

    private RaceDao getRaceDao() {
        if (this.raceDao == null) {
            this.raceDao = new RaceDaoImpl();
        }
        return this.raceDao;
    }
}
