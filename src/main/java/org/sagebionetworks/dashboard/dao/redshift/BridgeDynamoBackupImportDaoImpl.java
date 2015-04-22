package org.sagebionetworks.dashboard.dao.redshift;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.BridgeDynamoBackupImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amazonaws.util.IOUtils;

@Repository("bridgeDynamoBackupImportDao")
public class BridgeDynamoBackupImportDaoImpl implements BridgeDynamoBackupImportDao {

    private final static String DATE_SUFFIX_PLACEHOLDER = "<dateSuffix>";

    private final Logger logger = LoggerFactory.getLogger(BridgeDynamoBackupImportDaoImpl.class);

    @Resource
    private DwDao dwDao;

    @Override
    public String createTable(final String tableName, final String dateSuffix) {
        final String path = "/sql/" + tableName + ".sql";
        try (final InputStream source = this.getClass().getResourceAsStream(path)) {
            if (source == null) {
                throw new RuntimeException("Cannot find file " + path + " for table " + tableName);
            }
            final String rawQuery = IOUtils.toString(source);
            if (!rawQuery.contains(DATE_SUFFIX_PLACEHOLDER)) {
                throw new RuntimeException(
                        "The create-table query has no date-suffix placeholder: " + rawQuery);
            }
            final String createTableQuery = rawQuery.replace(DATE_SUFFIX_PLACEHOLDER, dateSuffix);
            dwDao.createTable(createTableQuery);
            final String fullTableName = getFullTableName(tableName, dateSuffix);
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
        dwDao.dropTable(dropTableQuery);
        logger.info("Table " + fullTableName + " has been dropped.");
        return fullTableName;
    }

    @Override
    public void copy(String s3Path, String dwTable) {
        // TODO Auto-generated method stub
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
