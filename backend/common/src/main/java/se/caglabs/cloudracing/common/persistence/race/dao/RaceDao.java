package se.caglabs.cloudracing.common.persistence.race.dao;

import se.caglabs.cloudracing.common.persistence.race.model.Race;

public interface RaceDao {
    class RaceQueueDaoException extends Error {
        public RaceQueueDaoException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    void saveRace(Race race);
}
