package se.caglabs.cloudracing.lambda.bailoutfromrace.service;
import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDao;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDaoImpl;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;
import se.caglabs.cloudracing.common.restpayload.UserNamePayload;

import java.util.Optional;

@Slf4j
public class BailOutFromRaceHandler {
    private RaceDao raceDao;
    private RaceQueueDao raceQueueDao;
    private static int idNo = 1;

    public APIGatewayProxyResponseEvent bailOutFromRace(APIGatewayProxyRequestEvent request, Context context) {
        UserNamePayload userNamePayload = null;
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            userNamePayload = mapper.readValue(body, UserNamePayload.class);
            log.info("User bailing out, name: " + userNamePayload.getUserName());
        } catch (JsonProcessingException  e) {
            e.printStackTrace();
            log.warn("Error bailing out user, no userName", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("userName required to bail out of race!");
        }

        // mockupRace(userNamePayload.getUserName());

        Optional<Race> raceToBailOut = this.getRaceDao().findByUserName(userNamePayload.getUserName());
        if (raceToBailOut.isPresent()) {
            // change state IDLE to ABORTED in races
            Race race = raceToBailOut.get();
            race.setRaceStatus(Race.RaceStatus.ABORTED);
            getRaceDao().saveRace(race);
            // remove from race-queue
            getRaceQueueDao().removeFromQueue(race.getId());
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("User bailed out OK: " +
                    userNamePayload.getUserName());
        } else {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Race for username: " +
                    userNamePayload.getUserName() + " not found!");
        }
    }


    private void mockupRace(String userName) {
        Race race = new Race();
        race.setId("id" + Integer.toString(idNo++));
        race.setUserName(userName);
        race.setRaceStatus(Race.RaceStatus.IDLE);
        race.setCreateTime(System.currentTimeMillis());
        race.setStartTime(System.currentTimeMillis());
        race.setSplitTime(System.currentTimeMillis());
        race.setFinishTime(System.currentTimeMillis());
        this.getRaceDao().saveRace(race);

        RaceQueue raceQueue = new RaceQueue();
        raceQueue.setRaceId(race.getId());
        this.getRaceQueueDao().saveRaceInQueue(raceQueue);
    }

    private RaceDao getRaceDao() {
        if (this.raceDao == null) {
            this.raceDao = new RaceDaoImpl();
        }
        return this.raceDao;
    }

    private RaceQueueDao getRaceQueueDao() {
        if (this.raceQueueDao == null) {
            this.raceQueueDao = new RaceQueueDaoImpl();
        }
        return this.raceQueueDao;
    }
}
