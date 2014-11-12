package org.sagebionetworks.dashboard.dao.postgres;

import java.util.HashMap;
import java.util.Map;

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

    private static final String COPY_HEAD = "COPY raw_access_record FROM ':file_path'"
            + " CREDENTIALS 'aws_access_key_id=";
    private static final String COPY_CRED = ";aws_secret_access_key=";
    private static final String COPY_END = "' CSV GZIP;";

    @Override
    public void copy(String filePath, String username, String password) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("file_path", filePath);
        String query = COPY_HEAD + username + COPY_CRED + password + COPY_END;
        try {
            dwTemplate.update(query, namedParameters);
            logger.info("Finish adding " + filePath + " into raw_access_record table.");
        } catch (Throwable e) {
            logger.error("Failed to add " + filePath, e);
        }
    }

}
