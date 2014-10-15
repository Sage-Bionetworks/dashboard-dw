package org.sagebionetworks.datawarehouse.parse;

public interface AccessRecord extends Record{
    String getSessionId();
    String getUserId();
    String getObjectId();
    String getMethod();
    String getUri();
    String getQueryString();
    String getStatus();
    Long getLatency();
    String getUserAgent();
    String getStack();
    String getHost();
    String getInstance();
    String getVM();
    String getThreadId();
}
