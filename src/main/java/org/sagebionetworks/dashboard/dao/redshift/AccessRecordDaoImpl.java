package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.sagebionetworks.dashboard.model.AccessRecord;
import org.sagebionetworks.dashboard.model.SynapseRepoRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("accessRecordDao")
public class AccessRecordDaoImpl implements AccessRecordDao{

    private static class AccessRecordMapper implements RowMapper<AccessRecord> {
        @Override
        public AccessRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            SynapseRepoRecord record = new SynapseRepoRecord();
            record.setSessionId(rs.getString("sessionId"));
            record.setObjectId(rs.getString("returnObjectId"));
            record.setUri(rs.getString("requestURL"));
            record.setQueryString(rs.getString("queryString"));
            return record;
        }
    }

    private static final RowMapper<AccessRecord> ROW_MAPPER = new AccessRecordMapper();

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);

    @Resource
    private DwDao dwDao;

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String NEXT_RECORDS = "SELECT * FROM access_record "
            + "WHERE sessionId NOT IN (SELECT sessionId FROM access_record_entity) LIMIT 1000;";

    private static final String COUNT = "SELECT COUNT(*) FROM access_record;";

    private static final String CREATE_TEMP = "CREATE TABLE access_record_temp "
            +"AS SELECT DISTINCT * FROM raw_access_record;";

    private static final String DROP_TEMP = "DROP TABLE IF EXISTS access_record_temp;";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS access_record;";

    private static final String RENAME = "ALTER TABLE access_record_temp "
            +"RENAME TO access_record;";

    @Override
    public List<AccessRecord> nextRecords() {
        return dwTemplate.query(NEXT_RECORDS, Collections.<String, Object> emptyMap(), ROW_MAPPER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
        return dwTemplate.queryForLong(COUNT, Collections.<String, Object> emptyMap());
    }

    @Override
    public void createTemp() {
        dwDao.execute(DROP_TEMP);
        dwDao.execute(CREATE_TEMP);
        logger.info("access_record_temp is created.");
    }

    @Transactional
    @Override
    public void activateTemp() {
        dwDao.execute(DROP_TABLE);
        dwDao.execute(RENAME);
        logger.info("access_record is updated.");
    }
}
