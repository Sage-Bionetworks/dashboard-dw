package org.sagebionetworks.dashboard.dao.redshift;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.test.util.ReflectionTestUtils;

public class BridgeImportDaoImplTest {

    @Test
    public void testCreateDropTable() {

        final String tableName = "test_table";
        final String dateSuffix = "date_suffix";
        final String fullTableName = tableName + "_" + dateSuffix;
        final List<String> tableList = Arrays.asList(fullTableName);
        DwDao mockDwDao = mock(DwDao.class);
        when(mockDwDao.getTables(tableName)).thenReturn(tableList);
        BridgeImportDao dao = new BridgeImportDaoImpl();
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);

        final String tableCreated = dao.createTable(tableName, dateSuffix);
        assertEquals(fullTableName, tableCreated);
        verify(mockDwDao, times(1)).execute(startsWith("CREATE TABLE "));

        final String tableDropped = dao.dropTable(tableName, dateSuffix);
        assertEquals(fullTableName, tableDropped);
        verify(mockDwDao, times(1)).execute(startsWith("DROP TABLE "));
    }

    @Test
    public void testCopyFromDynamo() {
        BridgeImportDao dao = new BridgeImportDaoImpl();
        DwConfig mockConfig = mock(DwConfig.class);
        when(mockConfig.getBridgeAwsAccessKey()).thenReturn("accessKey");
        when(mockConfig.getBridgeAwsSecretKey()).thenReturn("secretKey");
        ReflectionTestUtils.setField(dao, "dwConfig", mockConfig);
        DwDao mockDwDao = mock(DwDao.class);
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);
        dao.copyFromDynamo("dynamo", "redshift");
        verify(mockDwDao, times(1)).execute(startsWith("COPY redshift FROM 'dynamodb://'"));
    }

    @Test
    public void testUpdateView() {
        BridgeImportDao dao = new BridgeImportDaoImpl();
        DwDao mockDwDao = mock(DwDao.class);
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);
        dao.updateView("table", "snapshot");
        verify(mockDwDao, times(1)).execute(startsWith("CREATE OR REPLACE VIEW "));
    }
}
