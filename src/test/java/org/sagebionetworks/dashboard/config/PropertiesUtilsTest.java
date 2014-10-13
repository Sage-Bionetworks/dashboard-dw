package org.sagebionetworks.dashboard.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertiesUtilsTest {

    private String key1;
    private String value1;
    private String key2;
    private String value2;

    @Before
    public void before() {
        key1 = getClass().getName() + "key1";
        value1 = key1 + "value";
        System.setProperty(key1, value1);
        key2 = getClass().getName() + "key2";
        value2 = key2 + "value";
        System.setProperty(key2, value2);
    }

    @After
    public void after() {
        System.clearProperty(key1);
        System.clearProperty(key2);
    }

    @Test
    public void testMergeWithCmdArgs() {
        Properties original = new Properties();
        original.setProperty(key1, "someValueOf1");
        Properties merged = PropertiesUtils.mergeProperties(
                new CommandArgsPropertyReader(), original);
        assertEquals(value1, merged.getProperty(key1));
        assertNull(merged.getProperty(key2)); // Must be driven by the original
    }

    @Test
    public void testMergeProperties() {
        Properties original = new Properties();
        original.setProperty(key1, value1);
        original.setProperty(key2, value2);
        Properties additional = new Properties();
        final String newValue2 = "newValue2";
        final String key3 = "key3";
        final String value3 = "value3";
        additional.setProperty(key2, newValue2);
        additional.setProperty(key3, value3);
        Properties merged = PropertiesUtils.mergeProperties(additional, original);
        assertEquals(3, merged.size());
        assertEquals(value1, merged.getProperty(key1));
        assertEquals(newValue2, merged.getProperty(key2));
        assertEquals(value3, merged.getProperty(key3));
    }
}
