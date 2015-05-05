package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("dwDao")
public class DwDaoImpl implements DwDao {

    private static final String SELECT_TABLES =
            "SELECT tablename " +
            "FROM pg_catalog.pg_tables " +
            "WHERE tablename LIKE :tableNamePrefix;";

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    @Transactional
    @Override
    public void execute(final String query) {
        dwTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                // For creating or dropping tables, this always returns false
                // indicating ps.getResultSet() is always null
                return ps.execute();
            }
        });
    }

    @Override
    public List<String> getTables(final String tableNamePrefix) {
        final Map<String, Object> params = new HashMap<>();
        params.put("tableNamePrefix", tableNamePrefix + "%");
        return dwTemplate.queryForList(SELECT_TABLES, params, String.class);
    }
}
