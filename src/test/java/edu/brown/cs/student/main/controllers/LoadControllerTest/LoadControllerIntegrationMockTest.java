package edu.brown.cs.student.main.controllers.LoadControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.brown.cs.student.main.SpringBootServerApplication;
import edu.brown.cs.student.main.controllers.LoadController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = SpringBootServerApplication.class)
@AutoConfigureMockMvc
public class LoadControllerIntegrationMockTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private LoadController loadController;

  @Test
  public void testLoadControllerIsAutowired() {
    assert (loadController != null);
  }

  @Test
  public void testLoadCSVSuccess() throws Exception {
    mockMvc
        .perform(
            get("/api/loadcsv")
                .param("filepath", "/census/dol_ri_earnings_disparity.csv")
                .param("hasHeader", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("success"))
        .andExpect(jsonPath("$.filepath").value("/census/dol_ri_earnings_disparity.csv"));
  }

  @Test
  public void testLoadCSVMissingParameters() throws Exception {
    // Missing 'filepath' parameter
    mockMvc
        .perform(get("/api/loadcsv").param("hasHeader", "true"))
        .andExpect(status().isBadRequest());

    // Missing 'hasHeader' parameter
    mockMvc
        .perform(get("/api/loadcsv").param("filepath", "data/census/dol_ri_earnings_disparity.csv"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testLoadCSVInvalidFilepath() throws Exception {
    mockMvc
        .perform(
            get("/api/loadcsv").param("filepath", "invalid/path.csv").param("hasHeader", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.response_type").value("error_bad_request"))
        .andExpect(jsonPath("$.message").value("Could not find a csv file at invalid/path.csv"));
  }

  @Test
  public void testLoadCSVNoFilepath() throws Exception {
    mockMvc
        .perform(get("/api/loadcsv").param("hasHeader", "true"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.response_type").value("error_bad_request"))
        .andExpect(jsonPath("$.message").value("Missing required query parameter: filepath"));
  }
}
