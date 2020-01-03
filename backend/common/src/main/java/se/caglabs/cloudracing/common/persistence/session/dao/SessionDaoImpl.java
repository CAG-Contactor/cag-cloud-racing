package se.caglabs.cloudracing.common.persistence.session.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import se.caglabs.cloudracing.common.persistence.session.model.Session;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionDaoImpl implements SessionDao {

    private DynamoDBMapper mapper;

    public SessionDaoImpl() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    @Override
    public Session findSessionByToken(String token) {
        return this.mapper.load(Session.class, token);
    }

    @Override
    public List<Session> findSessionsForUser(String name) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":name", new AttributeValue().withS(name));
        return mapper.scan(Session.class, new DynamoDBScanExpression()
                .withFilterExpression("name = :name")
                .withExpressionAttributeValues(eav));
    }

    @Override
    public void saveSession(Session session) {
        this.mapper.save(session);
    }

    @Override
    public void deleteSession(Session session) {
        this.mapper.delete(session);
    }
}
