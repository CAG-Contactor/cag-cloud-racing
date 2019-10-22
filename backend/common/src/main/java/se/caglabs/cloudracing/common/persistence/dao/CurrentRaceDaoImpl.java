package se.caglabs.cloudracing.common.persistence.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import se.caglabs.cloudracing.common.persistence.model.CurrentRace;

import java.util.UUID;

public class CurrentRaceDaoImpl implements CurrentRaceDao {

	private AmazonDynamoDB client;
	private DynamoDBMapper mapper;

	public CurrentRaceDaoImpl() {
		this.client = AmazonDynamoDBClientBuilder.standard().build();
		this.mapper = new DynamoDBMapper(this.client);
	}

	public CurrentRace getCurrentRace(String id) {
		return this.mapper.load(CurrentRace.class, id);
	}

	public void saveCurrentRace(CurrentRace currentRace) {
		if(currentRace.getId() == null) {
			currentRace.setId(UUID.randomUUID().toString());
		}
		this.mapper.save(currentRace);
	}

	public void deleteCurrentRace(String id) {
		CurrentRace currentRace = this.getCurrentRace(id);
		if(currentRace != null) {
			this.mapper.delete(currentRace);
		}
	}
}
