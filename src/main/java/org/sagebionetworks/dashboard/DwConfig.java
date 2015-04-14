package org.sagebionetworks.dashboard;

import java.io.IOException;

import org.sagebionetworks.dashboard.config.Config;
import org.sagebionetworks.dashboard.config.DefaultConfig;
import org.sagebionetworks.dashboard.config.Stack;
import org.springframework.stereotype.Component;

@Component("dwConfig")
public class DwConfig implements Config {

    private final Config config;

    public DwConfig() {
        try {
            final String srcConfigFile = getClass().getResource("/dashboard-dw.config").getFile();
            final String userHome = System.getProperty("user.home");
            final String homeConfigFile = userHome + "/.dashboard/dashboard-dw.config";
            config = new DefaultConfig(srcConfigFile, homeConfigFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String key) {
        return config.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    @Override
    public int getInt(String key) {
        return config.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return config.getLong(key);
    }

    @Override
    public Stack getStack() {
        return config.getStack();
    }

    public String getDwUrl() {
        return config.get("dw.url");
    }

    public String getDwUsername() {
        return config.get("dw.username");
    }

    public String getDwPassword() {
        return config.get("dw.password");
    }

    public String getSynapseUser() {
        return config.get("synapse.user");
    }

    public String getSynapsePassword() {
        return config.get("synapse.password");
    }

    public String getAccessRecordBucket() {
        return config.get("access.record.bucket");
    }

    public String getAwsAccessKey() {
        return config.get("aws.access.key");
    }

    public String getAwsSecretKey() {
        return config.get("aws.secret.key");
    }
}
