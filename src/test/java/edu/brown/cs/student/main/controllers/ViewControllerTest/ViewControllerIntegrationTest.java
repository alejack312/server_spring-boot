package edu.brown.cs.student.main.controllers.ViewControllerTest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.brown.cs.student.main.Data;
import edu.brown.cs.student.main.SpringBootServerApplication;
import edu.brown.cs.student.main.controllers.ViewController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = SpringBootServerApplication.class)
@AutoConfigureMockMvc
public class ViewControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ViewController viewController;

  @Autowired private Data data; // Ensure that the Data object is autowired

  @BeforeEach
  public void setUp() {
    // Reset the data object before each test
    // If Data has a reset method, use it. Otherwise, manually reset its fields.
    if (data != null) {
      data.setParser(null); // Example: Resetting the parser
      // Add more resets if Data holds more state
    }
  }

  @AfterEach
  public void tearDown() {
    // Reset the data object after each test to ensure no residual state
    if (data != null) {
      data.setParser(null); // Example: Resetting the parser
      // Add more resets if Data holds more state
    }
  }

  @Test
  public void testViewControllerIsAutowired() {
    assert (viewController != null);
  }

  @Test
  public void testViewEndpointSuccess() throws Exception {
    mockMvc.perform(
        get("/api/loadcsv")
            .param("filepath", "/census/dol_ri_earnings_disparity.csv")
            .param("hasHeader", "true"));

    mockMvc
        .perform(get("/api/viewcsv").param("id", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("success"))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data", hasSize(6)))
        .andExpect(jsonPath("$.data[0]", hasSize(6)))
        .andExpect(jsonPath("$.data[0][0]").value("RI"))
        .andExpect(jsonPath("$.data[0][1]").value("White"))
        .andExpect(jsonPath("$.data[0][2]").value("\" $1,058.47 \""))
        .andExpect(jsonPath("$.data[0][3]").value("395773.6521"))
        .andExpect(jsonPath("$.data[0][4]").value(" $1.00 "))
        .andExpect(jsonPath("$.data[0][5]").value("75%"));
  }

  @Test
  public void testViewCSVHandlerNoData() throws Exception {
    mockMvc
        .perform(get("/api/viewcsv"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("error_datasource"))
        .andExpect(jsonPath("$.message").value("There is no CSV file to be viewed"));
  }
}
