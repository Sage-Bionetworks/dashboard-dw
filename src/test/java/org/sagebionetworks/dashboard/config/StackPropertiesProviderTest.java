package org.sagebionetworks.dashboard.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.sagebionetworks.dashboard.config.PropertyReader.SEPARATOR;
import static org.sagebionetworks.dashboard.config.Stack.DEVELOP;
import static org.sagebionetworks.dashboard.config.Stack.PROD;

import java.util.Properties;

import org.junit.Test;

public class StackPropertiesProviderTest {
    @Test
    public void test() {
        Properties properties = new Properties();
        final String key1 = "key1";
        final String value1 = "value1";
        final String developKey1 = DEVELOP + SEPARATOR + key1;
        final String developValue1 = DEVELOP + value1;
        final String prodKey1 = PROD + SEPARATOR + key1;
        final String prodValue1 = PROD + value1;
        final String key2 = "key2";
        final String value2 = "value2";
        final String prodKey2 = PROD + SEPARATOR + key2;
        final String prodValue2 = PROD + value2;
        final String prodKey3 = PROD + SEPARATOR + "key3";
        final String prodValue3 = PROD + "value3";
        final String key4 = "key4";
        final String developKey4 = DEVELOP + SEPARATOR + key4;
        final String developValue4 = DEVELOP + "value4";
        properties.setProperty(key1, value1);
        properties.setProperty(developKey1, developValue1);
        properties.setProperty(prodKey1, prodValue1);
        properties.setProperty(key2, value2);
        properties.setProperty(prodKey2, prodValue2);
        properties.setProperty(prodKey3, prodValue3);
        properties.setProperty(developKey4, developValue4);
        PropertiesProvider propsProvider = new BasicPropertiesProvider(properties);
        assertEquals(value1, propsProvider.getProperties().getProperty(key1));
        assertEquals(value2, propsProvider.getProperties().getProperty(key2));
        propsProvider = new StackPropertiesProvider(Stack.DEVELOP, propsProvider);
        assertEquals(developValue1, propsProvider.getProperties().getProperty(key1));
        assertEquals(value2, propsProvider.getProperties().getProperty(key2));
        assertNull(propsProvider.getProperties().getProperty(prodKey3));
        assertEquals(developValue4, propsProvider.getProperties().getProperty(key4));
    }
}
