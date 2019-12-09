package se.caglabs.cloudracing.common.persistence.racequeue.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import se.caglabs.cloudracing.common.persistence.race.model.Race;
import se.caglabs.cloudracing.common.persistence.racequeue.model.RaceQueue;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class RaceQueueDaoImpl implements RaceQueueDao {

    private DynamoDBMapper mapper;

    public RaceQueueDaoImpl() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    @Override
    public List<RaceQueue> getRaceQueue() {
        return new ArrayList<>(this.mapper.scan(RaceQueue.class, new DynamoDBScanExpression()));
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

    @Override
    public Optional<RaceQueue> getNextRace() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<RaceQueue> races = this.mapper.scan(RaceQueue.class, scanExpression);
        return races.stream()
                .min(Comparator.comparingLong(RaceQueue::getCreateTime));
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
            throw new RaceQueueDaoException("more than one race with id " + id + " in the queue");
        }
        return query.stream().findFirst();

    }

    @Override
    public boolean raceExist(String userName) {
        PaginatedScanList<Race> races = this.mapper.scan(Race.class, new DynamoDBScanExpression());

        return races.stream().anyMatch(race -> race.getUserName().equals(userName) && race.getRaceStatus() == Race.RaceStatus.IDLE);
    }
}
