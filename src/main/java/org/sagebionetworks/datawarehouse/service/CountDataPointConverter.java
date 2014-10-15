package org.sagebionetworks.datawarehouse.service;

import org.sagebionetworks.datawarehouse.model.CountDataPoint;

public interface CountDataPointConverter {
    CountDataPoint convert(CountDataPoint dataPoint);
}
