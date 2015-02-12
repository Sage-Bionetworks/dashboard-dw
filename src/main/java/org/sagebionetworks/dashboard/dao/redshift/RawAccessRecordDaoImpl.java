package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.RawAccessRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("rawAccessRecordDao")
public class RawAccessRecordDaoImpl implements RawAccessRecordDao{

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String COPY_HEAD = "COPY raw_access_record FROM '";
    private static final String COPY_CRED_U = "' CREDENTIALS 'aws_access_key_id=";
    private static final String COPY_CRED_P = ";aws_secret_access_key=";
    private static final String COPY_END = "' CSV GZIP FILLRECORD;";
    private static final String VACUUM ="VACUUM raw_access_record;";

    private static final String COUNT = "SELECT COUNT(*) FROM raw_access_record;";

    @Override
    public void copy(String filePath, String username, String password) {
        String query = COPY_HEAD + filePath + COPY_CRED_U + username + COPY_CRED_P + password + COPY_END;
        dwTemplate.update(query, new HashMap<String, Object>());
    }

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
        return dwTemplate.getJdbcOperations().queryForInt(COUNT);
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
        logger.info("raw_access_record table is vacuumed. ");
    }

}
