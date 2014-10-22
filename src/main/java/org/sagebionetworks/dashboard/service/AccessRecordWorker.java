package org.sagebionetworks.dashboard.service;

import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DashboardConfig;
import org.sagebionetworks.dashboard.service.AccessLogFileFetcher;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

@Service("accessRecordWorker")
public class AccessRecordWorker {

    @Resource
    private DashboardConfig dashboardConfig;

    @Resource
    private AccessLogFileFetcher repoFileFetcher;

    @Resource
    private UpdateService updateService;

    @Resource
    private AmazonS3 s3Client;

    public void doWork() {
        final String bucket = dashboardConfig.getAccessRecordBucket();
        List<String> batch = repoFileFetcher.nextBatch();
        for (final String key : batch) {
            updateFile(bucket, key, 0);
        }
    }

    public void updateFile(final String bucket, final String key, final int startingLine) {

        // Read the file to update the metrics
        S3Object file = s3Client.getObject(bucket, key);
        try {
            updateService.update(file.getObjectContent(), key, startingLine);
        } finally {
            try {
                file.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
