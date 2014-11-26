package org.sagebionetworks.dashboard.dao.redshift;

import java.util.HashMap;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.RawAccessRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("rawAccessRecordDao")
public class RawAccessRecordDaoImpl implements RawAccessRecordDao{

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String COPY_HEAD = "COPY raw_access_record FROM '";
    private static final String COPY_CRED_U = "' CREDENTIALS 'aws_access_key_id=";
    private static final String COPY_CRED_P = ";aws_secret_access_key=";
    private static final String COPY_END = "' CSV GZIP FILLRECORD;";

    private static final String COUNT = "SELECT COUNT(*) FROM raw_access_record;";

    @Override
    public void copy(String filePath, String username, String password) {
        String query = COPY_HEAD + filePath + COPY_CRED_U + username + COPY_CRED_P + password + COPY_END;
        try {
            dwTemplate.update(query, new HashMap<String, Object>());
            logger.info("Finish adding " + filePath + " into raw_access_record table.");
        } catch (Throwable e) {
            logger.error("Failed to add " + filePath, e);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
        return dwTemplate.getJdbcOperations().queryForInt(COUNT);
    }

}
