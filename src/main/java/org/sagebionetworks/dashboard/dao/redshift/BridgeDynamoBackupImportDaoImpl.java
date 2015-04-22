package org.sagebionetworks.dashboard.dao.redshift;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.sagebionetworks.dashboard.dao.BridgeDynamoBackupImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amazonaws.util.IOUtils;

@Repository("bridgeDynamoBackupImportDao")
public class BridgeDynamoBackupImportDaoImpl implements BridgeDynamoBackupImportDao {

    private final Logger logger = LoggerFactory.getLogger(BridgeDynamoBackupImportDaoImpl.class);

    @Resource
    private DwDao dwDao;

    @Override
    public String createTable(final String tableName, final String dateSuffix) {
        final String path = "/spring/" + tableName + ".sql";
        try (final InputStream source = this.getClass().getResourceAsStream(path)) {
            if (source == null) {
                throw new RuntimeException("Cannot find file " + path);
            }
            final String query = IOUtils.toString(source);
            final String createTableQuery = query.replace("<dateSuffix>", dateSuffix);
            dwDao.createTable(createTableQuery);
            return getFullTableName(tableName, dateSuffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String dropTable(final String tableName, final String dateSuffix) {
        final String fullTableName = getFullTableName(tableName, dateSuffix);
        final String dropTableQuery = "DROP TABLE " + fullTableName;
        dwDao.dropTable(dropTableQuery);
        return fullTableName;
    }

    @Override
    public void copy(String s3Path, String dwTable) {
        // TODO Auto-generated method stub
    }
    
    private String getFullTableName(final String tableName, final String dateSuffix) {
        return tableName + "_" + dateSuffix;
    }
}
