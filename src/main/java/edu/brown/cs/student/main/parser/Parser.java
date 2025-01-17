package edu.brown.cs.student.main.parser;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.interfaces.CreatorFromRow;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Parser<T> {

  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  private Reader _reader;
  private List<T> _parsedContent;
  private CreatorFromRow<T> _rowCreator;
  private Map<String, Integer> _headerMap;

  // TODO: Modify the header and body of this constructor however you wish

  /**
   * @param reader
   * @param rowCreator
   */
  public Parser(Reader reader, CreatorFromRow<T> rowCreator) {
    this._reader = Objects.requireNonNull(reader);
    this._rowCreator = rowCreator;
    this._parsedContent = new ArrayList<>();
    this._headerMap = new HashMap<>();
  }

  /**
   * TODO: Modify this method to incorporate your design choices
   *
   * @throws IOException when buffer reader fails to read-in a line
   */
  public void parse() {
    String line;

    // "You might find it useful, in your CSV parser, to wrap the given
    // Reader object in a BufferedReader object"
    try (BufferedReader readInBuffer = new BufferedReader(_reader)) {
      while ((line = readInBuffer.readLine()) != null) {
        if (_headerMap.isEmpty()) {
          String[] header = regexSplitCSVRow.split(line);
          for (int i = 0; i < header.length; i++) {
            _headerMap.put(header[i], i);
          }
          continue;
        }
        String[] result = regexSplitCSVRow.split(line);
        List<String> lineToArr = Arrays.stream(result).toList();
        try {
          T row = this._rowCreator.create(lineToArr);
          _parsedContent.add(row);
        } catch (FactoryFailureException e) {
          /*
           * TODO: Handle exception
           *
           * If an error is encountered, consider carefully what to do
           * with it; remember that you don’t know what the developer
           * will be doing with your parser.
           */
          System.err.println(e.getMessage());
        }
      }
      readInBuffer.close();
    } catch (Exception e) {
      // TODO: handle exception
      System.err.println(e.getMessage());
    }
  }

  public List<T> get_parsedContent() {
    return Collections.unmodifiableList(_parsedContent);
  }

  public Map<String, Integer> getHeaderMap() {
    return Collections.unmodifiableMap(_headerMap);
  }
}
