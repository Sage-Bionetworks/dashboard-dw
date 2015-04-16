package org.sagebionetworks.dashboard;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DwConfigTest {

    @Test
    public void test() {
        DwConfig config = new DwConfig();
        assertNotNull(config.getAccessRecordBucket());
        assertNotNull(config.getAwsAccessKey());
        assertNotNull(config.getAwsSecretKey());
        assertNotNull(config.getDwUrl());
        assertNotNull(config.getDwUsername());
        assertNotNull(config.getDwPassword());
        assertNotNull(config.getSynapseUser());
        assertNotNull(config.getSynapsePassword());
    }
}