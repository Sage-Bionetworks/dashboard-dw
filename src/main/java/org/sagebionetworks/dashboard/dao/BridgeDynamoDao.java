package org.sagebionetworks.dashboard.dao;

/**
 * Interface directly over Bridge DynamoDB tables.
 *
 */
public interface BridgeDynamoDao {

    /**
     * @param dynamoTableName DynamoDb table name without the stack-user prefix
     * @return Full DynamoDb table name with the stack-user prefix
     */
    String getFullTableName(String dynamoTableName);

    /**
     * @param dynamoTableName DynamoDb table name without the stack-user prefix
     * @return whether the table exists
     */
    boolean tableExists(String dynamoTableName);
}
