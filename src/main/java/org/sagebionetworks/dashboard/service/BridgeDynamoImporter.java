package org.sagebionetworks.dashboard.service;

import static org.joda.time.DateTimeConstants.DAYS_PER_WEEK;
import static org.joda.time.DateTimeConstants.MONDAY;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.stereotype.Service;

import com.amazonaws.util.IOUtils;

@Service("bridgeDynamoImporter")
public final class BridgeDynamoImporter {

    private final static String DATE_SUFFIX_PLACEHOLDER = "<dateSuffix>";
    private static final int FIRST_DAY_OF_MONTH = 1;
    private static final int WEEKS_PER_MONTH = 4;

    @Resource
    private DwConfig dwConfig;

    @Resource
    private BridgeTableNameService bridgeTableNameService;

    @Resource
    private DwDao dwDao;

    @Resource
    private BridgeImportDao bridgeImportDao;

    public void run() {
        final List<String> dynamoTables = dwConfig.getBridgeDynamoTables();
        for (String dynamoTable : dynamoTables) {
            final DateTime now = DateTime.now(DateTimeZone.UTC);
            final String dateSuffix = bridgeTableNameService.getDateSuffix(now);
            final String dwTableName = bridgeTableNameService.getDwTableName(dynamoTable);
            final String createTableQuery = getCreateTableQuery(dwTableName, dateSuffix);
            bridgeImportDao.createTable(createTableQuery);
            final String fullDwTableName = findTable(dwTableName, dateSuffix);
            if (fullDwTableName == null) {
                throw new RuntimeException("Creating table " + dwTableName + " has failed.");
            }
            final String fullDynamoTableName = bridgeTableNameService.getFullDynamoTableName(dynamoTable);
            bridgeImportDao.copyFromDynamo(fullDynamoTableName, fullDwTableName);
            bridgeImportDao.updateView(dwTableName, fullDwTableName);
            cleanup(dwTableName, now);
        }
    }

    String getCreateTableQuery(final String dwTableName, final String dateSuffix) {
        final String path = "/sql/" + dwTableName + ".sql";
        try (final InputStream source = this.getClass().getResourceAsStream(path)) {
            if (source == null) {
                throw new RuntimeException("Cannot find file " + path + " for table " + dwTableName);
            }
            final String rawQuery = IOUtils.toString(source);
            if (!rawQuery.contains(DATE_SUFFIX_PLACEHOLDER)) {
                throw new RuntimeException(
                        "The create-table query has no date-suffix placeholder: " + rawQuery);
            }
            return rawQuery.replace(DATE_SUFFIX_PLACEHOLDER, dateSuffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the full name of the table that actually exists. Returns null
     * if the table does not exist.
     */
    String findTable(final String tableName, final String dateSuffix) {
        List<String> tables = dwDao.getTables(tableName);
        for (String table : tables) {
            if (table.endsWith(dateSuffix)) {
                return table;
            }
        }
        return null;
    }

    /**
     * Finds dates of snapshots to drop. We keep only the following daily snapshots:
     * 
     * 1) daily snapshots of the past 7 days
     * 2) weekly snapshots of the past month
     * 3) monthly snapshots
     *
     */
    List<DateTime> findDatesToDrop(final DateTime timestamp) {
        List<DateTime> dates = new ArrayList<>();
        final DateTime aWeekBefore = timestamp.minusDays(DAYS_PER_WEEK);
        if (aWeekBefore.getDayOfWeek() != MONDAY &&
                aWeekBefore.getDayOfMonth() != FIRST_DAY_OF_MONTH) {
            dates.add(aWeekBefore);
        }
        final DateTime aMonthBefore = timestamp.minusDays(DAYS_PER_WEEK * WEEKS_PER_MONTH);
        for (int i = 0; i < DAYS_PER_WEEK; i++) {
            final DateTime day = aMonthBefore.minusDays(i);
            if (day.getDayOfWeek() == MONDAY &&
                    day.getDayOfMonth() != FIRST_DAY_OF_MONTH) {
                dates.add(day);
            }
        }
        return dates;
    }

    private void cleanup(final String dwTableName, final DateTime timestamp) {
        final List<DateTime> datesToDrop = findDatesToDrop(timestamp);
        for (DateTime dateToDrop : datesToDrop) {
            final String dateSuffix = bridgeTableNameService.getDateSuffix(dateToDrop);
            final String fullDwTableName = findTable(dwTableName, dateSuffix);
            if (fullDwTableName != null) {
                bridgeImportDao.dropTable(fullDwTableName);
            }
        }
    }
}
