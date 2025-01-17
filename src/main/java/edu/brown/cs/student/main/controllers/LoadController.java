package edu.brown.cs.student.main.controllers;

import edu.brown.cs.student.main.Data;
import edu.brown.cs.student.main.interfaces.CreatorInterfaces.TrivialCreator;
import edu.brown.cs.student.main.parser.Parser;
import edu.brown.cs.student.main.responses.CSVParseSuccess;

import java.io.FileReader;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is a controller that handles the loading of a CSV file into the server.
 * 
 * The class is annotated with @RestController, which tells Spring that this class is a
 * controller that handles HTTP requests. The class is also annotated with @Validated,
 * which tells Spring to validate the input parameters of the methods in this class.
 * 
 * The class has a constructor that takes a Data object as a parameter. This Data object
 * is autowired by Spring, which means that Spring will automatically provide an instance
 * of the Data class to this class when it is created. This is an example of dependency
 * injection, which is a design pattern that allows for the separation of concerns in a
 * program.
 */
@RestController
@Validated
public class LoadController {
  private final Data _data;

  /**
   * Constructor for LoadController.
   * 
   * @param data: Data object that stores the parsed CSV data.
   * 
   * The data object contains the parsed CSV data that is loaded into the server.
   */
  @Autowired
  public LoadController(Data data) {
    this._data = data;
  }

  /**
   * This method handles the loading of a CSV file into the server.
   * 
   * The method is annotated with @GetMapping, which tells Spring that this method
   * should be called when an HTTP GET request is sent to the specified endpoint.
   * The method takes two parameters: filepathString and hasHeader. The @RequestParam
   * annotation tells Spring to bind the value of the query parameter with the name
   * "filepath" to the filepathString parameter, and the value of the query parameter
   * with the name "hasHeader" to the hasHeader parameter. The @NotBlank annotation
   * tells Spring to validate that the parameters are not blank.
   * 
   * @param filepathString
   * @param hasHeader
   * @return
   */
  @GetMapping("/api/loadcsv")
  public String loadCSV(
      @RequestParam("filepath") @NotBlank(message = "Filepath is required") String filepathString,
      @RequestParam("hasHeader") @NotBlank(message = "hasHeader parameter is required")
          String hasHeader) {

    // Parse the CSV file
    try {
      String fullPath = System.getProperty("user.dir") + "/data/" + filepathString;
      System.out.println("Full path: " + fullPath);
      FileReader fileReader = new FileReader(fullPath);
      TrivialCreator creator = new TrivialCreator();

      Parser<List<String>> parser = new Parser(fileReader, creator);
      parser.parse();
      // Save the data from the parsed CSV file
      this._data.setParser(parser);

      return new CSVParseSuccess(filepathString).serialize();
    } catch (Exception e) {
      return "Could not find a csv file at " + filepathString;
    }
  }
}
