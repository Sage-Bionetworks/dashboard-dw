package org.sagebionetworks.dashboard.service;

import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.parse.AccessRecord;
import org.sagebionetworks.dashboard.parse.EntityIdReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service("accessRecordService")
public class AccessRecordService {

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordService.class);

    @Resource
    private AccessRecordDao accessRecordDao;

    public void update() {
        // insert new raw records into access_record table
        accessRecordDao.insertNewRecords();
        // vacuum
        accessRecordDao.vacuum();

        try {
            // select 1000 null entityId records to update
            List<AccessRecord> records = accessRecordDao.nextRecords();
            // for each record, update the entityId field
            for (AccessRecord record : records) {
                String entityId = new EntityIdReader().read(record);
                if (entityId.startsWith("syn")) {
                    entityId = entityId.substring(3);
                }
                Long entity;
                try {
                    entity = Long.parseLong(entityId);
                } catch (NumberFormatException e) {
                    entity = (long) -1;
                }
                accessRecordDao.update(entity, record.getSessionId());
            }
        } catch (DataAccessException e) {
            logger.error("Failed to update", e);
        }
    }

}
