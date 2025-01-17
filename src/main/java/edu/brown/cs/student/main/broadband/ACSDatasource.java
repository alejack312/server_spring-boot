package edu.brown.cs.student.main.broadband;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.APIUtilities;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import okio.Buffer;

/** Class to handle requests for broadband data. */
public class ACSDatasource {
  private static final String API_K = "5df6e25503f43779c0f4acebaebe097354155a37";
  private static Moshi moshi = new Moshi.Builder().build();
  private static final Type listListString =
      Types.newParameterizedType(List.class, List.class, String.class);
  private static JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

  /**
   * Handles a request for broadband data without caching
   *
   * @param state the state name
   * @param county the county name
   * @return the response which is the county's broadband data
   * @throws Exception if there is an error
   */
  public static List<List<String>> getBroadbandData(String state, String county) throws Exception {
    String stateCode = "";
    String countyCode = "";

    // Configure the connection (but don't actually send the request yet)
    URL requestStateURL =
        new URL("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*&key=" + API_K);
    HttpURLConnection stateRequest = APIUtilities.connect(requestStateURL);

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    // clientConnection.setRequestMethod("GET");

    stateRequest.connect();

    List<List<String>> stateList =
        adapter.fromJson(new Buffer().readFrom(stateRequest.getInputStream()));
    stateRequest.disconnect();

    for (List<String> s : stateList) {
      if (s.get(0).equals(state)) {
        stateCode = s.get(1);
      }
    }

    // Configure the connection (but don't actually send the request yet)
    URL requestCountyURL =
        new URL(
            "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                + stateCode
                + "&key="
                + API_K);
    HttpURLConnection countyRequest = APIUtilities.connect(requestCountyURL);

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    // clientConnection.setRequestMethod("GET");

    countyRequest.connect();

    List<List<String>> countyList =
        adapter.fromJson(new Buffer().readFrom(countyRequest.getInputStream()));
    countyRequest.disconnect();
    for (List<String> c : countyList) {
      if (c.get(0).split(" County,")[0].equals(county)) {
        countyCode = c.get(2);
      }
    }

    URL requestBroadbandURL =
        new URL(
            "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_001E&for=county:"
                + countyCode
                + "&in=state:"
                + stateCode
                + "&key="
                + API_K);
    System.out.println(requestBroadbandURL);
    HttpURLConnection broadbandRequest = APIUtilities.connect(requestBroadbandURL);
    broadbandRequest.connect();

    List<List<String>> dataList =
        adapter.fromJson(new Buffer().readFrom(broadbandRequest.getInputStream()));
    broadbandRequest.disconnect();

    return dataList;
  }

  /**
   * Handles a request for broadband data with caching
   *
   * @param state the state name
   * @param county the county name
   * @return the response which is the county's broadband data
   * @throws Exception if there is an error
   */
  public static List<List<String>> getBroadbandDataCache(
      String state, String county, BroadbandCache cache) throws Exception {
    String stateCode = cache.getState(state);
    String countyCode = cache.getCounty(county);
    String broadband = cache.getBroadband(countyCode + stateCode);

    return List.of(
        List.of(
            "NAME",
            "S2802_C03_001E",
            "state",
            "county",
            county
                + " County, "
                + state
                + ", "
                + broadband
                + ", "
                + stateCode
                + ", "
                + countyCode));
  }
}
