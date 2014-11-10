package org.sagebionetworks.dashboard.service;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.RawAccessRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("rawAccessRecordService")
public class RawAccessRecordService {

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordService.class);

    @Resource
    private RawAccessRecordDao rawAccessRecordDao;

    public void update(final String filePath, final String username, final String password) {
        try {
            rawAccessRecordDao.copy(filePath, username, password);
        } catch (Throwable exception) {
            logger.error("Failed to copy file " + filePath);
        }
    }
}
