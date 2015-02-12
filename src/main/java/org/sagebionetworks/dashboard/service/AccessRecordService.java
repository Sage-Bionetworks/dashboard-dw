package org.sagebionetworks.dashboard.service;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.AccessRecordEntityDao;
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("accessRecordService")
public class AccessRecordService {

    private final Logger logger = LoggerFactory.getLogger(AccessRecordService.class);

    @Resource
    private AccessRecordDao accessRecordDao;
    @Resource
    private AccessRecordEntityDao accessRecordEntityDao;
    @Resource
    private LogFileDao logFileDao;

    public void update() {
        accessRecordDao.createTemp();
        accessRecordDao.activateTemp();
    }
}
