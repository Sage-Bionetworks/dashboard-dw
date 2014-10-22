package org.sagebionetworks.dashboarddw;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.service.AccessRecordWorker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("accessRecordScheduler")
public class AccessRecordScheduler {

    @Resource
    private AccessRecordWorker accessRecordWorker;

    /**
     * Initial delay of 7 minutes. Updates every 23 minutes.
     */
    @Scheduled(initialDelay=(7L * 60L * 1000L), fixedRate=(23L * 60L * 1000L))
    public void runRecordWorker() {
        accessRecordWorker.doWork();
    }
}
