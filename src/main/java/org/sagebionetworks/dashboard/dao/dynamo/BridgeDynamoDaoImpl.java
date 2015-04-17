package org.sagebionetworks.dashboard.dao.dynamo;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeDynamoDao;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

@Repository("bridgeDynamoDao")
public class BridgeDynamoDaoImpl implements BridgeDynamoDao {

    @Resource
    private DwConfig dwConfig;

    @Resource
    private AmazonDynamoDB dynamoClient;

    @Override
    public String getFullTableName(String dynamoTableName) {
        final String prefix = dwConfig.getBridgeStack() + "-" + dwConfig.getBridgeUser() + "-";
        return prefix + dynamoTableName;
    }

    @Override
    public boolean tableExists(String dynamoTableName) {
        try {
            dynamoClient.describeTable(getFullTableName(dynamoTableName));
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }
}
