package org.sagebionetworks.dashboard.dao.redshift;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.amazonaws.util.IOUtils;

public class DataWarehouseInit {

    private final Logger logger = LoggerFactory.getLogger(DataWarehouseInit.class);
    private final String TABLE_EXIST = "SELECT * FROM pg_tables WHERE tablename = ':tablename';";

    public DataWarehouseInit(NamedParameterJdbcTemplate dwTemplate) {
        createTable(dwTemplate, "/META-INF/spring/LogFileTable.sql", "log_file");
        createTable(dwTemplate, "/META-INF/spring/RawAccessRecordTable.sql", "raw_access_record");
        createTable(dwTemplate, "/META-INF/spring/AccessRecordTable.sql", "access_record");

        logger.info("Data warehouse initialzied.");
    }

    private void createTable(NamedParameterJdbcTemplate dwTemplate, String path, String tablename) {

        if (isExists(dwTemplate, tablename)) {
            logger.info("Skip " + tablename + ". Reason: already existed.");
            return;
        }

        InputStream source = this.getClass().getResourceAsStream(path);

        if (source == null) {
            logger.info("Failed to read the source from " + path
                    + ". Data warehouse initiation failure.");
            return;
        }

        String query = null;
        try {
            query = IOUtils.toString(source);
        } catch (IOException e) {
            logger.info("Failed to convert InputStream into String."
                    + " Data warehouse initiation failure.");
            return;
        }

        try {
            dwTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {
                    return ps.execute();
                }});
        } catch (DataAccessException e) {
            logger.info("Failed to create table in file " + path);
        }
    }

    private boolean isExists(NamedParameterJdbcTemplate dwTemplate, String tablename) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tablename", tablename);
        List<String> result = dwTemplate.query(TABLE_EXIST, parameter, new TableRowMapper());
        if (result.isEmpty()) {
            return false;
        }
        return true;
    }

    private static class TableRowMapper implements RowMapper<String> {
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("tablename");
        }
    }
}
