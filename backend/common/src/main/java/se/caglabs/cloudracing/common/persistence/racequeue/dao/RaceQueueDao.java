package se.caglabs.cloudracing.common.persistence.racequeue.dao;

import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;

import java.util.Optional;


public interface RaceQueueDao {


    class RaceQueueDaoException extends Error {
        RaceQueueDaoException(String message) {
            super(message);
        }
    }

    void removeFromQueue(String id);

    void saveRaceInQueue(RaceQueue raceQueue);

    Optional<RaceQueue> getNextRace();

    void remove(Race race);

    Optional<RaceQueue> findById(String id);

    boolean raceExist(String userId);
}
