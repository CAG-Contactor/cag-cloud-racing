package se.caglabs.cloudracing.common.persistence.registeredcontestant.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registeredcontestant.model.User;
import se.caglabs.cloudracing.common.persistence.stuff.StageNameTableNameResolver;

import java.util.List;

@Slf4j
public class UserDaoImpl implements UserDao {

    private DynamoDBMapper mapper;

    public UserDaoImpl() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(client, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new StageNameTableNameResolver()).build());
    }

    public void saveUser(User newUser) throws UserDaoException {
        if(userExists(newUser)) {
            throw new UserDaoException("User with name [" + newUser.getName() + "] already exists!");
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

    public boolean userExist(String name) {
        return listUsers().stream().anyMatch(user -> user.getName().equals(name));
    }

    private boolean userExists(User newUser) {
        User user = mapper.load(User.class, newUser.getName());

        return user != null;
    }
}
