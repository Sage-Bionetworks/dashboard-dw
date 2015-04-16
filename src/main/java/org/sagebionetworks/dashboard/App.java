package org.sagebionetworks.dashboard;

import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {

        @SuppressWarnings("resource")
        final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("/spring/app-context.xml");
        context.registerShutdownHook();
        context.start();
        LogFileDao logfileDao = context.getBean(LogFileDao.class);
        logfileDao.cleanupProcessingFile();
    }
}
