package org.sagebionetworks.dashboard.dao;

public interface BridgeDynamoDao {

    /**
     * @param dynamoTableName DynamoDb table name without the stack-user prefix
     * @return The SQL to create the data warehouse table
     */
    String getCreateTableSql(String dynamoTableName);
}
