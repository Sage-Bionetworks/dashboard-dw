package org.sagebionetworks.datawarehouse.dao;

/**
 * DAO that has local cache.
 */
public interface CachedDao {

    void clearCache();
}
