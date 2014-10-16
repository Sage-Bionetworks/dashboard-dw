package org.sagebionetworks.dashboard.config;

import java.util.Properties;

class BasicPropertiesProvider implements PropertiesProvider {

    private final Properties properties;

    BasicPropertiesProvider(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Properties getProperties() {
        return new Properties(properties);
    }
}
