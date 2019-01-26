package com.cottagecoders;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

public class LogSplitter {

  LogSplitter() {

  }

  public static void main(String[] args) {

    TreeSet<Path> fileNames;

    LogSplitter logSplitter = new LogSplitter();
    fileNames = logSplitter.gatherFileNames();

    Parser parser = new Parser();
    for (Path inputFileName : fileNames.descendingSet()) {
      parser.parse(inputFileName);
    }
    parser.closeAll();
  }

  TreeSet<Path> gatherFileNames() {
    TreeSet<Path> fileNames = new TreeSet<>();
    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get("."), "sdc.log*")) {
      dirStream.forEach(path -> fileNames.add(path));
    } catch (IOException ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
      System.exit(27);
    }
    return fileNames;
  }
}
