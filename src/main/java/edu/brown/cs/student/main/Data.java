package edu.brown.cs.student.main;

import edu.brown.cs.student.main.parser.Parser;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Class representing the data that is loaded into the server. This class is used to store the
 * parsed CSV data.
 *
 * <p>The class has the Spring annotation @Component, which allows it to be autowired into other
 * classes. Auto-wiring is a feature of Spring that allows for automatic dependency injection. This
 * means that Spring will automatically provide an instance of this class to any other class that
 * requires it.
 */
@Component
public class Data {

  protected Parser<List<String>> parser;

  public Data() {
    this.parser = null;
  }

  /**
   * Retrieves the parsed CSV data.
   *
   * @return Parser containing parsed data or null if not set.
   */
  public Parser<List<String>> getParser() {
    return this.parser;
  }

  /**
   * Sets the parsed CSV data.
   *
   * @param parser Parser containing the parsed data.
   */
  public void setParser(Parser<List<String>> parser) {
    this.parser = Objects.requireNonNull(parser, "Parser cannot be null");
  }
}
