package org.sagebionetworks.dashboard.dao.redshift;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

    @Test
    public void test() {
        final String table = getClass().getSimpleName();
        final String date = DateTime.now(DateTimeZone.UTC).toString("yyyyMMdd");
        final String fullTableName = table + "_" + date;
        final String createTableQuery = "CREATE TABLE " + fullTableName + ";";
        dwDao.createTable(createTableQuery);
        final String dropTableQuery = "DROP TABLE " + fullTableName + ";";
        dwDao.dropTable(dropTableQuery);
    }
}
