package org.sagebionetworks.dashboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.AccessRecordEntityDao;
import org.sagebionetworks.dashboard.parse.AccessRecord;
import org.sagebionetworks.dashboard.parse.EntityIdReader;
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

    public void update() {
        accessRecordDao.insertNewRecords();
        accessRecordDao.vacuum();
    }

    @SuppressWarnings("unchecked")
    public void updateEntityLookupTable() {
        List<AccessRecord> records = accessRecordDao.nextRecords();
        Map<String, Object>[] batchValues = new Map[records.size()];
        for (int i = 0; i < records.size(); i++) {
            AccessRecord record = records.get(i);
            Map<String, Object> value = new HashMap<String, Object>();
            value.put(":sessionId", record.getSessionId());
            String entityId = new EntityIdReader().read(record);
            if (entityId == null) {
                value.put(":entityId", -1L);
            } else {
                if (entityId.startsWith("syn")) {
                    entityId = entityId.substring(3);
                }
                Long entity;
                try {
                    entity = Long.parseLong(entityId);
                } catch (NumberFormatException e) {
                    entity = (long) -1;
                }
                value.put(":entityId", entity);
            }
            batchValues[i] = value;
        }
        logger.info("Updating " + records.size() + " records ...");
        accessRecordEntityDao.insertNewRecords(batchValues);
        logger.info("Finish updating " + records.size() + " records.");
    }
}
