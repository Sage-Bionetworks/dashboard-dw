package org.sagebionetworks.dashboard.service;

import static org.joda.time.DateTimeConstants.DAYS_PER_WEEK;
import static org.joda.time.DateTimeConstants.MONDAY;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeImportDao;
import org.springframework.stereotype.Service;

@Service("bridgeDynamoImporter")
public final class BridgeDynamoImporter {

    private static final int FIRST_DAY_OF_MONTH = 1;
    private static final int WEEKS_PER_MONTH = 4;

    @Resource
    private DwConfig dwConfig;

    @Resource
    private BridgeImportDao bridgeImportDao;

    public void doWork() {
        final List<String> tables = dwConfig.getBridgeDynamoTables();
        for (String table : tables) {
            final DateTime now = DateTime.now(DateTimeZone.UTC);
            final String dateSuffix = getDateSuffix(now);
            final String snapshotName = bridgeImportDao.createTable(table, dateSuffix);
            final String dynamoTable = dwConfig.getBridgeStack() + "-" +
                    dwConfig.getBridgeUser() + "-" + table;
            bridgeImportDao.copyFromDynamo(dynamoTable, snapshotName);
            updateView(table, snapshotName);
            cleanup(table, now);
        }
    }

    private void updateView(final String tableName, final String snapshotName) {
        bridgeImportDao.updateView(tableName, snapshotName);
    }

    private void cleanup(final String table, final DateTime timestamp) {
        // Keep only the following:
        // 1) daily snapshots of the past 7 days
        // 2) weekly snapshots of the past month
        // 3) monthly snapshots
        final DateTime aWeekBefore = timestamp.minusDays(DAYS_PER_WEEK);
        if (aWeekBefore.getDayOfWeek() != MONDAY &&
                aWeekBefore.getDayOfMonth() != FIRST_DAY_OF_MONTH) {
            final String dateSuffix = getDateSuffix(timestamp);
            bridgeImportDao.dropTable(table, dateSuffix);
        }
        final DateTime aMonthBefore = timestamp.minusDays(DAYS_PER_WEEK * WEEKS_PER_MONTH);
        for (int i = 0; i < DAYS_PER_WEEK; i++) {
            final DateTime day = aMonthBefore.minusDays(i);
            if (day.getDayOfMonth() != FIRST_DAY_OF_MONTH) {
                final String dateSuffix = getDateSuffix(day);
                bridgeImportDao.dropTable(table, dateSuffix);
            }
        }
    }

    private String getDateSuffix(final DateTime timestamp) {
        return timestamp.toString("yyyyMMdd");
    }
}
