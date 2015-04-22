package org.sagebionetworks.dashboard.dao.redshift;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.sagebionetworks.dashboard.dao.BridgeDynamoBackupImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.test.util.ReflectionTestUtils;

public class BridgeDynamoBackupImportDaoImplTest {

    @Test
    public void test() {

        final String tableName = "test_table";
        final String dateSuffix = "date_suffix";
        final String fullTableName = tableName + "_" + dateSuffix;
        final List<String> tableList = Arrays.asList(fullTableName);
        DwDao mockDwDao = mock(DwDao.class);
        when(mockDwDao.getTables(tableName)).thenReturn(tableList);
        BridgeDynamoBackupImportDao dao = new BridgeDynamoBackupImportDaoImpl();
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);

        final String tableCreated = dao.createTable(tableName, dateSuffix);
        assertEquals(fullTableName, tableCreated);
        verify(mockDwDao, times(1)).createTable(any(String.class));

        final String tableDropped = dao.dropTable(tableName, dateSuffix);
        assertEquals(fullTableName, tableDropped);
        verify(mockDwDao, times(1)).dropTable(any(String.class));
    }
}
