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

class Parser {
  private static final String regex = "(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) \\[user:(.*)\\] " +
      "\\[pipeline:(.*)\\] \\[runner:] (.*)$";

  private final Pattern pattern = Pattern.compile(regex);

  private Map<String, BufferedWriter> fds = new HashMap<>();

  Parser() {

  }

  /**
   * Parse the log4j-formatted records.  If the record contains a specifically mentioned pipeline name
   * in [pipeline:name], we use that pipeline name to create an output file, save the
   * BufferedWriter in a map which is keyed by the file name, which is derived from the pipeline name.
   * Then, we write the record to that file.
   *
   * On the subsequent times we get a record with that pipeline name, we just write the record
   * to the <code>BufferedWriter</code> in the map.
   *
   * We save the current <code>BufferedWriter</code>, because we can get stack trace records after the one which has
   * the pipeline name, so we'll concatenate the stack trace to the correct file, too.
   *
   * @param inputFile input file name
   */
  void parse(Path inputFile) {
    try (BufferedReader brdr = new BufferedReader(new FileReader(inputFile.toFile()))) {
      String record;

      BufferedWriter currentWriter = null;
      while ((record = brdr.readLine()) != null) {

        final Matcher matcher = pattern.matcher(record);

        if (matcher.matches()) {

          // scrub bad chars from pipeline name...
          String pipeline = matcher.group(4).replaceAll("[^-a-zA-Z0-9_]", "_");
          // if the pipeline name is empty, we can skip the record.
          if (StringUtils.isEmpty(pipeline) || allSame(pipeline, "_".toCharArray()[0])) {
            continue;
          }

          String destinationFileName = OUTPUT_DIR + pipeline + ".log";

          // check if we have a file open for this pipeline
          if (!fds.containsKey(destinationFileName)) {
            // open file for output and add to map...
            fds.put(destinationFileName, new BufferedWriter(new FileWriter(destinationFileName)));
            // and set as current output file...
            currentWriter = fds.get(destinationFileName);
          } else {
            if (currentWriter == null || currentWriter != fds.get(destinationFileName)) {
              currentWriter = fds.get(destinationFileName);
            }
          }

        }

        // unlikely to happen, but you never know.
        if (currentWriter != null) {
          currentWriter.write(record + "\n");
        }
      }

    } catch (IOException ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
      System.exit(27);
    }
  }

  /**
   * Go through the map of <code>BufferedWriter</code> objects; close them all.
   */
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

  /**
   * check if all the characters in <code>str</code> are all equal to <code>val</code>.
   *
   * @param str String to check
   * @param val substring we're looking for.
   * @return true if all the same.
   */
  private boolean allSame(String str, Character val) {
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) != val) {
        return false;
      }
    }
    return true;
  }
}
