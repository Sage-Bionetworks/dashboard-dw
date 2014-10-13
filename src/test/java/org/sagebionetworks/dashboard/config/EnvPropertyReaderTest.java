package org.sagebionetworks.dashboard.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EnvPropertyReaderTest {

    @Test
    public void test() {
        PropertyReader reader = new EnvPropertyReader();
        String lowerHome = reader.read("home");
        String upperHome = reader.read("HOME");
        assertNotNull(lowerHome);
        assertNotNull(upperHome);
        assertEquals(lowerHome, upperHome);
        assertNull(reader.read("SomethingThatDoesNotExist"));
    }
}
