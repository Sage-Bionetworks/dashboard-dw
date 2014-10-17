package org.sagebionetworks.dashboard.service;

import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.config.DashboardConfig;
import org.sagebionetworks.dashboard.model.WriteRecordResult;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

@Service("accessRecordWorker")
public class AccessRecordWorker {

    @Resource
    private DashboardConfig dashboardConfig;

    @Resource
    private RepoFileFetcher repoFileFetcher;

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
            updateService.update(file.getObjectContent(), key, startingLine,
                    new UpdateFileCallback() {
                        @Override
                        public void call(UpdateResult result) {
                            /*if (UpdateStatus.SUCCEEDED.equals(result.getStatus())) {
                                fileStatusDao.setCompleted(key);
                            }*/
                        }
                    },
                    new UpdateRecordCallback() {
                        @Override
                        public void handle(WriteRecordResult result) {
                            //failedRecordDao.put(result);
                        }
                        
                    });
        } finally {
            try {
                file.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
