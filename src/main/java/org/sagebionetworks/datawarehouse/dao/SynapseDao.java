package org.sagebionetworks.datawarehouse.dao;

import java.util.List;

import org.sagebionetworks.datawarehouse.parse.CuPassingRecord;
import org.sagebionetworks.datawarehouse.parse.CuResponseRecord;

/**
 * Caches data retrieved directly from Synapse.
 */
public interface SynapseDao {

    /** Gets user display name. Can return null. */
    String getUserName(String userId);

    /** Gets entity name. Can return null. */
    String getEntityName(String entityId);

    /** Gets the ID of the benefactor. */
    String getBenefactor(String entityId);

    /** Gets the ID of the containing project. */
    String getProject(String entityId);

    /** Gets the PassingRecord of a user's Certified Quiz */
    CuPassingRecord getCuPassingRecord(String userId);

    /** Gets the list of responses that are submitted by a given user */
    List<CuResponseRecord> getResponses(String userId);

    /** Refresh the list of users. */
    void refreshUsers();
}
