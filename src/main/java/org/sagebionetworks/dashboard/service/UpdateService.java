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
import org.sagebionetworks.dashboard.dao.LogFileDao;
import org.sagebionetworks.dashboard.parse.AccessRecord;
import org.sagebionetworks.dashboard.parse.RecordParser;
import org.sagebionetworks.dashboard.parse.RepoRecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("updateService")
public class UpdateService {
    private final Logger logger = LoggerFactory.getLogger(UpdateService.class);

    @Resource
    private AccessRecordDao acessRecordDao;

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
     * @param callback       Callback to receive the update results.
     */
    @Transactional
    public void update(final InputStream in, final String filePath, final int startLineIncl) {

        String id = UUID.randomUUID().toString();
        // type 0 for access_record
        logFileDao.put(filePath, id, 0);

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
                    updateRecord(record, filePath, id);
                }
            }
        } catch (Throwable e) {
            //There will be no file with failed status.
            logger.error("Failed to insert file " + filePath);
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

        logger.info("Done inserting file " + filePath + " with " + lineCount + "lines.");
    }

    private void updateRecord(AccessRecord record, String filePath, String file_id) {
        acessRecordDao.put(record, file_id);
    }

}
