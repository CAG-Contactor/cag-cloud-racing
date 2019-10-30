package se.caglabs.cloudracing.common.persistence.race.dao;

import se.caglabs.cloudracing.common.persistence.race.model.Race;

import java.util.Optional;

public interface RaceDao {
    class RaceQueueDaoException extends Error {
        public RaceQueueDaoException(String message, Throwable cause) {
            super(message, cause);
        }

        public RaceQueueDaoException(String message) {
            super(message);
        }
    }

    Optional<Race> findById(String id);

    void saveRace(Race race);
}
