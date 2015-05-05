package org.sagebionetworks.dashboard.dao.redshift;

import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("bridgeImportDao")
public class BridgeImportDaoImpl implements BridgeImportDao {

    final static Pattern CRED_PATTERN = Pattern.compile("\\s+CREDENTIALS\\s+\\S+\\s+",
            Pattern.CASE_INSENSITIVE);

    private final Logger logger = LoggerFactory.getLogger(BridgeImportDaoImpl.class);

    @Resource
    private DwConfig dwConfig;

    @Resource
    private DwDao dwDao;

    @Override
    public void createTable(final String createTableQuery) {
        dwDao.execute(createTableQuery);
    }

    @Override
    public void dropTable(final String fullDwTableName) {
        final String dropTableQuery = "DROP TABLE " + fullDwTableName + ";";
        dwDao.execute(dropTableQuery);
    }

    @Override
    public void copyFromDynamo(final String fullDynamoTableName, final String fullDwTableName) {
        final String copy = "COPY " + fullDwTableName +
                " FROM " + getDynamoUrl(fullDynamoTableName) +
                " CREDENTIALS " + getAwsCredentials() +
                " READRATIO 50;";
        try {
            dwDao.execute(copy);
        } catch (Throwable e) {
            // Potential leak of credentials in the error message; strip them off
            logger.error(CRED_PATTERN.matcher(e.getMessage()).replaceAll(" "));
        }
    }

    @Override
    public void updateView(final String viewName, final String fullDwTableName) {
        final String query = "CREATE OR REPLACE VIEW " + viewName +
                " AS SELECT * FROM " + fullDwTableName + ";";
        dwDao.execute(query);
    }

    String getAwsCredentials() {
        return  "'aws_access_key_id=" + dwConfig.getBridgeAwsAccessKey() +
                ";aws_secret_access_key=" + dwConfig.getBridgeAwsSecretKey() + "'";
    }

    String getDynamoUrl(final String fullDynamoTableName) {
        return "'dynamodb://" + fullDynamoTableName + "'";
    }
}
