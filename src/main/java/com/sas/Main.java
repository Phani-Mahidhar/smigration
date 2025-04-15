package com.sas;

import com.sas.core.SasFile;
import com.sas.parser.SasFileParser;
import com.sas.parser.SasParser;
import com.sas.transformer.pyspark.PySparkConfig;
import com.sas.transformer.pyspark.PySparkTransformer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: java Main <folder-or-file-path>");
      return;
    }

    File fileOrFolder = new File(args[0]);
    File outputFolder = new File(args[1]);
    if (!outputFolder.exists()) {
      outputFolder.mkdirs();
    }
    if (!outputFolder.isDirectory()) {
      throw new InvalidParameterException("The specified path is not a directory: " + args[1]);
    }
    if (!fileOrFolder.exists()) {
      throw new InvalidParameterException("The specified path does not exist: " + args[0]);
    }
    Map<Path, SasParser> parserMap = new HashMap<>();

    // Part 1: Parse the SAS file(s)
    if (fileOrFolder.isFile()) {
      Path filePath = fileOrFolder.toPath();
      parserMap.putAll(SasFileParser.parseFile(filePath));
    } else {
      for (File file : Objects.requireNonNull(fileOrFolder.listFiles())) {
        Path filePath = file.toPath();
        if (file.isFile() && SasFileParser.isSasFile(filePath)) {
          System.out.println("Parsing file: " + filePath + "...");
          try {
            parserMap.putAll(SasFileParser.parseFile(filePath));
          } catch (Exception e) {
            System.err.println("Error parsing file: " + filePath);
          }
          System.out.println("Parsed file: " + filePath);
        }
      }
    }

    // Part 2: Translate to PySpark
    for (Map.Entry<Path, SasParser> entry : parserMap.entrySet()) {
      Path filePath = entry.getKey();
      SasParser parser = entry.getValue();
      System.out.println("Translating file: " + filePath + " to PySpark...");
      try {
        SasFile file = buildCanonical(parser);
        String pySparkCode = canonicalToPySpark(file);
        String outFilename;
        if (filePath.toString().endsWith(".sas")) {
          outFilename = filePath.getFileName().toString().replace(".sas", ".py");
        } else {
          outFilename = filePath.getFileName().toString().replace(".inc", ".py");
        }
        // create path with output folder
        Path outputPath = outputFolder.toPath().resolve(outFilename);
        Files.writeString(outputPath, pySparkCode);
        System.out.println("Successfully wrote output to: " + outputPath);
      } catch (IOException e) {
        System.err.println("Error writing to output file: " + e.getMessage());
      } catch (Exception e) {
        System.err.println("Error translating file: " + filePath + " to PySpark: " + e.getMessage());
      }
    }

  }

  private static SasFile buildCanonical(SasParser parser) {
    return SasFile.buildCanonical(parser);
  }

  private static String canonicalToPySpark(SasFile file) {
    return new PySparkTransformer(PySparkConfig.DEFAULT).translateSasFile(file);
  }
}
