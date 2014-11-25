package org.sagebionetworks.dashboard.service;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("accessRecordService")
public class AccessRecordService {

    private final Logger logger = LoggerFactory.getLogger(RawAccessRecordService.class);

    @Resource
    private AccessRecordDao accessRecordDao;

    public void update() {
        // insert new raw records into access_record table
        
        // vacuum
        
        // select 1000 null entityId records to update
        // for each record, update the entityId field
    }

}
