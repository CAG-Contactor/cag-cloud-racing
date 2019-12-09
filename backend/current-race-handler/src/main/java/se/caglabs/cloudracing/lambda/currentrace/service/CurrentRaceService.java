package se.caglabs.cloudracing.lambda.currentrace.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

@Slf4j
public class CurrentRaceService {

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;
    private RaceQueueDao raceQueueDao;
    private ObjectMapper mapper;


    CurrentRaceService() {
        this.currentRaceDao = new CurrentRaceDaoImpl();
        this.raceDao = new RaceDaoImpl();
        this.raceQueueDao = new RaceQueueDaoImpl();
        mapper = new ObjectMapper();
    }

    public APIGatewayProxyResponseEvent armRace(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        CurrentRace currentRace = currentRaceDao
                .getCurrentRace()
                .orElse(null);
        if (currentRace != null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("There is already a current race present");
        }
        RaceQueue nextRace = raceQueueDao
                .getNextRace()
                .orElse(null);
        if (nextRace == null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("There is no pending race");
        }
        Race race = raceDao.findById(nextRace.getRaceId()).orElse(null);
        if (race == null) {
            return new APIGatewayProxyResponseEvent().withStatusCode(409).withBody("Could not find the race object");
        }
        raceQueueDao.remove(race);
        race.setRaceStatus(Race.RaceStatus.ARMED);
        raceDao.saveRace(race);
        currentRaceDao.saveCurrentRace(new CurrentRace(nextRace.getRaceId()));
        return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody(mapper.writeValueAsString(race));
    }

    APIGatewayProxyResponseEvent abortActiveRace(APIGatewayProxyRequestEvent request) {
        Optional<CurrentRace> currentRace = currentRaceDao.getCurrentRace();
        return currentRace.map(currRace -> {
            Optional<Race> race = raceDao.findById(currRace.getRaceId());
            return race.map(r -> {
                r.setCreateTime(null);
                r.setFinishTime(null);
                r.setSplitTime(null);
                r.setStartTime(null);
                r.setRaceStatus(Race.RaceStatus.ABORTED);
                raceDao.saveRace(r);
                currentRaceDao.deleteCurrentRace();
                return new APIGatewayProxyResponseEvent().withStatusCode(201).withBody("OK");
            }).orElse(new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Race could not be found"));
        }).orElse(new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("No ongoing race could be found"));
    }

    public APIGatewayProxyResponseEvent getCurrentRace() {
        Optional<CurrentRace> currentRace = currentRaceDao.getCurrentRace();
        if (currentRace.isPresent()) {
            Optional<Race> race = raceDao.findById(currentRace.get().getRaceId());
            if (race.isPresent()) {
                try {
                    return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(mapper.writeValueAsString(race.get()));
                } catch (JsonProcessingException e) {
                    return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Unexpected error mapping result");
                }
            }
        }
        return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("No ongoing race could be found");
    }
}
