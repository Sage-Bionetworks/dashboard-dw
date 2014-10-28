package org.sagebionetworks.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.dashboard.config.DashboardConfig;
import org.sagebionetworks.dashboard.config.Stack;
import org.sagebionetworks.dashboard.service.UpdateService;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        final Logger logger = org.slf4j.LoggerFactory.getLogger(App.class);

        if (args[1] != null && !args[1].isEmpty()) {
            System.setProperty("stack", args[1]);
        }

        DashboardConfig config = new DashboardConfig();
        logger.info("    " + config.getStack());
        logger.info("    " + config.get("spring.context"));
        final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(config.get("spring.context"));
        context.registerShutdownHook();
        final Stack stack = config.getStack();
        if (Stack.PROD.equals(stack)) {
            context.start();
        } else {
            final File filePath = new File(args[0]);
            if (!filePath.exists()) {
                context.close();
                throw new IllegalArgumentException("File " + filePath.getPath() + " does not exist.");
            }

            final List<File> files = new ArrayList<File>();
            getCsvGzFiles(filePath, files);
            final int total = files.size();
            logger.info("Total number of files: " + total);
            if (total == 0) {
                context.close();
                return;
            }

            final UpdateService updateService = context.getBean(UpdateService.class);

            final long start = System.nanoTime();
            try {
                for (int i = files.size() - 1; i >= 0; i--) {
                    File file = files.get(i);
                    logger.info("Loading file " + (files.size() - i) + " of " + total);
                    try {
                        InputStream is = new FileInputStream(file);
                        updateService.update(is, file.getPath());
                        if (is != null) {
                            is.close();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                final long end = System.nanoTime();
                logger.info("Done loading log files. Time spent (seconds): " + (end - start) / 1000000000L);
                context.close();
            }
        }
    }

    /**
     * Gets all the "csv.gz" files but exclude the "rolling" ones.
     */
    private static void getCsvGzFiles(File file, List<File> files) {
        if (file.isFile()) {
            final String fileName = file.getName();
            if (fileName.endsWith("csv.gz") && !fileName.contains("rolling")) {
                files.add(file);
            }
            return;
        }
        File[] moreFiles = file.listFiles();
        for (File f : moreFiles) {
            getCsvGzFiles(f, files);
        }
    }
}
