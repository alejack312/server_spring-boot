package edu.brown.cs.student;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.brown.cs.student.main.SpringBootFirebaseApplication;
import edu.brown.cs.student.main.broadband.BroadbandCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = SpringBootFirebaseApplication.class)
@AutoConfigureMockMvc
public class BroadbandControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BroadbandCache cache;

  @Test
  public void testBroadbandEndpoint() throws Exception {
    mockMvc
        .perform(
            get("/api/broadband")
                .param("state", "California")
                .param("county", "Los Angeles")
                .param("caching", "true"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"));
  }
}
