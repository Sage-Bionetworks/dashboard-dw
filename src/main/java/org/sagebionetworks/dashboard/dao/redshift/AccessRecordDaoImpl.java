package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.parse.AccessRecord;
import org.sagebionetworks.dashboard.parse.RepoRecord;
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
            RepoRecord record = new RepoRecord();
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

    private static final String INSERT_RECORD = "INSERT INTO access_record "
            + "(SELECT DISTINCT * FROM raw_access_record WHERE sessionId NOT IN "
            + "(SELECT sessionId FROM access_record));";

    private static final String VACUUM = "VACUUM access_record;";

    private static final String NEXT_RECORDS = "SELECT * FROM access_record "
            + "WHERE entityId IS NULL LIMIT 1000;";

    private static final String CLEAR_TABLE = "DELETE FROM access_record;";

    private static final String COUNT = "SELECT COUNT(*) FROM access_record;";

    @Override
    public void insertNewRecords() {
        int rowAffected = dwTemplate.update(INSERT_RECORD, new HashMap<String, Object>());
        logger.info(rowAffected + " new records are inserted into access_record.");
    }

    @Override
    public List<AccessRecord> nextRecords() {
        return dwTemplate.query(NEXT_RECORDS, new HashMap<String, Object>(), new AccessRecordMapper());
    }

    @Override
    @Transactional
    public void vacuum() {
        dwTemplate.execute(VACUUM, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }});
        logger.info("access_record table is vacuumed. ");
    }

    @Override
    public void cleanup() {
        dwTemplate.execute(CLEAR_TABLE, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }});
        logger.info("access_record table is clear. ");
    }

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
        return dwTemplate.getJdbcOperations().queryForInt(COUNT);
    }

}
