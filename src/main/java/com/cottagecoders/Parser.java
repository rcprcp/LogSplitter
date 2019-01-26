package com.cottagecoders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
  private static final String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[user:(.*)\\] " +
      "\\[pipeline:(.*)" + "\\] " + "\\[runner:\\d+] \\[thread:(.*)\\](.*)$";

  final Pattern pattern = Pattern.compile(regex);

  Map<String, BufferedWriter> fds = new HashMap<>();

  Parser() {

  }

  void parse(Path inputFile) {
    // open the input file name
    try (BufferedReader brdr = new BufferedReader(new FileReader(inputFile.toFile()))) {
      String record;
      while ((record = brdr.readLine()) != null) {
        // parse the line...
        final Matcher matcher = pattern.matcher(record);
        // is this a log4j record?  get the pipeline name, maybe open a file...
        if (matcher.matches()) {
          System.out.println("group 1" +matcher.group(1));
          System.out.println("group 2" +matcher.group(2));
          System.out.println("group 3" +matcher.group(3));
          System.out.println("group 4" +matcher.group(4));
          System.exit(1);

          // scrub bad chars from pipeline name.
          String pName = matcher.group(4).replaceAll("[^a-zA-Z0-9-_]", "");
          String destinationFileName = pName + ".log";

          BufferedWriter currentWriter = null;
          if (!fds.containsKey(destinationFileName)) {
            fds.put(destinationFileName, new BufferedWriter(new FileWriter(destinationFileName)));
            currentWriter = fds.get(destinationFileName);
          } else {
            if (currentWriter == null || currentWriter != fds.get(destinationFileName)) {
              currentWriter = fds.get(destinationFileName);
            }
          }
          currentWriter.write(record+"\n");

        } else {
          System.out.println("no match " + record);
        }
      }
    } catch (IOException ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  void closeAll() {
    try {
      for (BufferedWriter br : fds.values()) {
        br.close();
      }
    } catch (IOException ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}
