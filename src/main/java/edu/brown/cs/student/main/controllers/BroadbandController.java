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

@RestController
public class BroadbandController {
  // Cache Object
  private BroadbandCache cache;

  /**
   * Constructor for the SearchCSVHandler.
   *
   * @param data Data object
   */
  @Autowired
  public BroadbandController(BroadbandCache cache) {
    this.cache = new BroadbandCache();
  }

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
        responseMap.put(
            "broadbandData", ACSDatasource.getBroadbandDataCache(stateName, countyName, cache));
      } else {
        responseMap.put("reponse_type", "success");
        responseMap.put("date", date);
        responseMap.put("time", time);
        responseMap.put("state", stateName);
        responseMap.put("county", countyName);
        responseMap.put("data", ACSDatasource.getBroadbandData(stateName, countyName));
      }
    } catch (Exception e) {
      System.out.println(e.toString());
      responseMap.put("reponse_type", "error");
      responseMap.put("state", stateName);
      responseMap.put("county", countyName);
      responseMap.put("error_message", e.getMessage());
    }
    return APIUtilities.serializeMap(responseMap);
  }
}
