package org.sagebionetworks.dashboard.dao.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.FailedRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("failedAccessRecordDao")
public class FailedRecordDaoImpl implements FailedRecordDao {

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String INSERT_RECORD = "INSERT INTO failed_record " + 
            "(file_id, line_number, session_id) " + "VALUES (:file_id,:line_number,:session_id);";

    private static final String CLEAR_TABLE = "DELETE FROM failed_record;";

    private static final String COUNT = "SELECT COUNT(*) FROM failed_record;";

    @Override
    public void put(String fileId, int lineNumber, String sessionId) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("file_id", fileId);
        namedParameters.put("line_number", lineNumber);
        namedParameters.put("session_id", sessionId);

        try {
            dwTemplate.update(INSERT_RECORD, namedParameters);
        } catch (DataAccessException exception) {
            throw exception;
        }
    }

    @Override
    public void cleanup() {
        dwTemplate.execute(CLEAR_TABLE, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }});
        logger.info("log_file table is clear. ");
    }

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
            return dwTemplate.getJdbcOperations().queryForInt(COUNT);
    }
}
