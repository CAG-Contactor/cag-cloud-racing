package se.caglabs.cloudracing.common.persistence.race.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;


@DynamoDBTable(tableName="races-STAGE")
@Data
public class Race {
    public enum Status {
        IDLE, ARMED, STARTED, FINISHED, ABORTED
    }

    @DynamoDBHashKey(attributeName="id")
    @DynamoDBAutoGeneratedKey()
    private String id;
    @DynamoDBAttribute(attributeName="userName")
    private String userName;
    @DynamoDBAttribute(attributeName="status")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private Status status;
    @DynamoDBAttribute(attributeName="startTime")
    private Long startTime;
    @DynamoDBAttribute(attributeName="splitTime")
    private Long splitTime;
    @DynamoDBAttribute(attributeName="finishTime")
    private Long finishTime;
    @DynamoDBAttribute(attributeName="createTime")
    private Long createTime;
}
