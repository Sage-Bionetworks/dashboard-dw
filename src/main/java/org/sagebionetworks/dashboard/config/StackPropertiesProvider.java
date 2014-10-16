package org.sagebionetworks.dashboard.config;

import java.util.Properties;

/**
 * Stack-sensitive configuration properties.
 */
public class StackPropertiesProvider implements PropertiesProvider {

    private final Properties properties;

    public StackPropertiesProvider(final Stack stack, final PropertiesProvider provider) {
        final Properties src = provider.getProperties();
        final Properties dest = new Properties();
        // First, read the non-stack-based default properties
        for (final String key : src.stringPropertyNames()) {
            if (!isStackPrefixed(key)) {
                dest.setProperty(key, src.getProperty(key));
            }
        }
        // Then, overwrite with properties only for this stack
        for (final String key : src.stringPropertyNames()) {
            final String newKey = removeStackPrefix(key, stack);
            if (newKey != null) {
                dest.setProperty(newKey, src.getProperty(key));
            }
        }
        properties = dest;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    private boolean isStackPrefixed(final String key) {
        for (final Stack stack : Stack.values()) {
            final String prefix = getStackPrefix(stack);
            if (isStackPrefixed(key, prefix)) {
                return true;
            }
        }
        return false;
    }

    private String removeStackPrefix(final String key, final Stack stack) {
        final String prefix = getStackPrefix(stack);
        if (isStackPrefixed(key, prefix)) {
            return key.substring(prefix.length());
        }
        return null;
    }

    private boolean isStackPrefixed(final String key, final String stackPrefix) {
        return key.toLowerCase().startsWith(stackPrefix);
    }

    private String getStackPrefix(final Stack stack) {
        return stack.name().toLowerCase() + PropertyReader.SEPARATOR;
    }
}
