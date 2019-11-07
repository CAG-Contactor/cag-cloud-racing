package se.caglabs.cloudracing.common.persistence.currentrace.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName="current-race-STAGE")
@NoArgsConstructor
public class CurrentRace {

    private String raceId;

	public CurrentRace(String raceId) {
		this.raceId = raceId;
	}

	@DynamoDBHashKey(attributeName="raceId")
	public String getRaceId() {
		return this.raceId;
	}
	
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

}
