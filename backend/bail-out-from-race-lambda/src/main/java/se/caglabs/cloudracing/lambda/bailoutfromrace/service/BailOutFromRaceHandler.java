package se.caglabs.cloudracing.lambda.bailoutfromrace.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.dto.UserDTO;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDao;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDaoImpl;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;

import java.util.Optional;

@Slf4j
public class BailOutFromRaceHandler {
    private RaceDao raceDao;
    private RaceQueueDao raceQueueDao;
    private static int idNo = 1;

    public APIGatewayProxyResponseEvent bailOutFromRace(APIGatewayProxyRequestEvent request, Context context) {
        UserDTO userDTO = null;
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            userDTO = mapper.readValue(body, UserDTO.class);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("name required to bail out of race");
        }

        //mockupRace(userDTO.getName());

        Optional<Race> raceToBailOut = getRaceDao().findByUserName(userDTO.getName());
        if (raceToBailOut.isPresent()) {
            Race race = raceToBailOut.get();
            race.setRaceStatus(Race.RaceStatus.ABORTED);
            getRaceDao().saveRace(race);
            getRaceQueueDao().removeFromQueue(race.getId());
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("User bailed out: " +
                    userDTO.getName());
        } else {
            return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Race for user: " +
                    userDTO.getName() + " with state IDLE not found");
        }
    }

    private RaceDao getRaceDao() {
        if (raceDao == null) {
            raceDao = new RaceDaoImpl();
        }
        return raceDao;
    }

    private RaceQueueDao getRaceQueueDao() {
        if (raceQueueDao == null) {
            raceQueueDao = new RaceQueueDaoImpl();
        }
        return raceQueueDao;
    }

    private void mockupRace(String name) {
        Race race = new Race();
        race.setId("id" + Integer.toString(idNo++));
        race.setUserName(name);
        race.setRaceStatus(Race.RaceStatus.IDLE);
        race.setCreateTime(System.currentTimeMillis());
        race.setStartTime(System.currentTimeMillis());
        race.setSplitTime(System.currentTimeMillis());
        race.setFinishTime(System.currentTimeMillis());
        getRaceDao().saveRace(race);

        RaceQueue raceQueue = new RaceQueue();
        raceQueue.setRaceId(race.getId());
        getRaceQueueDao().saveRaceInQueue(raceQueue);
    }
}
