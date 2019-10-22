package se.caglabs.cloudracing.common.persistence.stuff;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class StageNameTableNameResolver extends DynamoDBMapperConfig.DefaultTableNameResolver {
    @Override
    public String getTableName(Class<?> clazz, DynamoDBMapperConfig config) {
        String stageName = System.getenv("Stage");
        String rawTableName = super.getTableName(clazz, config);
        return rawTableName.replace("STAGE", stageName);
    }
}
