package se.caglabs.cloudracing.common.persistence.racequeue.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName="race-queue-STAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceQueue {

    @DynamoDBHashKey(attributeName="raceId")
    private String raceId;
    @DynamoDBAttribute(attributeName="createTime")
    private Long createTime;
    @DynamoDBAttribute(attributeName="userName")
    private String userName;
}
