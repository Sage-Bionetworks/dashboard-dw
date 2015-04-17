package org.sagebionetworks.dashboard.dao.dynamo;

import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeDynamoDao;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

@Repository("bridgeDynamoDao")
public class BridgeDynamoDaoImpl implements BridgeDynamoDao {

    @Resource
    private DwConfig dwConfig;

    @Resource
    private AmazonDynamoDB dynamoClient;

    @Override
    public String getCreateTableSql(final String dynamoTableName) {
        final String prefix = dwConfig.getBridgeStack() + "-" + dwConfig.getBridgeUser() + "-";
        final String tableName = prefix + dynamoTableName;
        final DescribeTableResult result = dynamoClient.describeTable(tableName);
        final TableDescription table = result.getTable();
        final List<KeySchemaElement> keySchema = table.getKeySchema();
        for (KeySchemaElement key : keySchema) {
            System.out.println(key.getAttributeName());
            System.out.println(key.getKeyType());
        }
        final List<AttributeDefinition> attrs = table.getAttributeDefinitions();
        for (AttributeDefinition attr : attrs) {
            System.out.println(attr.getAttributeName());
            System.out.println(attr.getAttributeType());
        }
        return null;
    }
}
