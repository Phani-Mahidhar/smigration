package com.sas.transformer.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Utilities for creating strings of spaces.
 */
public class Spaces {

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private static final List<String> SPACE_LIST = new SpaceList();

  public static final CharSequence MAX = sequence(Integer.MAX_VALUE);

  // Utility class. Do not instantiate.
  private Spaces() {
  }

  /**
   * Creates a sequence of {@code n} spaces.
   */
  public static CharSequence sequence(int n) {
    return new SpaceString(n);
  }

  /**
   * Returns a string of {@code n} spaces.
   */
  public static String of(int n) {
    return SPACE_LIST.get(n);
  }

  /**
   * Appends {@code n} spaces to an {@link Appendable}.
   */
  public static Appendable append(Appendable buf, int n) throws IOException {
    buf.append(MAX, 0, n);
    return buf;
  }

  /**
   * Appends {@code n} spaces to a {@link PrintWriter}.
   */
  public static PrintWriter append(PrintWriter pw, int n) {
    pw.append(MAX, 0, n);
    return pw;
  }

  /**
   * Appends {@code n} spaces to a {@link StringWriter}.
   */
  public static StringWriter append(StringWriter pw, int n) {
    pw.append(MAX, 0, n);
    return pw;
  }

  /**
   * Appends {@code n} spaces to a {@link StringBuilder}.
   */
  public static StringBuilder append(StringBuilder buf, int n) {
    buf.append(MAX, 0, n);
    return buf;
  }

  /**
   * Appends {@code n} spaces to a {@link StringBuffer}.
   */
  public static StringBuffer append(StringBuffer buf, int n) {
    buf.append(MAX, 0, n);
    return buf;
  }

  /**
   * Returns a string that is padded on the right with spaces to the given
   * length.
   */
  public static String padRight(String string, int n) {
    final int x = n - string.length();
    if (x <= 0) {
      return string;
    }
    // Replacing StringBuffer with String would hurt performance.
    //noinspection StringBufferReplaceableByString
    return append(new StringBuilder(string), x).toString();
  }

  /**
   * Returns a string that is padded on the left with spaces to the given
   * length.
   */
  public static String padLeft(String string, int n) {
    final int x = n - string.length();
    if (x <= 0) {
      return string;
    }
    // Replacing StringBuffer with String would hurt performance.
    //noinspection StringBufferReplaceableByString
    return append(new StringBuilder(), x).append(string).toString();
  }

  /**
   * A string of spaces.
   */
  private static class SpaceString implements CharSequence {
    private final int length;

    private SpaceString(int length) {
      this.length = length;
    }

    // Do not override equals and hashCode to be like String. CharSequence does
    // not require it.

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
      return of(length);
    }

    public int length() {
      return length;
    }

    public char charAt(int index) {
      return ' ';
    }

    public CharSequence subSequence(int start, int end) {
      return new SpaceString(end - start);
    }
  }

  /**
   * List whose {@code i}th entry is a string consisting of {@code i} spaces.
   * It populates itself the first time you ask for a particular string, and
   * caches the result.
   */
  private static class SpaceList extends CopyOnWriteArrayList<String> {
    @Override
    public String get(int index) {
      for (; ; ) {
        try {
          return super.get(index);
        } catch (IndexOutOfBoundsException e) {
          if (index < 0) {
            throw e;
          }
          populate(Math.max(16, index + 1));
        }
      }
    }

    /**
     * Populates this list with all prefix strings of a given string. All
     * of the prefix strings share the same backing array of chars.
     */
    private synchronized void populate(int newSize) {
      final int size = size();
      if (newSize <= size) {
        return;
      }
      final char[] chars = new char[newSize];
      Arrays.fill(chars, ' ');
      final int length = newSize - size;
      final int offset = size;

      // addAll is much more efficient than repeated add for
      // CopyOnWriteArrayList
      addAll(
        new AbstractList<String>() {
          public String get(int index) {
            return String.valueOf(chars, 0, offset + index);
          }

          public int size() {
            return length;
          }
        });
    }
  }
}

