package org.sagebionetworks.dashboard.dao.redshift;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.sagebionetworks.dashboard.config.DwConfig;
import org.sagebionetworks.dashboard.dao.BridgeImportDao;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.test.util.ReflectionTestUtils;

public class BridgeImportDaoImplTest {

    @Test
    public void testCreateTable() {

        DwDao mockDwDao = mock(DwDao.class);
        BridgeImportDao dao = new BridgeImportDaoImpl();
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);

        final String createTableQuery = "CREATE TABLE IF NOT EXISTS test_table;";
        dao.createTable(createTableQuery);
        verify(mockDwDao, times(1)).execute(createTableQuery);
    }

    @Test
    public void testDropTable() {

        DwDao mockDwDao = mock(DwDao.class);
        BridgeImportDao dao = new BridgeImportDaoImpl();
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);

        final String fullDwTableName = "bridge_test_table";
        dao.dropTable(fullDwTableName);
        verify(mockDwDao, times(1)).execute(eq("DROP TABLE " + fullDwTableName + ";"));
    }

    @Test
    public void testCopyFromDynamo() {

        BridgeImportDao dao = new BridgeImportDaoImpl();
        DwConfig mockConfig = mock(DwConfig.class);
        when(mockConfig.getBridgeAwsAccessKey()).thenReturn("access-key");
        when(mockConfig.getBridgeAwsSecretKey()).thenReturn("secret-key");
        ReflectionTestUtils.setField(dao, "dwConfig", mockConfig);
        DwDao mockDwDao = mock(DwDao.class);
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);

        dao.copyFromDynamo("dynamo-table", "redshift-table");
        verify(mockDwDao, times(1)).execute(
                startsWith("COPY redshift-table FROM 'dynamodb://dynamo-table' "));
        verify(mockDwDao, times(1)).execute(
                endsWith("READRATIO 50;"));
    }

    @Test
    public void testUpdateView() {

        BridgeImportDao dao = new BridgeImportDaoImpl();
        DwDao mockDwDao = mock(DwDao.class);
        ReflectionTestUtils.setField(dao, "dwDao", mockDwDao, DwDao.class);

        dao.updateView("view", "snapshot");
        verify(mockDwDao, times(1)).execute(startsWith("CREATE OR REPLACE VIEW "));
        verify(mockDwDao, times(1)).execute(endsWith(";"));
    }

    @Test
    public void testAwsCredentialsPattern() {
        String sql = "copy favoritemovies from 'dynamodb://my-favorite-movies-table'" +
                " credentials 'aws_access_key_id=<Your-Access-Key-ID>;aws_secret_access_key=<Your-Secret-Access-Key>'" + 
                " readratio 50;";
        assertTrue(BridgeImportDaoImpl.CRED_PATTERN.matcher(sql).find());
        sql =   "COPY favoritemovies FROM 'dynamodb://my-favorite-movies-table'\n" +
                "CREDENTIALS 'aws_access_key_id=<Your-Access-Key-ID>;aws_secret_access_key=<Your-Secret-Access-Key>'\n" + 
                "READRATIO 50;";
        assertTrue(BridgeImportDaoImpl.CRED_PATTERN.matcher(sql).find());
    }

    @Test
    public void testGetAwsCredentials() {

        BridgeImportDaoImpl dao = new BridgeImportDaoImpl();
        DwConfig mockConfig = mock(DwConfig.class);
        when(mockConfig.getBridgeAwsAccessKey()).thenReturn("access-key");
        when(mockConfig.getBridgeAwsSecretKey()).thenReturn("secret-key");
        ReflectionTestUtils.setField(dao, "dwConfig", mockConfig);

        String credentials = dao.getAwsCredentials();
        assertTrue(credentials.startsWith("'"));
        assertTrue(credentials.endsWith("'"));
        assertTrue(credentials.contains("aws_access_key_id="));
        assertTrue(credentials.contains("aws_secret_access_key="));
        assertTrue(credentials.contains(";"));
    }

    @Test
    public void testGetDynamoUrl() {

        BridgeImportDaoImpl dao = new BridgeImportDaoImpl();
        String credentials = dao.getDynamoUrl("dynamo-table");
        assertTrue(credentials.startsWith("'dynamodb://"));
        assertTrue(credentials.endsWith("'"));
    }
}
