package edu.brown.cs.student.main.controllers.LoadControllerTest;

import static org.junit.jupiter.api.Assertions.*;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.SpringBootServerApplication;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

/** Integration tests for the LoadController class. */
@SpringBootTest(
    classes = SpringBootServerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadControllerIntegrationTest {
  // Random port to avoid conflicts with other tests
  @LocalServerPort private int port;

  private JsonAdapter<Map<String, Object>> adapter;
  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);

  @BeforeEach
  public void setup() {
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);
  }

  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + port + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    // clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  /** Tests the loadcsv endpoint with a valid CSV file. */
  @Test
  public void testLoadCSVSuccess() throws IOException {
    // Load a valid CSV file
    // Test basic LoadCSV cases
    HttpURLConnection riEarningsConnection =
        tryRequest("api/loadcsv?filepath=census/dol_ri_earnings_disparity.csv&hasHeader=true");
    assertEquals(200, riEarningsConnection.getResponseCode());

    Map<String, Object> body =
        adapter.fromJson(new Buffer().readFrom(riEarningsConnection.getInputStream()));
    assertEquals("success", body.get("response_type"));
    assertEquals("census/dol_ri_earnings_disparity.csv", body.get("filepath"));
  }

  /** Tests the loadcsv endpoint with missing parameters. */
  @Test
  public void testLoadCSVMissingParameters() throws IOException {
    // Missing 'filepath' parameter
    HttpURLConnection urlMissingFilepath = tryRequest("api/loadcsv?hasHeader=true");
    assertEquals(200, urlMissingFilepath.getResponseCode());
  }
}
