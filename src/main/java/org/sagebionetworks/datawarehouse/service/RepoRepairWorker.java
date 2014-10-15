package org.sagebionetworks.datawarehouse.service;

import java.util.List;

import javax.annotation.Resource;

import org.sagebionetworks.datawarehouse.context.DashboardContext;
import org.sagebionetworks.datawarehouse.dao.FileStatusDao;
import org.sagebionetworks.datawarehouse.model.FileFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("repoRepairWorker")
public class RepoRepairWorker {

    private final Logger logger = LoggerFactory.getLogger(RepoRepairWorker.class);

    @Resource
    private DashboardContext datawarehouseContext;

    @Resource
    private FileStatusDao fileStatusDao;

    @Resource
    private RepoRecordWorker repoRecordWorker;

    public void doWork() {
        final String bucket = datawarehouseContext.getAccessRecordBucket();
        final List<FileFailure> failures = fileStatusDao.getFailures();
        for (FileFailure failure : failures) {
            final String key = failure.getFile();
            final int line = failure.getLineNumber();
            logger.info("Repairing " + key + " starting from line " + line);
            repoRecordWorker.updateFile(bucket, key, line);
        }
    }
}
