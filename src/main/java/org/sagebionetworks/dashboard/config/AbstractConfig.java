package org.sagebionetworks.dashboard.config;

abstract class AbstractConfig implements Config {

    @Override
    public final boolean getBoolean(String key) {
        String value = get(key);
        return Boolean.parseBoolean(value);
    }

    @Override
    public final int getInt(String key) {
        String value = get(key);
        return Integer.parseInt(value);
    }

    @Override
    public final long getLong(String key) {
        String value = get(key);
        return Long.parseLong(value);
    }
}
