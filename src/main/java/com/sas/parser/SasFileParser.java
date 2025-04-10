package com.sas.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


public class SasFileParser {

  public static Map<Path, com.sas.parser.SasParser> parseFile(Path filePath) {
    if (filePath == null || !filePath.toFile().exists()) {
      throw new IllegalArgumentException("File does not exist");
    }
    if (!isSasFile(filePath)) {
      throw new IllegalArgumentException("File must have a .sas or .inc extension");
    }
    try {
      return Map.of(filePath, parseFileContent(new String(Files.readAllBytes(filePath))));
    } catch (IOException e) {
      throw new IllegalArgumentException("Unexpected Error while reading the file: " + filePath, e);
    } catch (OutOfMemoryError e) {
      throw new IllegalArgumentException("File is too large to read: " + filePath, e);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error reading the file: " + filePath, e);
    }
  }

  private static com.sas.parser.SasParser parseFileContent(String fileContent) {
    CharStream input = CharStreams.fromString(fileContent);
    com.sas.parser.SasLexer lexer = new com.sas.parser.SasLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    return new com.sas.parser.SasParser(tokens);
  }

  public static boolean isSasFile(Path filePath) {
    return filePath.toString().endsWith(".sas") || filePath.toString().endsWith(".inc");
  }
}
