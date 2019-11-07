package se.caglabs.cloudracing.common.persistence.currentrace.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.sql.Timestamp;

@DynamoDBTable(tableName="current-race")
public class CurrentRace {

    private String id;
    private String raceId;

	public CurrentRace(String id, String raceId) {
		this.id = id;
		this.raceId = raceId;
	}

	@DynamoDBHashKey(attributeName="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName="raceId")
	public String getRaceId() {
		return this.raceId;
	}
	
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

}
