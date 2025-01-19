package edu.brown.cs.student.main.controllers.LoadControllerTest;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.SpringBootServerApplication;
import edu.brown.cs.student.main.responses.CSVParseSuccess;
import edu.brown.cs.student.main.responses.ErrorBadRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(
    classes = SpringBootServerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadControllerIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String getBaseUrl() {
    return "http://localhost:" + port;
  }

  @Test
  public void testLoadCSVSuccess() {
    String url =
        getBaseUrl() + "/api/loadcsv?filepath=/census/dol_ri_earnings_disparity.csv&hasHeader=true";

    ResponseEntity<CSVParseSuccess> response =
        restTemplate.getForEntity(url, CSVParseSuccess.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("success", response.getBody().response_type());
    assertEquals("/census/dol_ri_earnings_disparity.csv", response.getBody().filepath());
  }

  @Test
  public void testLoadCSVMissingParameters() {
    // Missing 'filepath' parameter
    String urlMissingFilepath = getBaseUrl() + "/api/loadcsv?hasHeader=true";

    ResponseEntity<ErrorBadRequest> responseMissingFilepath =
        restTemplate.getForEntity(urlMissingFilepath, ErrorBadRequest.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseMissingFilepath.getStatusCode());
    assertNotNull(responseMissingFilepath.getBody());
    assertTrue(
        responseMissingFilepath
            .getBody()
            .message()
            .contains("Missing required query parameter: filepath"));

    // Missing 'hasHeader' parameter
    String urlMissingHasHeader =
        getBaseUrl() + "/api/loadcsv?filepath=data/census/dol_ri_earnings_disparity.csv";

    ResponseEntity<ErrorBadRequest> responseMissingHasHeader =
        restTemplate.getForEntity(urlMissingHasHeader, ErrorBadRequest.class);

    assertEquals(HttpStatus.BAD_REQUEST, responseMissingHasHeader.getStatusCode());
    assertNotNull(responseMissingHasHeader.getBody());
    assertTrue(
        responseMissingHasHeader
            .getBody()
            .message()
            .contains("Missing required query parameter: hasHeader"));
  }

  @Test
  public void testLoadCSVInvalidFilepath() {
    String url = getBaseUrl() + "/api/loadcsv?filepath=invalid/path.csv&hasHeader=true";

    ResponseEntity<ErrorBadRequest> response =
        restTemplate.getForEntity(url, ErrorBadRequest.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Could not find a csv file at invalid/path.csv", response.getBody().message());
  }
}
