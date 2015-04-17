package org.sagebionetworks.dashboard.dao.dynamo;

import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sagebionetworks.dashboard.dao.BridgeDynamoDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:/spring/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BridgeDynamoDaoImplTest {

    @Resource
    private BridgeDynamoDao bridgeDynamoDao;

    @Test
    public void test() {
        assertNull(bridgeDynamoDao.getCreateTableSql("ParticipantOptions"));
    }
}
