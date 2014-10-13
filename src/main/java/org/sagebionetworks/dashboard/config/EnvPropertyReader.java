package org.sagebionetworks.dashboard.config;

public class EnvPropertyReader implements PropertyReader {
    @Override
    public String read(final String key) {
        // Convert to a valid environment variable name
        String envKey = key.toUpperCase().replace(SEPARATOR, "_");
        return System.getenv(envKey);
    }
}
