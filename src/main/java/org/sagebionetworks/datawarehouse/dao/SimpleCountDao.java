package org.sagebionetworks.datawarehouse.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.sagebionetworks.datawarehouse.model.TimeDataPoint;

public interface SimpleCountDao {

    /**
     * Increases the count by 1 for the specified metric at the specified time.
     */
    void put(String metricId, DateTime timestamp);

    /**
     * Gets the counts between the specified time range.
     */
    List<TimeDataPoint> get(String metricId, DateTime from, DateTime to);
}
