package se.caglabs.cloudracing.common.persistence.registereduser.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import lombok.extern.slf4j.Slf4j;
import se.caglabs.cloudracing.common.persistence.registereduser.exception.UserDaoException;
import se.caglabs.cloudracing.common.persistence.registereduser.model.User;
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

    @Override
    public void saveUser(User newUser) throws UserDaoException {
        if(userExists(newUser)) {
            throw new UserDaoException("User with name [" + newUser.getName() + "] already exists!");
        } else {
            this.mapper.save(newUser);
        }
    }

    @Override
    public User getUser(String name) {
        return this.mapper.load(User.class, name);
    }

    @Override
    public List<User> listUsers() {
        return this.mapper.scan(User.class, new DynamoDBScanExpression());
    }

    @Override
    public boolean userExist(String name) {
        return listUsers().stream().anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public void deleteUser(User name) {
        this.mapper.delete(name);
    }

    private boolean userExists(User newUser) {
        User user = mapper.load(User.class, newUser.getName());

        return user != null;
    }
}
