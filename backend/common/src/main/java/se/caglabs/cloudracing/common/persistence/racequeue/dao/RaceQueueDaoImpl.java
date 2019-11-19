package se.caglabs.cloudracing.common.persistence.racequeue.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;


public class RaceQueueDaoImpl implements RaceQueueDao {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public RaceQueueDaoImpl() {
        this.client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(this.client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    @Override
    public void removeFromQueue(String id) {
        RaceQueue key = new RaceQueue();
        key.setRaceId(id);
        DynamoDBQueryExpression<RaceQueue> queryExpression = new DynamoDBQueryExpression<RaceQueue>()
                .withHashKeyValues(key);
        PaginatedQueryList<RaceQueue> query = this.mapper.query(RaceQueue.class, queryExpression);
        query.stream().findFirst().ifPresent(raceQueue ->
                this.mapper.delete(raceQueue));
    }

    @Override
    public void saveRaceInQueue(RaceQueue raceQueue) {
        this.mapper.save(raceQueue);
    }
}
