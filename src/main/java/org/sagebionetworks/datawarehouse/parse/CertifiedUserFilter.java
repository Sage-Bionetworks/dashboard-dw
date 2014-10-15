package org.sagebionetworks.datawarehouse.parse;

public class CertifiedUserFilter implements RecordFilter<CuPassingRecord>{

    @Override
    public boolean matches(CuPassingRecord record) {
        return record.isPassed();
    }
}
