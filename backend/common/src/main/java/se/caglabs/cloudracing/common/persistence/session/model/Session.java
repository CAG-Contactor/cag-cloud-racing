package se.caglabs.cloudracing.common.persistence.session.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;
import lombok.NonNull;

@DynamoDBTable(tableName="sessions-STAGE")
@Data
public class Session {
    @DynamoDBHashKey(attributeName = "token")
    private String token;
    @DynamoDBAttribute(attributeName = "name")
    private String userName;

    public Session() {
    }

    public Session(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }
}
