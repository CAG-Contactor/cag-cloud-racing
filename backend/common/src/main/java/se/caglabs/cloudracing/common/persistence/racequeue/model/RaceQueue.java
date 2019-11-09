package se.caglabs.cloudracing.common.persistence.racequeue.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@DynamoDBTable(tableName="race-queue-STAGE")
@Data
public class RaceQueue {

    @DynamoDBHashKey(attributeName="raceId")
    private String raceId;
}
