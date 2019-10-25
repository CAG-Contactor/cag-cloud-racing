package se.caglabs.cloudracing.common.persistence.currentrace.dao;

import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;

import java.util.Optional;

public interface CurrentRaceDao {
    public static class CurrentRaceDaoException extends Error {
		public CurrentRaceDaoException(String message, Throwable cause) {
			super(message, cause);
		}
	}

    Optional<CurrentRace> getCurrentRace();

    void saveCurrentRace(CurrentRace currentRace) throws CurrentRaceDaoException;

    void deleteCurrentRace();

}
