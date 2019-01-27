package com.cottagecoders;

import org.apache.commons.lang3.StringUtils;

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

import static com.cottagecoders.LogSplitter.OUTPUT_DIR;

public class Parser {
  private static final String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[user:(.*)\\] " +
      "\\[pipeline:(.*)\\] \\[runner:] (.*)$";
  private static final String SAVEDregex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[user:(.*)\\] " +
      "\\[pipeline:(.*)" + "\\] " + "\\[runner:\\d+] \\[thread:(.*)\\](.*)$";

  private static final String NEWregex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[user:(.*)\\] " +
      "\\[(.*)\\]" + "\\[runner:\\d+] \\[thread:(.*)\\](.*)$";

  final Pattern pattern = Pattern.compile(regex);

  Map<String, BufferedWriter> fds = new HashMap<>();

  Parser() {

  }

  void parse(Path inputFile) {

    // open the input file...
    try (BufferedReader brdr = new BufferedReader(new FileReader(inputFile.toFile()))) {
      String record;

      BufferedWriter currentWriter = null;
      while ((record = brdr.readLine()) != null) {

        final Matcher matcher = pattern.matcher(record);

        if (matcher.matches()) {

          // scrub bad chars from pipeline name...
          String pipeline = matcher.group(4).replaceAll("[^-a-zA-Z0-9_]", "_");
          // is the pipeline name empty?
          if (StringUtils.isEmpty(pipeline) || pipeline.equals("_")) {
            continue;
          }

          String destinationFileName = OUTPUT_DIR + pipeline + ".log";

          if (!fds.containsKey(destinationFileName)) {
            fds.put(destinationFileName, new BufferedWriter(new FileWriter(destinationFileName)));
            currentWriter = fds.get(destinationFileName);
          } else {
            if (currentWriter == null || currentWriter != fds.get(destinationFileName)) {
              currentWriter = fds.get(destinationFileName);
            }
          }

        }
        currentWriter.write(record + "\n");
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
