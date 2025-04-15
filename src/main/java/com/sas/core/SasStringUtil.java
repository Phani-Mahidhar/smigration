package com.sas.core;

public class SasStringUtil {
  public static boolean isStringLiteral(String sasString) {
    return sasString.startsWith("'") && sasString.endsWith("'");
  }

  public static boolean isResolvable(String sasString) {
    return !isStringLiteral(sasString);
  }

  public static boolean isDoubleQuoted(String sasString) {
    return sasString.startsWith("\"") && sasString.endsWith("\"");
  }

  public static String stripQuotes(String sasString) {
    return sasString.substring(1, sasString.length() - 1);
  }
}
