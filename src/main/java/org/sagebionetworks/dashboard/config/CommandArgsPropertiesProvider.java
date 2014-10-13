package org.sagebionetworks.dashboard.config;

import java.util.Properties;

/**
 * Overwrites with command-line arguments. Stack-insensitive.
 */
public class CommandArgsPropertiesProvider implements PropertiesProvider {

    private final Properties properties;

    public CommandArgsPropertiesProvider(PropertiesProvider provider) {
        PropertyReader cmdArgsReader = new CommandArgsPropertyReader();
        Properties original = provider.getProperties();
        properties = PropertiesUtils.mergeProperties(cmdArgsReader, original);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
