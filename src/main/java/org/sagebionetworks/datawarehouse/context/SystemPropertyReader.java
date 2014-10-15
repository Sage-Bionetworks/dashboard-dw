package org.sagebionetworks.datawarehouse.context;

class SystemPropertyReader implements ContextReader {
    @Override
    public String read(String name) {
        return System.getProperty(name);
    }
}
