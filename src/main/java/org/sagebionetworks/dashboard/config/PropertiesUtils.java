package org.sagebionetworks.dashboard.config;

import java.util.Properties;

final class PropertiesUtils {

    /**
     * Merges additional properties on top of the original ones.
     * If they are on the same key, the original property will be overwritten.
     */
    static Properties mergeProperties(final PropertyReader propertyReader, final Properties original) {
        final Properties merged = new Properties();
        for (final String key : original.stringPropertyNames()) {
            String value = propertyReader.read(key);
            value = value == null ? original.getProperty(key) : value;
            merged.setProperty(key, value);
        }
        return merged;
    }

    /**
     * Merges additional properties on top of the original ones.
     * If they are on the same key, the original property will be overwritten.
     */
    static Properties mergeProperties(final Properties additional, final Properties original) {
        final Properties merged = new Properties();
        for (final String key : original.stringPropertyNames()) {
            merged.setProperty(key, original.getProperty(key));
        }
        for (final String key : additional.stringPropertyNames()) {
            merged.setProperty(key, additional.getProperty(key));
        }
        return merged;
    }

    private PropertiesUtils() {}
}
