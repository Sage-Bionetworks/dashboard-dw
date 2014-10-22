package org.sagebionetworks.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.dashboard.service.UpdateService;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    // Run with 'gradle run -PfilePath=/path/to/access/log/files'
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Must provide the file path to the access log files.");
        }
        final File filePath = new File(args[0]);
        if (!filePath.exists()) {
            throw new IllegalArgumentException("File " + filePath.getPath() + " does not exist.");
        }

        final Logger logger = org.slf4j.LoggerFactory.getLogger(App.class);
        final boolean isProd = Boolean.parseBoolean(args[1]);
        logger.info("Prod = " + isProd);
        if (isProd) {

            @SuppressWarnings("resource")
            final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/scheduler-context.xml");
            context.registerShutdownHook();
            context.start();
        } else {
            final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/app-context.xml");
            context.registerShutdownHook();

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
