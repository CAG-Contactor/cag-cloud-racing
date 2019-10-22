package se.caglabs.cloudracing.common.persistence.currentrace.dao;

import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;

public interface CurrentRaceDao {

	CurrentRace getCurrentRace(String id);

	void saveCurrentRace(CurrentRace currentRace);

	void deleteCurrentRace(String id);

}
