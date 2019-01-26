package com.cottagecoders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
  private static final String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[user:(.*)\\] " +
      "\\[pipeline:(.*)" + "" + "" + "\\] " + "\\[runner:\\d+] \\[thread:(.*)\\](.*)$";

  final Pattern pattern = Pattern.compile(regex);

  Map<String, BufferedWriter> fds = new HashMap<>();

  Parser() {

  }

  void parse(String inputFileName) {
    // open the input file name
    try (BufferedReader brdr = new BufferedReader(new FileReader(inputFileName))) {
      String record;
      while ((record = brdr.readLine()) != null) {

        // parse the line...
        final Matcher matcher = pattern.matcher(record);
        // is this a log4j record?  get the pipeline name, maybe open a file...
        if (matcher.matches()) {
          // scrub bad chars from pipeline name.
          String pName = matcher.group(4).replaceAll("[^a-zA-Z0-9-_]", "");
          String destinationFileName = "log - " + pName + ".log ";

          if (fds.containsKey(destinationFileName)) {
            fds.get(destinationFileName).write(record);
          } else {
            fds.put(destinationFileName, BufferedWriter(new FileWriter(pName)));
          }
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
