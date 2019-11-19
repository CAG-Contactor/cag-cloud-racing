package se.caglabs.cloudracing.common.persistence.race.dao;

import se.caglabs.cloudracing.common.persistence.race.model.Race;

import java.util.Optional;

public interface RaceDao {
    boolean raceExist(String userName);

    class RaceDaoException extends Error {
        public RaceDaoException(String message, Throwable cause) {
            super(message, cause);
        }

        RaceDaoException(String message) {
            super(message);
        }
    }

    Optional<Race> findById(String id);

    Optional<Race> findByUserName(String userName);

    void saveRace(Race race);
}
