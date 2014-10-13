package org.sagebionetworks.dashboard.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultConfigTest {

    @Before
    public void before() {
        System.setProperty("key.a", "developValueA");
    }

    @After
    public void after() {
        System.clearProperty("key.a");
    }

    @Test
    public void test() throws Exception {
        String configFile = getClass().getResource("/test.config").getFile(); 
        Config config = new DefaultConfig(Stack.DEVELOP, configFile);
        assertEquals(Stack.DEVELOP, config.getStack());
        assertEquals("developValueA", config.get("key.a"));
        assertEquals("developValueB", config.get("key.b"));
        assertEquals("developValueC", config.get("key.c"));
        assertNull(config.get("key.d"));
        assertEquals(52, config.getInt("key.e"));
        assertEquals(52L, config.getLong("key.e"));
        assertTrue(config.getBoolean("key.f"));
    }
}
