package org.sagebionetworks.dashboard.dao.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("logFileDao")
public class LogFileDaoImpl implements LogFileDao {

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String INSERT_FILE = "INSERT INTO log_file " + 
            "(id, file_path, log_type, status) " + "VALUES (:id,:file_path,:log_type,:status);";

    private static final String CLEAR_TABLE = "DELETE FROM log_file;";

    private static final String COUNT = "SELECT COUNT(*) FROM log_file;";

    private static final String UPDATE = "UPDATE log_file SET status = 'DONE' WHERE id = :id;";

    private static final String ISCOMPLETED = "SELECT COUNT(*) FROM log_file WHERE status = 'DONE' AND file_path = :file_path;";

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

    @Override
    public void put(String filePath, String id, int logType) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("id", id);
        namedParameters.put("file_path", filePath);
        namedParameters.put("log_type", logType);
        namedParameters.put("status", "processing");

        try {
            dwTemplate.update(INSERT_FILE, namedParameters);
        } catch (DataAccessException exception) {
            throw exception;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isCompleted(String filePath) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("file_path", filePath);
        if (dwTemplate.queryForInt(ISCOMPLETED, namedParameters) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void update(String id) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("id", id);
        if (dwTemplate.update(UPDATE, namedParameters) != 1) {
            throw new RuntimeException("Failed to update file id " + id);
        }
    }

}
