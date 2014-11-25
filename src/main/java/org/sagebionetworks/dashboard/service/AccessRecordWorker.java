package org.sagebionetworks.dashboard.service;

import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DashboardConfig;
import org.sagebionetworks.dashboard.service.AccessLogFileFetcher;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

@Service("accessRecordWorker")
public class AccessRecordWorker {

    @Resource
    private DashboardConfig dashboardConfig;

    @Resource
    private AccessLogFileFetcher repoFileFetcher;

    @Resource
    private RawAccessRecordService rawAccessRecordService;

    @Resource
    private AccessRecordService accessRecordService;

    @Resource
    private AmazonS3 s3Client;

    /**
     * Copy the access_record log files in S3 to raw_access_record table.
     */
    public void copy() {
        final String bucket = dashboardConfig.getAccessRecordBucket();
        final String username = dashboardConfig.getAwsAccessKey();
        final String password = dashboardConfig.getAwsSecretKey();
        List<String> batch = repoFileFetcher.nextBatch();
        for (final String key : batch) {
            rawAccessRecordService.update(bucket, key, username, password);
        }
    }

    /**
     * Update the access_record table.
     */
    public void update() {
        accessRecordService.update();
    }
}
