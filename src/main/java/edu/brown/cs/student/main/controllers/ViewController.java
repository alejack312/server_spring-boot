package edu.brown.cs.student.main.controllers;

import edu.brown.cs.student.main.APIUtilities;
import edu.brown.cs.student.main.Data;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is a controller that handles the viewing of a CSV file.
 *
 * The class is annotated with @RestController, which tells Spring that this class is a
 * controller that handles HTTP requests.
 */
@RestController
public class ViewController {
  private final Data _data;

  /**
   * Constructor for ViewController.
   *
   * @param data
   *     <p>The constructor takes a Data object as a parameter. This Data object is autowired by
   *     Spring, which means that Spring will automatically provide an instance of the Data class to
   *     this class when it is created. This is an example of dependency injection, which is a
   *     design pattern that allows for the separation of concerns in a program.
   *     <p>The data object contains the parsed CSV data that is loaded into the server.
   */
  @Autowired
  public ViewController(Data data) {
    this._data = data;
  }

  @GetMapping("/api/viewcsv")
  public String viewCSV() {
    if (this._data.getParser() == null) {
      System.out.println("There is no CSV file to be viewed");
      return "There is no CSV file to be viewed";
    }

    // From September 21st Livecode
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("response_type", "success");

    responseMap.put("data", this._data.getParser().get_parsedContent());
    System.out.println("Data: " + this._data.getParser().get_parsedContent());
    return APIUtilities.serializeMap(responseMap);
  }
}
