package org.sagebionetworks.dashboard.service;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.springframework.stereotype.Service;

@Service("bridgeTableNameService")
public class BridgeTableNameService {

    private final static String BRIDGE_TABLE_PREFIX = "bridge_";

    @Resource
    private DwConfig dwConfig;

    /**
     * Gets the date suffix for the data warehouse table name.
     */
    String getDateSuffix(final DateTime timestamp) {
        return timestamp.toString("yyyyMMdd");
    }

    /**
     * Gets the full Bridge DynamoDB table name in this format:
     * [stack]-[IAM user name]-[table name].
     */
    String getFullDynamoTableName(final String dynamoTableName) {
        return dwConfig.getBridgeStack() + "-" + dwConfig.getBridgeUser() + "-" + dynamoTableName;
    }

    /**
     * Gets the corresponding data warehouse table name given the DynamoDB table name.
     * Specifically the name is folded to lower cases, according to SQL standard,
     * and is prefixed by "bridge_".
     */
    String getDwTableName(final String dynamoTableName) {
        return BRIDGE_TABLE_PREFIX + dynamoTableName.toLowerCase();
    }
}
