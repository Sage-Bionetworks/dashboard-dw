package org.sagebionetworks.dashboard.config;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandArgsPropertiesProviderTest {

    private String key;
    private String value;

    @Before
    public void before() {
        key = getClass().getName();
        value = key + "value";
        System.setProperty(key, value);
    }

    @After
    public void after() {
        System.clearProperty(key);
    }

    @Test
    public void test() {
        Properties props = new Properties();
        props.setProperty(key, "someOtherValue");
        PropertiesProvider propsProvider = new BasicPropertiesProvider(props);
        propsProvider = new CommandArgsPropertiesProvider(propsProvider);
        assertEquals(value, propsProvider.getProperties().getProperty(key));
    }
}
