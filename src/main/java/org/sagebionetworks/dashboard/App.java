package org.sagebionetworks.dashboard;

import java.io.File;

public class App {

    // Run with 'gradle run -PfilePath=/path/to/access/log/files'
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Must provide the file path to the access log files.");
        }
        final File filePath = new File(args[0]);
        if (!filePath.exists()) {
            throw new IllegalArgumentException("File " + filePath.getPath() + " does not exist.");
        }

    }

}
