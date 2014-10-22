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
import org.sagebionetworks.dashboard.parse.AccessRecord;
import org.sagebionetworks.dashboard.parse.RecordParser;
import org.sagebionetworks.dashboard.parse.RepoRecordParser;
import org.sagebionetworks.dashboard.service.UpdateFileCallback;
import org.sagebionetworks.dashboard.service.UpdateRecordCallback;
import org.sagebionetworks.dashboard.service.UpdateFileCallback.UpdateResult;
import org.sagebionetworks.dashboard.service.UpdateFileCallback.UpdateStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("updateService")
public class UpdateService {
    private final Logger logger = LoggerFactory.getLogger(UpdateService.class);

    @Resource
    private AccessRecordDao dw;

    private final RecordParser parser = new RepoRecordParser();

    public void update(InputStream in, String filePath, 
            UpdateFileCallback fileCallback, UpdateRecordCallback recordCallback) {
        update(in, filePath, 0, fileCallback, recordCallback);
    }

    /**
     * @param in             Input stream to read the metrics.
     * @param filePath       The file path that's behind the input stream.
     *                       This is used as the key to track the progress.
     * @param startLineIncl  1-based starting line number.
     * @param callback       Callback to receive the update results.
     */
    public void update(final InputStream in, final String filePath, final int startLineIncl,
            final UpdateFileCallback fileCallback, final UpdateRecordCallback recordCallback) {
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
                    updateRecord(record, filePath, lineCount, recordCallback);
                }
            }
        } catch (Throwable e) {
            //There will be no file with failed status.
            UpdateResult result = new UpdateResult(filePath, lineCount, UpdateStatus.FAILED);
            //fileCallback.call(result);
            logger.error(result.toString(), e);
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

        UpdateResult result = new UpdateResult(filePath, lineCount, UpdateStatus.SUCCEEDED);
        fileCallback.call(result);
        logger.info(result.toString());
    }

    private void updateRecord(AccessRecord record, String filePath,
            int lineCount, UpdateRecordCallback recordCallback) {
        dw.put(record, UUID.randomUUID().toString());
    }

}
