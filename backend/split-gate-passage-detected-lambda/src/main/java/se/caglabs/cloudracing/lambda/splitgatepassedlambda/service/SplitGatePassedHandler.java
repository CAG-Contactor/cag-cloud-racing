package se.caglabs.cloudracing.lambda.splitgatepassedlambda.service;

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

public class SplitGatePassedHandler {
    private static final String INPUT_PARAM = "timestamp";
    private static final String PREFIX_SUCCESS = "Success: ";
    private static final String PREFIX_EXCEPTION = "Exception: ";
    private static final String PREFIX_VALIDATION = "Validation error: ";
    private static final String MESSAGE_SUCCESS = "Race split time set successfully.";
    private static final String MESSAGE_TIMESTAMP_MISSING = "QueryString parameter timestamp is required.";
    private static final String MESSAGE_NO_CURRENT_RACE = "No race is ongoing.";
    private static final String MESSAGE_RACE_NOT_FOUND = "Could not find ongoing race.";
    private static final String MESSAGE_RACE_NOT_STARTED = "Race could not pass split gate due to invalid status.";

    private CurrentRaceDao currentRaceDao;
    private RaceDao raceDao;

    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request, Context context) {

        String timestampParam = request.getQueryStringParameters().get(INPUT_PARAM);
        if (timestampParam == null) {
            return validationError(MESSAGE_TIMESTAMP_MISSING);
        }
        try {
            Long timestamp = Long.parseLong(timestampParam);
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
            if (race.getStatus().equals(Race.Status.STARTED) &&
                    (race.getSplitTime() == null || race.getSplitTime() == 0)) {
                race.setSplitTime(timestamp);
                raceDao.saveRace(race);
                return success();
            } else {
                return validationError(MESSAGE_RACE_NOT_STARTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return serverError(e);
        }
    }

    private APIGatewayProxyResponseEvent success() {
        return response(200, PREFIX_SUCCESS + MESSAGE_SUCCESS);
    }

    private APIGatewayProxyResponseEvent validationError(String message) {
        return response(400, PREFIX_VALIDATION + message);
    }

    private APIGatewayProxyResponseEvent serverError(Exception e) {
        return response(500, PREFIX_EXCEPTION + e.getMessage());
    }

    private APIGatewayProxyResponseEvent response(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent().withStatusCode(statusCode).withBody(message);
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
