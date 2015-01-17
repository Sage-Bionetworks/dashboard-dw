package org.sagebionetworks.dashboard.dao.redshift;

import java.util.Map;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordEntityDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class AccessRecordEntityDaoImpl implements AccessRecordEntityDao {
    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);
    private static final String INSERT_NEW_RECORDS = "INSERT INTO access_record_entity " +
            "(sessionId, entityId) VALUES (:sessionId, :entityId)";

    @Override
    public void insertNewRecords(Map<String,?>[] batchValues) {
        int[] results = dwTemplate.batchUpdate(INSERT_NEW_RECORDS, batchValues);
        logger.info(countNonZero(results) + "rows has been added to access_record_entity.");
    }

    private int countNonZero(int[] results) {
        int count = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] > 0) count++;
        }
        return count;
    }

}
