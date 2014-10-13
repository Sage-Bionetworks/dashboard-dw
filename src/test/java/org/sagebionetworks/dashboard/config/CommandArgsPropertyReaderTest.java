package org.sagebionetworks.dashboard.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandArgsPropertyReaderTest {

    private String key;
    private String value;

    @Before
    public void before() {
        key = CommandArgsPropertyReaderTest.class.getName();
        value = key + "value";
        System.setProperty(key, value);
    }

    @After
    public void after() {
        System.clearProperty(key);
    }

    @Test
    public void test() {
        PropertyReader reader = new CommandArgsPropertyReader();
        assertEquals(value, reader.read(key));
        assertNull(reader.read("SomethingThatDoesNotExist"));
    }
}
