package org.sagebionetworks.dashboard.service;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeDynamoBackupImportDao;
import org.sagebionetworks.dashboard.dao.BridgeDynamoDao;
import org.springframework.stereotype.Service;

@Service("bridgeDynamoBackupImporter")
public final class BridgeDynamoBackupImporter {

    @Resource
    private DwConfig dwConfig;

    @Resource
    private BridgeDynamoBackupImportDao bridgeDao;

    public void doWork() {
        final List<String> tables = dwConfig.getBridgeDynamoTables();
        for (String table : tables) {
            final DateTime now = DateTime.now(DateTimeZone.UTC);
            final String dateSuffix = now.toString("yyyyMMdd");
            bridgeDao.createTable(table, dateSuffix);
            bridgeDao.copy("", "");
            DateTime aWeekAgo = now.minusDays(7);
            if (aWeekAgo.getDayOfMonth() != 0 && aWeekAgo.getDayOfWeek() != 0) {
                bridgeDao.dropTable("", "");
            }
        }
    }
}
