package org.sagebionetworks.dashboard.dao.redshift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@ContextConfiguration("classpath:/spring/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class DwDaoImplTest {

    @Resource
    private DwDao dwDao;

    @Before
    public void before() {
        List<String> tables = getTables();
        for (String table : tables) {
            dwDao.execute("DROP TABLE " + table + ";");
        }
    }

    @Test
    public void test() {
        List<String> tables = getTables();
        assertNotNull(tables);
        assertEquals(0, tables.size());
        final String fullTableName = getFullTableName();
        final String createTableQuery = "CREATE TABLE " + fullTableName + "(id char(36));";
        dwDao.execute(createTableQuery);
        tables = getTables();
        assertNotNull(tables);
        assertEquals(1, tables.size());
        assertEquals(fullTableName, tables.get(0));
        final String dropTableQuery = "DROP TABLE " + fullTableName + ";";
        dwDao.execute(dropTableQuery);
        tables = getTables();
        assertNotNull(tables);
        assertEquals(0, tables.size());
    }

    private String getTableName() {
        return getClass().getSimpleName().toLowerCase();
    }

    private String getFullTableName() {
        final String dateSuffix = DateTime.now(DateTimeZone.UTC).toString("yyyyMMdd");
        return getTableName() + "_" + dateSuffix;
    }

    private List<String> getTables() {
        return dwDao.getTables(getTableName());
    }
}
