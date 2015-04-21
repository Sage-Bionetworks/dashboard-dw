package org.sagebionetworks.dashboard.dao.redshift;

import javax.annotation.Resource;

import org.sagebionetworks.dashboard.dao.BridgeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class BridgeDaoImpl implements BridgeDao {

    private final Logger logger = LoggerFactory.getLogger(BridgeDaoImpl.class);

    @Resource
    private NamedParameterJdbcTemplate dwTemplate;

    @Override
    public void create(String tableName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(String tableName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void copy(String s3Path, String dwTable) {
        // TODO Auto-generated method stub
        
    }
}
