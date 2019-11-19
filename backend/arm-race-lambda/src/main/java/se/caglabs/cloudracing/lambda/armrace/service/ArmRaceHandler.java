package se.caglabs.cloudracing.lambda.armrace.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDao;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDao;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDaoImpl;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;

@Slf4j
@NoArgsConstructor
public class ArmRaceHandler {
    private RaceQueueDao raceQueueDao;

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;
    private ArmRaceHandler(RaceQueueDao raceQueueDao, CurrentRaceDao currentRaceDao, RaceDao raceDao) {
        this.raceQueueDao = raceQueueDao;
        this.currentRaceDao = currentRaceDao;
        this.raceDao = raceDao;
    }

    public APIGatewayProxyResponseEvent armRace(APIGatewayProxyRequestEvent request) {
        CurrentRace currentRace = getCurrentRaceDao()
                .getCurrentRace()
                .orElse(null);
        if (currentRace != null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("There is already a current race present");
        }

        RaceQueue nextRace = getRaceQueueDao()
                .getNextRace()
                .orElse(null);
        if (nextRace == null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("There is no pending race");
        }

        Race race = getRaceDao().findById(nextRace.getRaceId()).orElse(null);
        if (race == null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("Could not find the race object");
        }

        getRaceQueueDao().remove(race);
        race.setRaceStatus(Race.RaceStatus.ARMED);
        raceDao.saveRace(race);

        getCurrentRaceDao().saveCurrentRace(new CurrentRace(nextRace.getRaceId()));

        return new APIGatewayProxyResponseEvent().withStatusCode(201);
    }

    private RaceQueueDao getRaceQueueDao() {
        if (raceQueueDao == null) {
            raceQueueDao = new RaceQueueDaoImpl();
        }
        return raceQueueDao;
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
