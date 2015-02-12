package org.sagebionetworks.dashboard.dao.redshift;

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
import org.springframework.transaction.annotation.Transactional;

@Repository("logFileDao")
public class LogFileDaoImpl implements LogFileDao {

    private final Logger logger = LoggerFactory.getLogger(LogFileDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String INSERT_FILE = "INSERT INTO log_file " + 
            "(id, file_path, log_type, status) " + "VALUES (:id,:file_path,:log_type,:status);";

    private static final String CLEAR_TABLE = "DELETE FROM log_file;";

    private static final String COUNT = "SELECT COUNT(*) FROM log_file;";

    private static final String UPDATE = "UPDATE log_file SET status = 'DONE' WHERE id = :id;";

    private static final String EXIST = "SELECT COUNT(*) FROM log_file WHERE file_path = :file_path;";

    private static final String UPDATE_FAILED = "UPDATE log_file SET status = 'FAILED' WHERE id = :id;";

    private static final String VACUUM ="VACUUM log_file;";

    private static final String CLEAN_UP = "DELETE FROM log_file WHERE status = 'Processing';";

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
    @Transactional
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
    @Transactional
    public boolean exist(String filePath) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("file_path", filePath);
        if (dwTemplate.queryForInt(EXIST, namedParameters) == 1) {
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

    @Override
    public void updateFailed(String id) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("id", id);
        if (dwTemplate.update(UPDATE_FAILED, namedParameters) != 1) {
            throw new RuntimeException("Failed to update file id " + id);
        }
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
        logger.info("log_file table is vacuumed. ");
    }

    @Override
    public void cleanupProcessingFile() {
        int effectedRows = dwTemplate.update(CLEAN_UP, new HashMap<String, Object>());
        logger.info(effectedRows + " rows have been cleanned up.");
    }
}
