package org.sagebionetworks.datawarehouse.model;

public class FileFailure {

    public FileFailure(String file, int lineNumber) {
        this.file = file;
        this.lineNumber = lineNumber;
    }

    public String getFile() {
        return file;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    private final String file;
    private final int lineNumber;
}
