package se.caglabs.cloudracing.lambda.registercontestant.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;
import se.caglabs.cloudracing.lambda.registercontestant.exception.UserDaoException;
import se.caglabs.cloudracing.lambda.registercontestant.model.User;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private DynamoDBMapper mapper;

    public UserDaoImpl() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    public void saveUser(User newUser) throws UserDaoException {
        if(userExists(newUser)) {
            throw new UserDaoException("User already exists" + newUser.getName());
        } else {
            this.mapper.save(newUser);
        }
    }

    public User getUser(String name) {
        return this.mapper.load(User.class, name);
    }

    public List<User> listUsers() {
        return this.mapper.scan(User.class, new DynamoDBScanExpression());
    }

    private boolean userExists(User newUser) {
        List<User> allUsers = mapper.scan(User.class, new DynamoDBScanExpression());

        return allUsers.stream()
                .anyMatch(registeredUser -> registeredUser.getName().equals(newUser.getName()));
    }
}
