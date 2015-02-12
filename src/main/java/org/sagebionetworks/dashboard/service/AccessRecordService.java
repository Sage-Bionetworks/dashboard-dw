package org.sagebionetworks.dashboard.service;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.springframework.stereotype.Service;

@Service("accessRecordService")
public class AccessRecordService {

    @Resource
    private AccessRecordDao accessRecordDao;
    @Resource
    private LogFileDao logFileDao;

    public void update() {
        accessRecordDao.createTemp();
        accessRecordDao.activateTemp();
    }
}
