package org.sagebionetworks.dashboard.dao.postgres;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sagebionetworks.dashboard.dao.FailedRecordDao;
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:/META-INF/spring/test-postgres-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FailedRecordDaoImplTest {

    private static final int TEST_SIZE = 100;

    @Resource
    private LogFileDao logFileDao;

    @Resource
    private FailedRecordDao failedRecordDao;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(200);

    @Before
    public void before() throws Exception {
        assertNotNull(failedRecordDao);
        assertNotNull(logFileDao);
        failedRecordDao.cleanup();
        logFileDao.cleanup();
    }

    @After
    public void cleanup() {
        failedRecordDao.cleanup();
        logFileDao.cleanup();
        threadPool.shutdown();
    }
    @Test
    public void test() {
        List<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < TEST_SIZE; i++) {
            tasks.add(new Runnable() {
                @Override
                public void run() {
                    String file_id = UUID.randomUUID().toString();
                    logFileDao.put(file_id, file_id, 0);
                    failedRecordDao.put(file_id, 0, file_id);
                }
            });
        }
        assertEquals(TEST_SIZE, tasks.size());

        for (Runnable task : tasks) {
            threadPool.submit(task);
        }

        try {
            Thread.sleep(TEST_SIZE * 5L);
            while (!isDone()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TEST_SIZE, failedRecordDao.count());
    }

    private boolean isDone() {
        ThreadPoolExecutor pool = (ThreadPoolExecutor)threadPool;
        return pool.getActiveCount() == 0 && pool.getQueue().size() == 0;
    }
}
