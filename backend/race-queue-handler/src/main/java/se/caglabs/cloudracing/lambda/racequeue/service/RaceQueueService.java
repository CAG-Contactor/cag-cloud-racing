package se.caglabs.cloudracing.lambda.racequeue.service;

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
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registereduser.dao.UserDaoImpl;

import java.util.Optional;

@Slf4j
public class RaceQueueService {

    private UserDao userDao;
    private RaceDao raceDao;
    private RaceQueueDao raceQueueDao;
    private ObjectMapper mapper;

    RaceQueueService() {
        this.userDao = new UserDaoImpl();
        this.raceDao = new RaceDaoImpl();
        this.raceQueueDao = new RaceQueueDaoImpl();
        this.mapper = new ObjectMapper();
    }

    APIGatewayProxyResponseEvent getRaceQueue() {
        String json;
        try {
            json = mapper.writeValueAsString(raceQueueDao.getRaceQueue());
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Failed to convert value to json");
        }
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(json);
    }

    APIGatewayProxyResponseEvent bailOut(APIGatewayProxyRequestEvent request) {
        UserDTO userDTO;
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            userDTO = mapper.readValue(body, UserDTO.class);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("name required to bail out of race");
        }

        Optional<Race> raceToBailOut = raceDao.findByUserName(userDTO.getName());
        if (raceToBailOut.isPresent()) {
            Race race = raceToBailOut.get();
            race.setRaceStatus(Race.RaceStatus.ABORTED);
            raceDao.saveRace(race);
            raceQueueDao.removeFromQueue(race.getId());

            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("User bailed out: " +
                    userDTO.getName());
        } else {
            return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Race for user: " +
                    userDTO.getName() + " with state IDLE not found");
        }
    }

    APIGatewayProxyResponseEvent signUpForRace(APIGatewayProxyRequestEvent request) {
        String body = request.getBody();
        UserDTO user;
        try {
            user = mapper.readValue(body, UserDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("User name needed to add a race");
        }
        if (!userDao.userExist(user.getName())) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("User with name " +
                    user.getName() + " not registered");
        }
        if (raceQueueDao.raceExist(user.getName())) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Race already exist for user " +
                    user.getName());
        }
        saveRace(Race.builder().userName(user.getName()).build());
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Race added for user " + user.getName());
    }

    private void saveRace(Race race) {
        race.setRaceStatus(Race.RaceStatus.IDLE);
        race.setCreateTime(System.currentTimeMillis());
        raceDao.saveRace(race);

        RaceQueue raceQueue = RaceQueue.builder()
                .raceId(race.getId())
                .createTime(race.getCreateTime())
                .build();

        raceQueueDao.saveRaceInQueue(raceQueue);
    }
}
