package org.sagebionetworks.dashboard.dao.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:/META-INF/spring/test-postgres-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class LogFileDaoImplTest {

    private static final int TEST_SIZE = 100;

    @Resource
    private LogFileDao logFileDao;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(200);

    @Before
    public void before() throws Exception {
        assertNotNull(logFileDao);
        logFileDao.cleanup();
    }

    @After
    public void cleanup() {
        logFileDao.cleanup();
        threadPool.shutdown();
    }

    @Test
    public void test() {
        List<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < TEST_SIZE; i++) {
            final String id = UUID.randomUUID().toString();
            for (int j = 0; j < TEST_SIZE; j++) {
                tasks.add(new Runnable() {
    
                    @Override
                    public void run() {
                        logFileDao.put(id, id, 0);
                    }
                });
            }
        }
        assertEquals(TEST_SIZE*TEST_SIZE, tasks.size());

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

        assertEquals(TEST_SIZE, logFileDao.count());
    }

    private boolean isDone() {
        ThreadPoolExecutor pool = (ThreadPoolExecutor)threadPool;
        return pool.getActiveCount() == 0 && pool.getQueue().size() == 0;
    }

}
