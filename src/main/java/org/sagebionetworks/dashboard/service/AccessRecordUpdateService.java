package org.sagebionetworks.dashboard.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.AccessRecordDao;
import org.sagebionetworks.dashboard.dao.FailedRecordDao;
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.sagebionetworks.dashboard.parse.AccessRecord;
import org.sagebionetworks.dashboard.parse.RecordParser;
import org.sagebionetworks.dashboard.parse.RepoRecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("updateService")
public class AccessRecordUpdateService {
    private final Logger logger = LoggerFactory.getLogger(AccessRecordUpdateService.class);

    @Resource
    private AccessRecordDao accessRecordDao;

    @Resource
    private FailedRecordDao failedRecordDao;

    @Resource
    private LogFileDao logFileDao;

    private final RecordParser parser = new RepoRecordParser();

    public void update(InputStream in, String filePath) {
        update(in, filePath, 0);
    }

    /**
     * @param in             Input stream to read the metrics.
     * @param filePath       The file path that's behind the input stream.
     *                       This is used as the key to track the progress.
     * @param startLineIncl  1-based starting line number.
     */
    public void update(final InputStream in, final String filePath, final int startLineIncl) {

        String id = UUID.randomUUID().toString();
        try {
            // type 0 for access_record
            logFileDao.put(filePath, id, 0);
        } catch (Throwable exception) {
            // if it's not a duplicate file, log the error
            if (!exception.getMessage().contains("already exists")) {
                logger.error("Failed to insert file " + filePath);
            }
            return;
        }

        GZIPInputStream gzis = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        int lineCount = 0;
        try {
            gzis = new GZIPInputStream(in);
            ir = new InputStreamReader(gzis, StandardCharsets.UTF_8);
            br = new BufferedReader(ir);
            List<AccessRecord> records = parser.parse(br);
            for (AccessRecord record : records) {
                lineCount++;
                if (lineCount >= startLineIncl) {
                    updateRecord(record, filePath, id, lineCount);
                }
            }
        } catch (Throwable e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (ir != null) {
                    ir.close();
                }
                if (gzis != null) {
                    gzis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            logFileDao.update(id);
            logger.info("Done inserting file " + filePath + " with " + lineCount + "lines.");
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
    }

    private void updateRecord(AccessRecord record, String filePath, String file_id, int lineNumber) {
        /*try {
            accessRecordDao.put(record, file_id);
        } catch (Throwable e) {
            if (!e.getMessage().contains("already exists")) {
                try {
                    failedRecordDao.put(file_id, lineNumber, record.getSessionId());
                } catch (Throwable e2) {
                    logger.error(e2.getMessage());
                }
            }
        }*/
    }

}
