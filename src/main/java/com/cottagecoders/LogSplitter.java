package com.cottagecoders;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

public class LogSplitter {
  public static final String OUTPUT_DIR = "./out/";

  LogSplitter() {

  }

  public static void main(String[] args) {
    TreeSet<Path> fileNames;

    LogSplitter logSplitter = new LogSplitter();
    fileNames = logSplitter.gatherFileNames();

    // wipe out directory and contents.
    try {
      FileUtils.deleteDirectory(new File(OUTPUT_DIR));
    }catch(IOException ex) {
      System.out.println("deleteDirectory " + OUTPUT_DIR + " " + ex.getMessage());
    }

    // create directory...
    try {
      FileUtils.forceMkdir(new File(OUTPUT_DIR));
    } catch(IOException ex) {
      System.out.println("forceMkdir " + OUTPUT_DIR + " " + ex.getMessage());
    }

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
