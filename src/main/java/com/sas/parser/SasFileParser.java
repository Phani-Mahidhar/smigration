package com.sas.parser;

import java.nio.file.Path;
import java.util.Map;


public class SasFileParser {

  public static Map<Path, com.sas.parser.SasParser> parseFile(Path filePath) {
    return Map.of(filePath, SasFile.parseFile(filePath));
  }

}
