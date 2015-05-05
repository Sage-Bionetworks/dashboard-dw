package org.sagebionetworks.dashboard.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Test;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.springframework.test.util.ReflectionTestUtils;

public class BridgeTableNameServiceTest {

    @Test
    public void testGetDateSuffix() {
        BridgeTableNameService tableNameService = new BridgeTableNameService();
        DateTime timestamp = new DateTime(2015, 5, 3, 10, 21);
        String dateSuffix = tableNameService.getDateSuffix(timestamp);
        assertEquals("20150503", dateSuffix);
    }

    @Test
    public void testGetDwTableName() {
        BridgeTableNameService tableNameService = new BridgeTableNameService();
        String dwTableName = tableNameService.getDwTableName("UserConsent2");
        assertEquals("bridge_userconsent2", dwTableName);
    }

    @Test
    public void testGetFullDynamoTableName() {
        DwConfig dwConfig = mock(DwConfig.class);
        when(dwConfig.getBridgeStack()).thenReturn("local");
        when(dwConfig.getBridgeUser()).thenReturn("bridge");
        BridgeTableNameService tableNameService = new BridgeTableNameService();
        ReflectionTestUtils.setField(tableNameService, "dwConfig", dwConfig, DwConfig.class);
        String fullDynamoTableName = tableNameService.getFullDynamoTableName("UserConsent2");
        assertEquals("local-bridge-UserConsent2", fullDynamoTableName);
    }
}
