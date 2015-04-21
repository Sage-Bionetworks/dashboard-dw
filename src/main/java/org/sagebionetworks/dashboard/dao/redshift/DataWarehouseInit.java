package org.sagebionetworks.dashboard.dao.redshift;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.amazonaws.util.IOUtils;

@Component("dwInit")
public class DataWarehouseInit {

    private final Logger logger = LoggerFactory.getLogger(DataWarehouseInit.class);
    private final NamedParameterJdbcTemplate dwTemplate;

    @Autowired
    public DataWarehouseInit(NamedParameterJdbcTemplate dwTemplate) {
        this.dwTemplate = dwTemplate;
        createTable("/spring/LogFileTable.sql");
        createTable("/spring/RawAccessRecordTable.sql");
        createTable("/spring/AccessRecordTable.sql");
        logger.info("Data warehouse initialzied.");
    }

    private void createTable(String path) {
        try (final InputStream source = this.getClass().getResourceAsStream(path)) {
            if (source == null) {
                logger.error("The source " + path
                        + "is null. Data warehouse initiation has failed.");
                return;
            }
            final String query = IOUtils.toString(source);
            dwTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {
                    return ps.execute();
                }
            });
        } catch (DataAccessException | IOException e) {
            logger.error("Data warehouse initiation failure.", e);
            return;
        }
    }
}
