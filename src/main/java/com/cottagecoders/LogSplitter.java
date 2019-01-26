package com.cottagecoders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LogSplitter {

  LogSplitter() {

    // gather all the start options

    // for now remove the "output" subdirectory

    // gather the sdc.log files


    // sort set.

    // reverse set.

  }

  public static void main(String[] args) {

    Set<String> fileNames = new TreeSet<>();

    LogSplitter logSplitter = new LogSplitter();
    fileNames = logSplitter.gatherFileNames();
    logSplitter.reverseSort(fileNames);

    Parser parser = new Parser();
    for (String inputFileName : fileNames) {
      parser.parse(inputFileName);
    }
    parser.closeAll();
  }

  void reverseSort(Set<String> fileNames) {

  }

  void start() {
    // process each file, they'll be in reverse order.

    // we find the oldest log records at the top of the last file.

  }

  }

  Set<String> gatherFileNames() {

  }
}
