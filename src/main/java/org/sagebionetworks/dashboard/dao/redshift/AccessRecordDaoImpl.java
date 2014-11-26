package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("accessRecordDao")
public class AccessRecordDaoImpl implements AccessRecordDao{

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String INSERT_RECORD = "INSERT INTO access_record "
            + "(SELECT DISTINCT * FROM raw_access_record WHERE sessionId NOT IN "
            + "(SELECT sessionId FROM access_record));";

    private static final String VACUUM = "VACUUM access_record;";

    private static final String NEXT_RECORDS = "SELECT * FROM access_record "
            + "WHERE entityId IS NULL LIMIT 1000;";

    private static final String UPDATE = "UPDATE access_record "
            + "SET entityId = :entityId WHERE sessionId = :sessionId;";

    private static final String CLEAR_TABLE = "DELETE FROM access_record;";

    private static final String COUNT = "SELECT COUNT(*) FROM access_record;";

    @Override
    public void insertNewRecords() {
        int rowAffected = dwTemplate.update(INSERT_RECORD, new HashMap<String, Object>());
        logger.info(rowAffected + " new records are inserted into access_record.");
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
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