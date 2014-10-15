package org.sagebionetworks.datawarehouse.parse;

import java.util.regex.Pattern;

/**
 * Extracts the ID value from the request URI field.
 */
public class UriIdReader extends RegexRecordReader {

    public UriIdReader(Pattern pattern) {
        super(pattern);
    }

    @Override
    String readString(AccessRecord record) {
        String uri = record.getUri();
        if (uri != null) {
            uri = uri.replace("v1", "").replace("wiki2", "");
        }
        return uri;
    }
}
