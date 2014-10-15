package org.sagebionetworks.datawarehouse.parse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClientFilterTest {
    @Test
    public void test() {
        RepoRecord record = new RepoRecord();
        // Real data of October, 2013
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.1 linux/2.6.18-194.17.4.el5");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.1 linux/2.6.18-194.8.1.el5");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.2 darwin/11.4.2");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.2 darwin/12.4.0");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.3 linux/2.6.18-194.3.1.el5");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.3 linux/3.2.0-36-virtual");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.3 linux/3.2.0-44-generic");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.3 linux/3.2.0-52-generic");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.4 linux/3.8.0-26-generic");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.4 linux/3.8.0-30-generic");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.5 darwin/12.4.0");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.5 linux/2.6.31.6");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.5 linux/3.10.10-200.fc19.x86_64");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.5 linux/3.10.11-200.fc19.x86_64");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.5 linux/3.10.9-200.fc19.x86_64");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/1.2.3 cpython/2.7.5 windows/7");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("python-requests/2.0.0 cpython/2.7.1 darwin/11.4.2");
        assertTrue(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("synapserclient/0.28");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertTrue(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("synapserclient/0.29-1");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertTrue(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("synapserclient/0.30-1");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertTrue(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("synapserclient/0.31-3");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertTrue(ClientFilter.R.matches(record));
        assertFalse(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/11.0-5-gb11fbfa  synapse-web-client/12.0");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/12.0-2-ge17e722  synapse-web-client/12.0-4-gafe76ad");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/2013-08-15-7ba0d44-611  synapse-web-client/10.0-6-gdd75c3d");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/2013-09-13-e70558e-662  synapse-web-client/13.0");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/2013-09-13-e70558e-662  synapse-web-client/13.0-11-g9d93b9b");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/2013-09-13-e70558e-662  synapse-web-client/13.0-9-g9bbedd9");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/2013-09-13-e70558e-662  synapse-web-client/13.1-3-g5abfc97");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
        record.setUserAgent("synpase-java-client/2013-09-13-e70558e-662  synapse-web-client/14.0-8-gd55d559");
        assertFalse(ClientFilter.PYTHON.matches(record));
        assertFalse(ClientFilter.R.matches(record));
        assertTrue(ClientFilter.WEB.matches(record));
    }
}
