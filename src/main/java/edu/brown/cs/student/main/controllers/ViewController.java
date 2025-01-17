package edu.brown.cs.student.main.controllers;

import edu.brown.cs.student.main.APIUtilities;
import edu.brown.cs.student.main.Data;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewController {
  private final Data _data;

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
