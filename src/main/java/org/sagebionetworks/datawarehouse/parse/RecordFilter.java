package org.sagebionetworks.datawarehouse.parse;

public interface RecordFilter<R extends Record> {
    boolean matches(R record);
}
