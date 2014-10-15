package org.sagebionetworks.datawarehouse;

import javax.annotation.Resource;

import org.sagebionetworks.datawarehouse.service.RepoUserWorker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Updates the list of Synapse users on a daily basis.
 */
@Component("repoUserScheduler")
public class RepoUserScheduler {

    @Resource
    private RepoUserWorker repoUserWorker;

    /**
     * Scheduled to run every 8 hour.
     */
    @Scheduled(initialDelay=1L, fixedRate=(8L * 60L * 60L * 1000L))
    public void run() {
        repoUserWorker.doWork();
    }
}
