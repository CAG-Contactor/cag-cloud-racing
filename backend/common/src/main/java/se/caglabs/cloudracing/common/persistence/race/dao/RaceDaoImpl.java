package se.caglabs.cloudracing.common.persistence.race.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;


public class RaceDaoImpl implements RaceDao {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public RaceDaoImpl() {
        this.client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(this.client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    @Override
    public void saveRace(Race race) {
        this.mapper.save(race);
    }
}
