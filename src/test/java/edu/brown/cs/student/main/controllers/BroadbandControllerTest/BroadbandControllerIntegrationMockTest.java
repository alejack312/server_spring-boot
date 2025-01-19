package edu.brown.cs.student.main.controllers.BroadbandControllerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.brown.cs.student.main.SpringBootServerApplication;
import edu.brown.cs.student.main.broadband.BroadbandCache;
import edu.brown.cs.student.main.controllers.BroadbandController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = SpringBootServerApplication.class)
@AutoConfigureMockMvc
public class BroadbandControllerIntegrationMockTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private BroadbandController broadbandController;

  // Ensure that the broadbandCache object is autowired
  @Autowired private BroadbandCache broadbandCache;

  @BeforeEach
  public void setUp() {
    // Reset the data object before each test
    // If Data has a reset method, use it. Otherwise, manually reset its fields.
    if (broadbandCache != null) {
      broadbandCache = null; // Example: Resetting the parser
      // Add more resets if Data holds more state
    }
  }

  @AfterEach
  public void tearDown() {
    // Reset the data object after each test to ensure no residual state
    if (broadbandCache != null) {
      broadbandCache = null; // Example: Resetting the parser
      // Add more resets if Data holds more state
    }
  }

  @Test
  public void testBroadbandControllerIsAutowired() {
    assert (broadbandController != null);
  }

  @Test
  public void testBroadbandSuccessWithCaching() throws Exception {
    String state = "California";
    String county = "Los Angeles";
    boolean caching = true;

    mockMvc
        .perform(
            get("/api/broadband")
                .param("state", state)
                .param("county", county)
                .param("caching", String.valueOf(caching))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("success"))
        .andExpect(jsonPath("$.state").value(state))
        .andExpect(jsonPath("$.county").value(county))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  public void testBroadbandSuccessWithoutCaching() throws Exception {
    String state = "New York";
    String county = "Albany";
    boolean caching = false;

    mockMvc
        .perform(
            get("/api/broadband")
                .param("state", state)
                .param("county", county)
                .param("caching", String.valueOf(caching))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("success"))
        .andExpect(jsonPath("$.state").value(state))
        .andExpect(jsonPath("$.county").value(county))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  public void testBroadbandMissingParameters() throws Exception {
    // Missing 'state' parameter
    mockMvc
        .perform(
            get("/api/broadband")
                .param("county", "Miami-Dade")
                .param("caching", "true")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("error_bad_request"))
        .andExpect(jsonPath("$.message").value("Missing required query parameter: state"));

    // Missing 'county' parameter
    mockMvc
        .perform(
            get("/api/broadband")
                .param("state", "Florida")
                .param("caching", "true")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("error_bad_request"))
        .andExpect(jsonPath("$.message").value("Missing required query parameter: county"));

    // Missing 'caching' parameter
    mockMvc
        .perform(
            get("/api/broadband")
                .param("state", "Texas")
                .param("county", "Houston")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("error_bad_request"))
        .andExpect(jsonPath("$.message").value("Missing required query parameter: caching"));
  }
}

// // Mock BroadbandCache behavior
// when(broadbandCache.getBroadband(anyString())).thenReturn("100 Mbps");

// // Mock ACSDatasource static method
// List<List<String>> mockData = List.of(List.of("NAME", "S2802_C03_001E",
// "value"));
// mockedACSDatasource
// .when(() -> ACSDatasource.getBroadbandDataCache(state, county,
// broadbandCache))
// .thenReturn(mockData);

// mockMvc
// .perform(
// get("/api/broadband")
// .param("state", state)
// .param("county", county)
// .param("caching", String.valueOf(caching))
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.response_type").value("success"))
// .andExpect(jsonPath("$.state").value(state))
// .andExpect(jsonPath("$.county").value(county))
// .andExpect(jsonPath("$.broadbandData").isArray());
// }

// @Test
// public void testBroadbandSuccessWithoutCaching() throws Exception {
// String state = "New York";
// String county = "Albany";
// boolean caching = false;

// // Mock ACSDatasource static method
// List<List<String>> mockData = List.of(List.of("NAME", "S2802_C03_001E", "50
// Mbps"));
// mockedACSDatasource
// .when(() -> ACSDatasource.getBroadbandData(state, county))
// .thenReturn(mockData);

// mockMvc
// .perform(
// get("/api/broadband")
// .param("state", state)
// .param("county", county)
// .param("caching", String.valueOf(caching))
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isOk())
// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.response_type").value("success"))
// .andExpect(jsonPath("$.state").value(state))
// .andExpect(jsonPath("$.county").value(county))
// .andExpect(jsonPath("$.broadbandData").isArray());
// }

// @Test
// public void testBroadbandMissingParameters() throws Exception {
// // Missing 'state' parameter
// mockMvc
// .perform(
// get("/api/broadband")
// .param("county", "Miami")
// .param("caching", "true")
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isBadRequest())
// .andExpect(jsonPath("$.response_type").value("error_bad_request"))
// .andExpect(jsonPath("$.message").value("Missing required query parameter:
// state"));

// // Missing 'county' parameter
// mockMvc
// .perform(
// get("/api/broadband")
// .param("state", "Florida")
// .param("caching", "true")
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isBadRequest())
// .andExpect(jsonPath("$.response_type").value("error_bad_request"))
// .andExpect(jsonPath("$.message").value("Missing required query parameter:
// county"));

// // Missing 'caching' parameter
// mockMvc
// .perform(
// get("/api/broadband")
// .param("state", "Texas")
// .param("county", "Houston")
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isBadRequest())
// .andExpect(jsonPath("$.response_type").value("error_bad_request"))
// .andExpect(jsonPath("$.message").value("Missing required query parameter:
// caching"));
// }

// @Test
// public void testBroadbandInvalidParameters() throws Exception {
// String state = "InvalidState";
// String county = "InvalidCounty";
// boolean caching = true;

// // Mock BroadBandCache to return null for invalid parameters
// when(broadbandCache.getBroadband(anyString())).thenReturn(null);

// // Mock ACSDatasource to throw an exception for invalid parameters
// mockedACSDatasource
// .when(() -> ACSDatasource.getBroadbandDataCache(state, county,
// broadbandCache))
// .thenThrow(new Exception("Invalid state or county"));

// mockMvc
// .perform(
// get("/api/broadband")
// .param("state", state)
// .param("county", county)
// .param("caching", String.valueOf(caching))
// .contentType(MediaType.APPLICATION_JSON))
// .andExpect(status().isInternalServerError())
// .andExpect(jsonPath("$.response_type").value("error"))
// .andExpect(jsonPath("$.state").value(state))
// .andExpect(jsonPath("$.county").value(county))
// .andExpect(jsonPath("$.error_message").value("Invalid state or county"));
// }
// }
