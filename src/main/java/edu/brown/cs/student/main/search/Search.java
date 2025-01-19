package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.interfaces.CreatorInterfaces.TrivialCreator;
import edu.brown.cs.student.main.parser.Parser;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {

  private Parser<List<String>> _parser;
  private FileReader _fileReader;
  private List<List<String>> _parsedContent;
  private Map<String, Integer> _headerMap;

  public Search(String filepath) {
    try {
      _fileReader = new FileReader(new File(filepath));
    } catch (Exception e) {
      System.out.println("File not found");
    }
    _parser = new Parser<List<String>>(_fileReader, new TrivialCreator());
    _parser.parse();
    _parsedContent = _parser.get_parsedContent();
    _headerMap = _parser.getHeaderMap();
  }

  public Search(Parser<List<String>> parser) {
    _parser = parser;
    _parsedContent = _parser.get_parsedContent();
    _headerMap = _parser.getHeaderMap();
  }

  public List search(String value) {
    List ret = new ArrayList();
    _parser
        .get_parsedContent()
        .forEach(
            row ->
                row.forEach(
                    cell -> {
                      if (cell.equals(value)) {
                        ret.add(row);
                      }
                    }));
    System.out.println(ret.toString());
    return ret;
  }

  public List search(String value, String column) {
    System.out.println("Searching for " + value + " in column " + column);
    if (column.equals("-1")) {
      search(value);
      return null;
    } else {
      Integer columnNum = null;
      List ret = new ArrayList();
      try {
        columnNum = Integer.parseInt(column);
      } catch (Exception e) {
        // TODO: handle exception
        System.err.println(column + " is not a valid column number");
      }

      if (columnNum == null) {
        for (int i = 0; i < _parsedContent.size(); i++) {
          if (_parsedContent.get(i).get(_headerMap.get(column)).equals(value)) {
            ret.add(_parsedContent.get(i));
          }
        }
      } else {
        for (int i = 0; i < _parsedContent.size(); i++) {
          if (_parsedContent.get(i).get(columnNum).equals(value)) {
            ret.add(_parsedContent.get(i));
          }
        }
      }
      System.out.println(ret.toString());
      return ret;
    }
  }
}
