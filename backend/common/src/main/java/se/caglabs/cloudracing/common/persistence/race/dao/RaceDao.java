package se.caglabs.cloudracing.common.persistence.race.dao;

import se.caglabs.cloudracing.common.persistence.race.model.Race;

import java.util.List;
import java.util.Optional;

public interface RaceDao {
    boolean raceExist(String userName);

    class RaceDaoException extends Error {
        RaceDaoException(String message) {
            super(message);
        }
    }

    Optional<Race> findById(String id);

    Optional<Race> findIdleByUserName(String userName);

    List<Race> findAll();

    List<Race> findAllByUserName(String username);

    void saveRace(Race race);

}
