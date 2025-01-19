package edu.brown.cs.student.main.controllers.BroadbandControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.SpringBootServerApplication;
import edu.brown.cs.student.main.broadband.BroadbandCache;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/** Integration tests for the LoadController class. */
@SpringBootTest(
    classes = SpringBootServerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BroadbandControllerIntegrationTest {

  // Random port to avoid conflicts with other tests
  @LocalServerPort private int port;

  private JsonAdapter<Map<String, Object>> adapter;

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);

  @BeforeAll
  public static void setup_before_everything() {
    // Stop the server if it's already running
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);

    // Mock Object
    BroadbandCache cache = new BroadbandCache();
  }

  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + port + "/" + apiCall);
    System.out.println(requestURL);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    // clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testBroadbandCachingSuccess() throws IOException {
    // Load a valid CSV file
    HttpURLConnection broadbandConnectionOne =
        tryRequest("api/broadband?state=Florida&county=Broward&caching=true");
    assertEquals(200, broadbandConnectionOne.getResponseCode());
    Map<String, Object> body =
        adapter.fromJson(new Buffer().readFrom(broadbandConnectionOne.getInputStream()));
    assertEquals("Florida", body.get("state"));
    assertEquals("Broward", body.get("county"));
    assertNotNull(body.get("data"));

    // Test basic Broadband cases
    HttpURLConnection broadbandConnectionTwo =
        tryRequest("api/broadband?state=Florida&county=Broward&caching=true");
    assertEquals(200, broadbandConnectionTwo.getResponseCode());
    body = adapter.fromJson(new Buffer().readFrom(broadbandConnectionTwo.getInputStream()));
    assertEquals("Florida", body.get("state"));
    assertEquals("Broward", body.get("county"));
    assertNotNull(body.get("data"));
  }
}
