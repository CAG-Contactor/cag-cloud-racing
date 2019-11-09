package se.caglabs.cloudracing.common.persistence.race.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;

import java.util.Optional;


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

    @Override
    public Optional<Race> findById(String id) {
        Race key = new Race();
        key.setId(id);
        DynamoDBQueryExpression<Race> queryExpression = new DynamoDBQueryExpression<Race>()
                .withHashKeyValues(key);
        PaginatedQueryList<Race> query = this.mapper.query(Race.class, queryExpression);
        if (query.size() > 1) {
            throw new RaceDaoException("more than one race with id " + id);
        }
        return query.stream().findFirst();
    }

    @Override
    public boolean raceExist(String userName) {
        PaginatedScanList<Race> races = this.mapper.scan(Race.class, new DynamoDBScanExpression());

        return races.stream().anyMatch(race -> race.getUserName().equals(userName) && race.getStatus() == Race.Status.IDLE);
    }
}
