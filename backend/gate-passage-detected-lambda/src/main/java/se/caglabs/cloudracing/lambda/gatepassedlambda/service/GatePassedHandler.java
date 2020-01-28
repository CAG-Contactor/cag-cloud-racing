package se.caglabs.cloudracing.lambda.gatepassedlambda.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDao;
import se.caglabs.cloudracing.common.persistence.currentrace.dao.CurrentRaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDao;
import se.caglabs.cloudracing.common.persistence.race.dao.RaceDaoImpl;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.stuff.CorsHeaders;

import java.util.Objects;
import java.util.Optional;


public class GatePassedHandler {

    private static final String INPUT_TIMESTAMP = "timestamp";
    private static final String INPUT_SENSOR_ID = "sensorID";
    private static final String SENSOR_ID_START = "START";
    private static final String SENSOR_ID_SPLIT = "SPLIT";
    private static final String SENSOR_ID_FINISH = "FINISH";
    private static final String PREFIX_SUCCESS = "Success: ";
    private static final String PREFIX_EXCEPTION = "Exception: ";
    private static final String PREFIX_VALIDATION = "Validation error: ";
    private static final String MESSAGE_SUCCESS = "Race time set successfully for sensor: ";
    private static final String MESSAGE_INVALID_SENSOR_ID = "The sensor-id is invalid.";
    private static final String MESSAGE_SENSOR_ID_MISSING = "QueryString parameter sensor-id is required.";
    private static final String MESSAGE_TIMESTAMP_MISSING = "QueryString parameter timestamp is required.";
    private static final String MESSAGE_NO_CURRENT_RACE = "No race is ongoing.";
    private static final String MESSAGE_RACE_NOT_FOUND = "Could not find ongoing race.";
    private static final String MESSAGE_RACE_NOT_STARTED_FOR_FINISH = "Race could not pass finish gate due to invalid status.";
    private static final String MESSAGE_RACE_NOT_ONGOING_FOR_FINISH = "Race could not pass finish gate due to start and/or middle gate not passed.";
    private static final String MESSAGE_RACE_NOT_STARTED = "Race could not pass split gate due to invalid status.";
    private static final String MESSAGE_RACE_NOT_ARMED = "Race could not be started due to invalid status.";

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;

    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {
        if (Objects.equals(request.getResource(), "ping")) {
            System.out.println("Got ping");
            return new APIGatewayProxyResponseEvent().withStatusCode(200);
        }

        String sensorIdParam = request.getQueryStringParameters().get(INPUT_SENSOR_ID);
        String timestampParam = request.getQueryStringParameters().get(INPUT_TIMESTAMP);

        if (sensorIdParam == null) {
            return validationError(MESSAGE_SENSOR_ID_MISSING);
        } else if (timestampParam == null) {
            return validationError(MESSAGE_TIMESTAMP_MISSING);
        }

        try {
            Long timestamp = Long.parseLong(timestampParam);

            switch (sensorIdParam) {
                case SENSOR_ID_START: return startGatePassed(timestamp);
                case SENSOR_ID_SPLIT: return splitGatePassed(timestamp);
                case SENSOR_ID_FINISH: return finishGatePassed(timestamp);
                default: return validationError(MESSAGE_INVALID_SENSOR_ID);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return serverError(e);
        }
    }

    private APIGatewayProxyResponseEvent startGatePassed(Long timestamp) {
        Optional<CurrentRace> currentRaceOpt = getCurrentRaceDao().getCurrentRace();
        if (!currentRaceOpt.isPresent()) {
            return validationError(MESSAGE_NO_CURRENT_RACE);
        }
        CurrentRace currentRace = currentRaceOpt.get();
        Optional<Race> raceOpt = getRaceDao().findById(currentRace.getRaceId());
        if (!raceOpt.isPresent()) {
            return validationError(MESSAGE_RACE_NOT_FOUND);
        }
        Race race = raceOpt.get();
        if (race.getRaceStatus().equals(Race.RaceStatus.ARMED)) {
            race.setStartTime(timestamp);
            race.setRaceStatus(Race.RaceStatus.STARTED);
            raceDao.saveRace(race);
            return success(SENSOR_ID_START);
        } else {
            return validationError(MESSAGE_RACE_NOT_ARMED);
        }
    }

    private APIGatewayProxyResponseEvent splitGatePassed(Long timestamp) {
        Optional<CurrentRace> currentRaceOpt = getCurrentRaceDao().getCurrentRace();
        if (!currentRaceOpt.isPresent()) {
            return validationError(MESSAGE_NO_CURRENT_RACE);
        }
        CurrentRace currentRace = currentRaceOpt.get();
        Optional<Race> raceOpt = getRaceDao().findById(currentRace.getRaceId());
        if (!raceOpt.isPresent()) {
            return validationError(MESSAGE_RACE_NOT_FOUND);
        }
        Race race = raceOpt.get();
        if (race.getRaceStatus().equals(Race.RaceStatus.STARTED) && isNullOrZero(race.getSplitTime())) {
            race.setSplitTime(timestamp);
            raceDao.saveRace(race);
            return success(SENSOR_ID_SPLIT);
        } else {
            return validationError(MESSAGE_RACE_NOT_STARTED);
        }
    }

    private APIGatewayProxyResponseEvent finishGatePassed(Long timestamp) {
        Optional<CurrentRace> currentRaceOpt = getCurrentRaceDao().getCurrentRace();
        if (!currentRaceOpt.isPresent()) {
            return validationError(MESSAGE_NO_CURRENT_RACE);
        }
        CurrentRace currentRace = currentRaceOpt.get();
        Optional<Race> raceOpt = getRaceDao().findById(currentRace.getRaceId());
        if (!raceOpt.isPresent()) {
            return validationError(MESSAGE_RACE_NOT_FOUND);
        }
        Race race = raceOpt.get();
        if (!race.getRaceStatus().equals(Race.RaceStatus.STARTED)) {
            return validationError(MESSAGE_RACE_NOT_STARTED_FOR_FINISH);
        }
        if (isNullOrZero(race.getStartTime()) || isNullOrZero(race.getSplitTime())) {
            return validationError(MESSAGE_RACE_NOT_ONGOING_FOR_FINISH);
        }
        race.setFinishTime(timestamp);
        race.setRaceStatus(Race.RaceStatus.FINISHED);
        getRaceDao().saveRace(race);
        getCurrentRaceDao().deleteCurrentRace();
        return success(SENSOR_ID_FINISH);
    }

    private APIGatewayProxyResponseEvent success(String suffix) {
        return response(200, PREFIX_SUCCESS + MESSAGE_SUCCESS + suffix);
    }

    private APIGatewayProxyResponseEvent validationError(String message) {
        return response(400, PREFIX_VALIDATION + message);
    }

    private APIGatewayProxyResponseEvent serverError(Exception e) {
        return response(500, PREFIX_EXCEPTION + e.getMessage());
    }

    private APIGatewayProxyResponseEvent response(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent().withHeaders(new CorsHeaders()).withStatusCode(statusCode).withBody(message);
    }

    private boolean isNullOrZero(Long value) {
        return value == null || value == 0L;
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
