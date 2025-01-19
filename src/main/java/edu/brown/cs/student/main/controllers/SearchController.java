package edu.brown.cs.student.main.controllers;

import edu.brown.cs.student.main.APIUtilities;
import edu.brown.cs.student.main.Data;
import edu.brown.cs.student.main.search.Search;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is a controller that handles the searching of a CSV file.
 *
 * The class is annotated with @RestController, which tells Spring that this class is a
 * controller that handles HTTP requests.
 */
@RestController
public class SearchController {
  private final Data _data;

  /**
   * Constructor for SearchController.
   *
   * @param data: Data object that stores the parsed CSV data.
   *     <p>The constructor takes a Data object as a parameter. This Data object is autowired by
   *     Spring, which means that Spring will automatically provide an instance of the Data class to
   *     this class when it is created. This is an example of dependency injection, which is a
   *     design pattern that allows for the separation of concerns in a program.
   *     <p>The data object contains the parsed CSV data that is loaded into the server.
   */
  @Autowired
  public SearchController(Data data) {
    this._data = data;
  }

  @GetMapping("/api/searchcsv")
  public String searchCSV(
      @RequestParam(value = "value") String value,
      @RequestParam(value = "columnId", required = false) String columnId) {

    // Parse the CSV file
    try {
      // Initialize the columnLabel

      // Create the searcher with that information
      Search searcher = new Search(_data.getParser());
      List<List<String>> rows = new ArrayList<>();

      // Get the returned row numbers

      if (columnId != null && !columnId.isEmpty()) {
        rows = searcher.search(value, columnId);
      } else {
        rows = searcher.search(value);
      }

      // Turn those row numbers into a list of list of strings and return it in a
      // ViewDataSuccess

      // Prepare to send a reply
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("result", "success");
      responseMap.put("data", rows);
      System.out.println("Rows: " + rows);

      return APIUtilities.serializeMap(responseMap);
    } catch (Exception e) {
      return "Could not find the term in the CSV file";
    }
  }
}
