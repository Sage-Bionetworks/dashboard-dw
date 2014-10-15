package org.sagebionetworks.datawarehouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.datawarehouse.model.WriteRecordResult;
import org.sagebionetworks.datawarehouse.service.RepoUpdateService;
import org.sagebionetworks.datawarehouse.service.UpdateFileCallback;
import org.sagebionetworks.datawarehouse.service.UpdateRecordCallback;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    // Run with 'gradle run -PfilePath=/path/to/access/log/files'
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            throw new IllegalArgumentException("Must provide the file path to the access log files.");
        }
        final File filePath = new File(args[0]);
        if (!filePath.exists()) {
            throw new IllegalArgumentException("File " + filePath.getPath() + " does not exist.");
        }

        final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/app-context.xml");
        context.registerShutdownHook();

        final Logger logger = org.slf4j.LoggerFactory.getLogger(App.class);
/*                final RepoUserWorker userWorker = context.getBean(RepoUserWorker.class);
        logger.info("Loading Synapse users.");
        userWorker.doWork();
        logger.info("Done loading Synapse users.");*/

        final List<File> files = new ArrayList<File>();
        getCsvGzFiles(filePath, files);
        final int total = files.size();
        logger.info("Total number of files: " + total);
        if (total == 0) {
            context.close();
            return;
        }

        final RepoUpdateService updateService = context.getBean(RepoUpdateService.class);
/*        final CuPassingRecordWorker passingRecordWorker = context.getBean(CuPassingRecordWorker.class);
*/
        // Load log files
        final long start = System.nanoTime();
        try {
            for (int i = files.size() - 1; i >= 0; i--) {
                File file = files.get(i);
                logger.info("Loading file " + (files.size() - i) + " of " + total);
                InputStream is = new FileInputStream(file);
                try {
                    updateService.update(is, file.getPath(), 
                            new UpdateFileCallback() {
                                @Override
                                public void call(UpdateResult result) {}
                            },
                            new UpdateRecordCallback() {
                                @Override
                                public void handle(WriteRecordResult result) {}

                            });
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
                //passingRecordWorker.doWork();
            }
        } finally {
            final long end = System.nanoTime();
            logger.info("Done loading log files. Time spent (seconds): " + (end - start) / 1000000000L);
            updateService.shutdown();
            context.close();
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
