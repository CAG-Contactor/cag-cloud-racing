package se.caglabs.cloudracing.lambda.signupforrace.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.caglabs.cloudracing.common.dto.UserDTO;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDao;
import se.caglabs.cloudracing.common.persistence.racequeue.dao.RaceQueueDaoImpl;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDao;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.dao.UserDaoImpl;


public class SignUpForRaceHandler {
    private UserDao userDao;
    private RaceDao raceDao;
    private RaceQueueDao raceQueueDao;

    public APIGatewayProxyResponseEvent signUpForRace(APIGatewayProxyRequestEvent request, Context context) {
        String body = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Stage: " + System.getenv("Stage"));
        UserDTO user;
        try {
            user = mapper.readValue(body, UserDTO.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("User name needed to add a race");
        }

        if (!this.getUserDao().userExist(user.getName())) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("User with name " +
                    user.getName() + " not registered");
        }

        if (this.getRaceQueueDao().raceExist(user.getName())) {
            return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("Race already exist for user " +
                    user.getName());
        }

        saveRace(Race.builder().userName(user.getName()).build());

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Race added for user " + user.getName());
    }

    private void saveRace(Race race) {
        race.setRaceStatus(Race.RaceStatus.IDLE);
        race.setCreateTime(System.currentTimeMillis());
        this.getRaceDao().saveRace(race);

        RaceQueue raceQueue = RaceQueue.builder()
                .raceId(race.getId())
                .createTime(race.getCreateTime())
                .build();

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

    private UserDao getUserDao() {
        if (this.userDao == null) {
            this.userDao = new UserDaoImpl();
        }
        return this.userDao;
    }
}
