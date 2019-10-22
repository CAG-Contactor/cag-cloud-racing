package se.caglabs.cloudracing.lambda.registercontestant.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;
import se.caglabs.cloudracing.lambda.registercontestant.model.User;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public UserDaoImpl() {
        this.client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(this.client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    public List<User> listUsers() {
        return this.mapper.scan(User.class, new DynamoDBScanExpression());
    }

    public User getUser(String name) {
        return this.mapper.load(User.class, name);
    }

    public void saveUser(User user) {
        this.mapper.save(user);
    }

    public void deleteUser(String name) {
        User user = this.getUser(name);
        if(user != null) {
            this.mapper.delete(user );
        }
    }
}
