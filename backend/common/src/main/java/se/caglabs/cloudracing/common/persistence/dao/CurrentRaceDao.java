package se.caglabs.cloudracing.common.persistence.dao;

import se.caglabs.cloudracing.common.persistence.model.CurrentRace;

public interface CurrentRaceDao {

	CurrentRace getCurrentRace(String id);

	void saveCurrentRace(CurrentRace currentRace);

	void deleteCurrentRace(String id);

}
