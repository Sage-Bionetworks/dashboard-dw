package org.sagebionetworks.dashboard.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InputStreamPropertiesProvider implements PropertiesProvider {

    private final Properties properties;

    /**
     * The specified stream remains open after this method returns.
     */
    public InputStreamPropertiesProvider(InputStream inputStream) throws IOException {
        properties = new Properties();
        properties.load(inputStream);
    }

    /**
     * The specified stream remains open after this method returns.
     */
    public InputStreamPropertiesProvider(InputStream inputStream, PropertiesProvider original)
            throws IOException {
        Properties additional = new Properties();
        additional.load(inputStream);
        properties = PropertiesUtils.mergeProperties(additional, original.getProperties());
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
