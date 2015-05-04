package org.sagebionetworks.dashboard.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.test.util.ReflectionTestUtils;

public class BridgeDynamoImporterTest {

    @Test
    public void testGetCreateTableQuery() {
        BridgeDynamoImporter importer = new BridgeDynamoImporter();
        String createTable = importer.getCreateTableQuery("test_table", "date_suffix");
        assertTrue(createTable.startsWith("CREATE TABLE test_table_date_suffix ("));
        assertTrue(createTable.endsWith(");"));
    }

    @Test
    public void testFindTable() {
        DwDao dwDao = mock(DwDao.class);
        List<String> tableList = new ArrayList<>();
        tableList.add("fake_table");
        when(dwDao.getTables("test_table")).thenReturn(tableList);
        BridgeDynamoImporter importer = new BridgeDynamoImporter();
        ReflectionTestUtils.setField(importer, "dwDao", dwDao, DwDao.class);
        String table = importer.findTable("test_table", "date_suffix");
        assertNull(table);
        tableList.add("test_table");
        table = importer.findTable("test_table", "date_suffix");
        assertNull(table);
        tableList.add("test_table_date_suffix");
        table = importer.findTable("test_table", "date_suffix");
        assertNotNull(table);
        assertEquals("test_table_date_suffix", table);
    }

    @Test
    public void testFindDatesToDrop() {

        BridgeDynamoImporter importer = new BridgeDynamoImporter();
        DateTime timestamp = new DateTime(2015, 5, 3, 11, 30);
        List<DateTime> datesToDrop = importer.findDatesToDrop(timestamp);
        assertNotNull(datesToDrop);
        assertEquals(2, datesToDrop.size());
        // April 26, 2015 is 7 days ago and is Sunday (not Monday)
        assertEquals(4, datesToDrop.get(0).getMonthOfYear());
        assertEquals(26, datesToDrop.get(0).getDayOfMonth());
        assertEquals(2015, datesToDrop.get(0).getYear());
        // March 30, 2015 is a month ago and is Monday
        assertEquals(3, datesToDrop.get(1).getMonthOfYear());
        assertEquals(30, datesToDrop.get(1).getDayOfMonth());
        assertEquals(2015, datesToDrop.get(1).getYear());

        timestamp = new DateTime(2015, 5, 4, 11, 30);
        datesToDrop = importer.findDatesToDrop(timestamp);
        assertNotNull(datesToDrop);
        // April 27, 2015 is 7 days ago and is Monday
        // It should be in the list
        assertEquals(1, datesToDrop.size());
        // April 6, 2015 is a month ago and is Monday
        assertEquals(4, datesToDrop.get(0).getMonthOfYear());
        assertEquals(6, datesToDrop.get(0).getDayOfMonth());
        assertEquals(2015, datesToDrop.get(0).getYear());

        timestamp = new DateTime(2015, 1, 3, 11, 30);
        datesToDrop = importer.findDatesToDrop(timestamp);
        assertNotNull(datesToDrop);
        // December 1, 2014 is a month ago and is Monday
        // It should not be in the list
        assertEquals(1, datesToDrop.size());
        // December 27, 2014 is a week ago and is not Monday
        assertEquals(12, datesToDrop.get(0).getMonthOfYear());
        assertEquals(27, datesToDrop.get(0).getDayOfMonth());
        assertEquals(2014, datesToDrop.get(0).getYear());
    }
}
