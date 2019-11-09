package se.caglabs.cloudracing.common.persistence.racequeue.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;


public class RaceQueueDaoImpl implements RaceQueueDao{

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public RaceQueueDaoImpl() {
        this.client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(this.client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    @Override
    public void saveRaceInQueue(RaceQueue raceQueue) {
        this.mapper.save(raceQueue);
    }
}
