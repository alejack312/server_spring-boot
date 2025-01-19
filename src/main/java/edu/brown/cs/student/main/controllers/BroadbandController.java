package edu.brown.cs.student.main.controllers;

import edu.brown.cs.student.main.APIUtilities;
import edu.brown.cs.student.main.broadband.ACSDatasource;
import edu.brown.cs.student.main.broadband.BroadbandCache;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is a controller that handles the searching of broadband data.
 * It pings the ACS API to get broadband data for a given state and county.
 */
@RestController
public class BroadbandController {
  // Cache Object
  private BroadbandCache cache;

  /**
   * Constructor for BroadbandController.
   *
   * @param cache
   *     <p>The constructor takes a BroadbandCache object as a parameter. This BroadbandCache object
   *     is autowired by Spring, which means that Spring will automatically provide an instance of
   *     the BroadbandCache class to this class when it is created. This is an example of dependency
   *     injection, which is a design pattern that allows for the separation of concerns in a
   *     program.
   *     <p>The cache object contains the cached broadband data that is loaded into the server.
   */
  @Autowired
  public BroadbandController(BroadbandCache cache) {
    this.cache = new BroadbandCache();
  }

  /**
   * This method handles the searching of broadband data.
   *
   * @param stateName
   * @param countyName
   * @param caching
   * @return
   */
  @GetMapping("/api/broadband")
  public String broadband(
      @RequestParam("state") @NotBlank(message = "Filepath is required") String stateName,
      @RequestParam("county") @NotBlank(message = "hasHeader parameter is required")
          String countyName,
      @RequestParam("caching") @NotBlank(message = "caching parameter is required")
          Boolean caching) {

    Instant currentTime = Instant.now();
    String date = currentTime.toString().split("T")[0];
    String time = currentTime.toString().split("T")[1];

    // Prepare to send a reply
    Map<String, Object> responseMap = new HashMap<>();

    try {
      if (caching) { // I may also wish to perform other configuration or omit the cache entirely
        responseMap.put("response_type", "success");
        responseMap.put("date", date);
        responseMap.put("time", time);
        responseMap.put("state", stateName);
        responseMap.put("county", countyName);
        responseMap.put("data", ACSDatasource.getBroadbandDataCache(stateName, countyName, cache));
      } else {
        responseMap.put("response_type", "success");
        responseMap.put("date", date);
        responseMap.put("time", time);
        responseMap.put("state", stateName);
        responseMap.put("county", countyName);
        responseMap.put("data", ACSDatasource.getBroadbandData(stateName, countyName));
      }
    } catch (Exception e) {
      System.out.println(e.toString());
      responseMap.put("response_type", "error");
      responseMap.put("state", stateName);
      responseMap.put("county", countyName);
      responseMap.put("error_message", e.getMessage());
    }
    return APIUtilities.serializeMap(responseMap);
  }
}
