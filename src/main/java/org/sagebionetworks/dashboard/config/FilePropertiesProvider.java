package org.sagebionetworks.dashboard.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePropertiesProvider implements PropertiesProvider {

    private final Properties properties;

    public FilePropertiesProvider(File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        try {
            InputStreamPropertiesProvider provider = new InputStreamPropertiesProvider(inputStream);
            properties = provider.getProperties();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public FilePropertiesProvider(File file, PropertiesProvider original) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            InputStreamPropertiesProvider provider = new InputStreamPropertiesProvider(inputStream);
            properties = PropertiesUtils.mergeProperties(
                    provider.getProperties(), original.getProperties());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
