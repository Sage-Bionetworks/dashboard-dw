package org.sagebionetworks.dashboard.dao;

/**
 * Imports Bridge DynamoDB table backups to Redshift.
 *
 */
public interface BridgeDynamoBackupDao {

    void create(String tableName);

    void copy(String tableName);
}
