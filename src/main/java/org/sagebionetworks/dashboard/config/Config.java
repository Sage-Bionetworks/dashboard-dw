package org.sagebionetworks.dashboard.config;

public interface Config {

    Stack getStack();

    String get(String key);

    boolean getBoolean(String key);

    int getInt(String key);

    long getLong(String key);
}
