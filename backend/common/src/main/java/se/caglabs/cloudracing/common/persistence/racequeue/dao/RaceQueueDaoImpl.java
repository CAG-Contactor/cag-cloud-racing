package se.caglabs.cloudracing.common.persistence.racequeue.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;

import java.util.Comparator;
import java.util.Optional;


public class RaceQueueDaoImpl implements RaceQueueDao {

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

    @Override
    public Optional<RaceQueue> getNextRace() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<RaceQueue> races = this.mapper.scan(RaceQueue.class, scanExpression);
        return races.stream()
                .max(Comparator.comparingLong(RaceQueue::getCreateTime));
    }

    @Override
    public void remove(Race race) {
        findById(race.getId())
                .ifPresent(r -> mapper.delete(r));
    }

    @Override
    public Optional<RaceQueue> findById(String id) {
        RaceQueue key = RaceQueue.builder().raceId(id).build();
        DynamoDBQueryExpression<RaceQueue> queryExpression = new DynamoDBQueryExpression<RaceQueue>()
                .withHashKeyValues(key);
        PaginatedQueryList<RaceQueue> query = this.mapper.query(RaceQueue.class, queryExpression);
        if (query.size() > 1) {
            throw new RaceQueueDaoException("more than one race with id " + id);
        }
        return query.stream().findFirst();

    }

    @Override
    public boolean raceExist(String userName) {
        PaginatedScanList<Race> races = this.mapper.scan(Race.class, new DynamoDBScanExpression());

        return races.stream().anyMatch(race -> race.getUserName().equals(userName) && race.getStatus() == Race.Status.IDLE);
    }
}
