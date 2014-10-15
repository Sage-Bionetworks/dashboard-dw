package org.sagebionetworks.datawarehouse.parse;

// TODO: This does not distinguish prod from staging
public class ProdFilter implements RecordFilter<AccessRecord> {
    @Override
    public boolean matches(AccessRecord record) {
        return "prod".equalsIgnoreCase(record.getStack());
    }
}
