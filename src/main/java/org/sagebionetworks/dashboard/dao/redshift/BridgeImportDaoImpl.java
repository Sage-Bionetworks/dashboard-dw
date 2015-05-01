package org.sagebionetworks.dashboard.dao.redshift;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amazonaws.util.IOUtils;

@Repository("bridgeImportDao")
public class BridgeImportDaoImpl implements BridgeImportDao {

    private final static String BRIDGE_TABLE_PREFIX = "bridge_";
    private final static String DATE_SUFFIX_PLACEHOLDER = "<dateSuffix>";
    private final static Pattern CRED_PATTERN = Pattern.compile("\\s+CREDENTIALS\\s+\\S+\\s+",
            Pattern.CASE_INSENSITIVE);

    private final Logger logger = LoggerFactory.getLogger(BridgeImportDaoImpl.class);

    @Resource
    private DwConfig dwConfig;

    @Resource
    private DwDao dwDao;

    @Override
    public String createTable(final String tableName, final String dateSuffix) {
        final String bridgeTableName = BRIDGE_TABLE_PREFIX + tableName.toLowerCase();
        final String path = "/sql/" + bridgeTableName + ".sql";
        try (final InputStream source = this.getClass().getResourceAsStream(path)) {
            if (source == null) {
                throw new RuntimeException("Cannot find file " + path + " for table " + bridgeTableName);
            }
            final String rawQuery = IOUtils.toString(source);
            if (!rawQuery.contains(DATE_SUFFIX_PLACEHOLDER)) {
                throw new RuntimeException(
                        "The create-table query has no date-suffix placeholder: " + rawQuery);
            }
            final String createTableQuery = rawQuery.replace(DATE_SUFFIX_PLACEHOLDER, dateSuffix);
            dwDao.execute(createTableQuery);
            final String fullTableName = getFullTableName(bridgeTableName, dateSuffix);
            logger.info("Table " + fullTableName + " has been created.");
            return fullTableName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String dropTable(final String tableName, final String dateSuffix) {
        final String fullTableName = getFullTableName(tableName, dateSuffix);
        final String dropTableQuery = "DROP TABLE " + fullTableName;
        dwDao.execute(dropTableQuery);
        logger.info("Table " + fullTableName + " has been dropped.");
        return fullTableName;
    }

    @Override
    public void copyFromDynamo(String dynamoTable, String dwFullTableName) {
        final String credentials = "aws_access_key_id=" + dwConfig.getBridgeAwsAccessKey() +
                ";aws_secret_access_key=" + dwConfig.getBridgeAwsSecretKey();
        final String copy = "COPY " + dwFullTableName +
                " FROM ''dynamodb://" + dynamoTable + "'" +
                " CREDENTIALS " + credentials +
                " READRATIO 50;";
        try {
            dwDao.execute(copy); // Is this a blocking call?
        } catch (Throwable e) {
            // Strip off potential leak of credentials
            logger.error(CRED_PATTERN.matcher(e.getMessage()).replaceAll(" "));
        }
    }

    @Override
    public void updateView(String tableName, String snapshotName) {
        final String query = "CREATE OR REPLACE VIEW " + tableName +
                " AS SELECT * FROM " + snapshotName + ";";
        dwDao.execute(query);
    }

    private String getFullTableName(final String tableName, final String dateSuffix) {
        List<String>  tables = dwDao.getTables(tableName);
        for (String table : tables) {
            if (table.endsWith(dateSuffix)) {
                return table;
            }
        }
        throw new RuntimeException(
                "Cannot find table " + tableName + " with suffix " + dateSuffix);
    }
}
