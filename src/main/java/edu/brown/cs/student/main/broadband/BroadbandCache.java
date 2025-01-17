package edu.brown.cs.student.main.broadband;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.APIUtilities;
import edu.brown.cs.student.main.cache.LRUCache;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import okio.Buffer;
import org.springframework.stereotype.Component;

@Component
public class BroadbandCache {
  private static final String API_K = "5df6e25503f43779c0f4acebaebe097354155a37";
  private static Moshi moshi = new Moshi.Builder().build();
  private static final Type listListString =
      Types.newParameterizedType(List.class, List.class, String.class);
  private static JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

  private LRUCache<String, String> cache;
  private String stateCode;
  private String countyCode;

  public BroadbandCache() {
    this.cache = new LRUCache<>(10);
  }

  public void put(String key, String value) {
    cache.put(key, value);
  }

  public String getState(String key) {
    String value = cache.get(key);
    if (value == null) {
      try {
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
          if (s.get(0).equals(key)) {
            value = s.get(1);
          }
        }

        put(key, value);
        setStateCode(value);
        return value;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    setStateCode(value);
    return value;
  }

  public String getCounty(String key) {
    String value = cache.get(key);
    if (value == null) {
      try {
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
          if (c.get(0).split(" County,")[0].equals(key)) {
            value = c.get(2);
          }
        }
        put(key, value);
        setCountyCode(value);
        return value;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    setCountyCode(value);
    return value;
  }

  public String getBroadband(String key) {
    String value = cache.get(key);
    if (value == null) {
      try {
        // Configure the connection (but don't actually send the request yet)
        System.out.println(countyCode);
        System.out.println(stateCode);
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
        System.out.println(dataList.toString());

        value = dataList.get(1).get(1);

        put(key, value);
        return value;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return value;
  }

  private void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  private void setCountyCode(String countyCode) {
    System.out.println("Setting county code to " + countyCode);
    this.countyCode = countyCode;
  }
}
