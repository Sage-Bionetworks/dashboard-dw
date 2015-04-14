package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.model.AccessRecord;
import org.sagebionetworks.dashboard.model.SynapseRepoRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
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

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String NEXT_RECORDS = "SELECT * FROM access_record "
            + "WHERE sessionId NOT IN (SELECT sessionId FROM access_record_entity) LIMIT 1000;";

    private static final String COUNT = "SELECT COUNT(*) FROM access_record;";

    private static final String CREATE_TEMP = "CREATE TABLE access_record_temp "
            +"AS SELECT DISTINCT * FROM raw_access_record;";

    private static final String DROP_TABLE = "DROP TABLE access_record";

    private static final String RENAME = "ALTER TABLE access_record_temp "
            +"RENAME TO access_record;";

    @Override
    public List<AccessRecord> nextRecords() {
        return dwTemplate.query(NEXT_RECORDS, new HashMap<String, Object>(), new AccessRecordMapper());
    }

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
        return dwTemplate.getJdbcOperations().queryForInt(COUNT);
    }

    @Override
    public void createTemp() {
        dwTemplate.execute(CREATE_TEMP, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }});
        logger.info("access_record_temp is created.");
    }

    @Transactional
    @Override
    public void activateTemp() {
        dwTemplate.execute(DROP_TABLE, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }});
        dwTemplate.execute(RENAME, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }});
        logger.info("access_record is updated.");
    }

}
