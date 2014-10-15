package org.sagebionetworks.datawarehouse.parse;

public class StatusCodeReader implements RecordReader<AccessRecord, String> {
    @Override
    public String read(AccessRecord record) {
        return record.getStatus();
    }
}
