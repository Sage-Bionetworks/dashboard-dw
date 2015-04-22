package org.sagebionetworks.dashboard.dao.redshift;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.DwDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("dwDao")
public class DwDaoImpl implements DwDao {

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    @Override
    public void createTable(final String createTableQuery) {
        execute(createTableQuery);
    }

    @Override
    public void dropTable(final String dropTableQuery) {
        execute(dropTableQuery);
    }

    private void execute(final String query) {
        dwTemplate.execute(query, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.execute();
                ResultSet r = ps.getResultSet();
                System.out.println(r);
                return r;
            }
        });
    }
}
