package org.sagebionetworks.dashboard.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service("updateService")
public class UpdateService {

    public void update(InputStream is, String path,
            UpdateFileCallback updateFileCallback,
            UpdateRecordCallback updateRecordCallback) {
        // TODO Auto-generated method stub
        
    }

    public void shutdown() {
        // TODO Auto-generated method stub
        
    }

}
