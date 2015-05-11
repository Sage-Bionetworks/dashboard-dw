package org.sagebionetworks.dashboard.dao.redshift;

import java.util.Collections;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("accessRecordDao")
public class AccessRecordDaoImpl implements AccessRecordDao{

    private final Logger logger = LoggerFactory.getLogger(AccessRecordDaoImpl.class);

    @Resource
    private DwDao dwDao;

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    private static final String COUNT = "SELECT COUNT(*) FROM access_record;";

    private static final String CREATE_TEMP = "CREATE TABLE access_record_temp "
            + "(LIKE raw_access_record);";

    private static final String DEDUPE = "INSERT INTO access_record_temp "
            + "(SELECT DISTINCT * FROM raw_access_record);";

    private static final String DROP_TEMP = "DROP TABLE IF EXISTS access_record_temp;";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS access_record;";

    private static final String RENAME = "ALTER TABLE access_record_temp "
            + "RENAME TO access_record;";

    @SuppressWarnings("deprecation")
    @Override
    public long count() {
        return dwTemplate.queryForLong(COUNT, Collections.<String, Object> emptyMap());
    }

    @Override
    public void createTemp() {
        dwDao.execute(DROP_TEMP);
        dwDao.execute(CREATE_TEMP);
        dwDao.execute(DEDUPE);
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
