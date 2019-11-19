package se.caglabs.cloudracing.common.persistence.racequeue.dao;

import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;


public interface RaceQueueDao {
    class RaceQueueDaoException extends Error {
        RaceQueueDaoException(String message) {
            super(message);
        }
    }

    void removeFromQueue(String id);

    void saveRaceInQueue(RaceQueue raceQueue);
}
