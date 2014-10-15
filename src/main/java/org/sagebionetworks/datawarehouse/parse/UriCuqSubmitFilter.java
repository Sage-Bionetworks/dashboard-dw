package org.sagebionetworks.datawarehouse.parse;

public class UriCuqSubmitFilter implements RecordFilter<AccessRecord> {
    @Override
    public boolean matches(AccessRecord record) {
        String uri = record.getUri();
        return "/repo/v1/certifiedUserTestResponse".equalsIgnoreCase(uri);
    }
}
