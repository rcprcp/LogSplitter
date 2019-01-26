package com.cottagecoders;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LogSplitter {

    public static void main(String[] args) {

        LogSplitter logSplitter = new LogSplitter();
        Set<String> fileNames = logSplitter.gatherFileNames();
        logSplitter.reverseSort(fileNames);

        // create the regex matcher...



        for(String inputFileName : fileNames) {
            logSplitter.parse(inputFileName);
        }
    }

    LogSplitter() {
        Map<String, File> fds = new HashMap<>();

        // gather all the start options

        // for now remove the "output" subdirectory

        // gather the sdc.log files


        // sort set.

        // reverse set.

    }

    void reverseSort(Set<String> fileNames) {

    }

    void start() {
        // process each file, they'll be in reverse order.

        // we find the oldest log records at the top of the last file.

    }

    void parse(String inputFileName) {
        try (BufferedReader brdr = new BufferedReader(FileReader(inputFileName))) {
            String record;
            while((record = brdr.readLine()) != null) {

            }

        } catch(IOException ex) {

        }

    }

    Set<String> gatherFileNames() {

    }
}
