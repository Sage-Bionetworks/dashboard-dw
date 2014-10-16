package org.sagebionetworks.dashboard.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>Load properties in the following order:
 * <ol>
 *   <li>Load all the properties defined in the specified configuration file.
 *   <li>If a property is for the specified stack, it is chosen over the default value; otherwise,
 *       the default value is used.
 *   <li>Overwrite properties with environment variables.
 *   <li>Overwrite properties with command-line arguments.
 * </ol>
 */
public class DefaultConfig extends AbstractConfig {

    private final Stack stack;
    private final Properties properties;

    public DefaultConfig(final Stack stack, final String configFile) throws IOException {
        this.stack = stack;
        PropertiesProvider provider =
                new CommandArgsPropertiesProvider(
                new EnvPropertiesProvider(
                new StackPropertiesProvider(stack,
                new FilePropertiesProvider(new File(configFile)))));
        properties = provider.getProperties();
    }

    @Override
    public Stack getStack() {
        return stack;
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }
}
