package se.caglabs.cloudracing.common.persistence.currentrace.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import se.caglabs.cloudracing.common.persistence.currentrace.model.CurrentRace;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;

import java.util.Optional;
import java.util.UUID;

public class CurrentRaceDaoImpl implements CurrentRaceDao {

    private DynamoDBMapper mapper;

    public CurrentRaceDaoImpl() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    public Optional<CurrentRace> getCurrentRace() {
        PaginatedScanList<CurrentRace> races = this.mapper.scan(CurrentRace.class, new DynamoDBScanExpression());
        if (races.size() == 1) {
            return Optional.of(races.get(0));
        } else if (races.size() == 0) {
            return Optional.empty();
        } else {
            throw new CurrentRaceDaoException("There are more than 1 current races", null);
        }
    }

    public void saveCurrentRace(CurrentRace currentRace) {
        PaginatedScanList<CurrentRace> races = this.mapper.scan(CurrentRace.class, new DynamoDBScanExpression());
        if (races.size() > 0) {
            throw new CurrentRaceDaoException("Tried to create a new current race when there already exist one", null);
        }

        if (currentRace.getRaceId() == null) {
            currentRace.setRaceId(UUID.randomUUID().toString());
        }

        this.mapper.save(currentRace);
    }

    public void deleteCurrentRace() {
        this.getCurrentRace().ifPresent(currentRace -> {
            this.mapper.delete(currentRace);
        });
    }
}
