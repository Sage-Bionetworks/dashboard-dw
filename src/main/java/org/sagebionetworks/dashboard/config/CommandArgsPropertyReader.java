package org.sagebionetworks.dashboard.config;

public class CommandArgsPropertyReader implements PropertyReader {
    @Override
    public String read(final String key) {
        return System.getProperty(key);
    }
}
