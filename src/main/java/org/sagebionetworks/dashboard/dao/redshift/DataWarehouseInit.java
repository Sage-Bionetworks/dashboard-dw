package org.sagebionetworks.dashboard.dao.redshift;

import java.io.IOException;
import java.io.InputStream;

import org.sagebionetworks.dashboard.dao.DwDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.amazonaws.util.IOUtils;

@Component("dwInit")
public class DataWarehouseInit {

    private final Logger logger = LoggerFactory.getLogger(DataWarehouseInit.class);
    private final DwDao dwDao;

    @Autowired
    public DataWarehouseInit(DwDao dwDao) {
        this.dwDao = dwDao;
        createTable("/sql/log_file.sql");
        createTable("/sql/raw_access_record.sql");
        createTable("/sql/access_record.sql");
        logger.info("Data warehouse initialzied.");
    }

    private void createTable(String path) {
        try (final InputStream source = this.getClass().getResourceAsStream(path)) {
            if (source == null) {
                logger.error("The source " + path
                        + " is null. Data warehouse initiation has failed.");
                return;
            }
            final String query = IOUtils.toString(source);
            dwDao.createTable(query);
        } catch (DataAccessException | IOException e) {
            logger.error("Data warehouse initiation failure.", e);
            return;
        }
    }
}
