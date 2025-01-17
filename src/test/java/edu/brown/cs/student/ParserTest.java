// package edu.brown.cs.student;

// import static org.junit.jupiter.api.Assertions.*;

// import edu.brown.cs.student.main.Parser.Parser;
// import edu.brown.cs.student.main.interfaces.CreatorInterfaces.TrivialCreator;
// import java.io.*;
// import java.util.List;
// import org.junit.jupiter.api.Test;

// /**
//  * TODO: add more tests in this file to build an extensive test suite for your parser and parsing
//  * functionalities
//  *
//  * <p>Tests for the parser class
//  */
// public class ParserTest {
//   Parser incomeByRaceParser;
//   Parser malformedParser;

//   // test parsing uniformed CSV
//   @Test
//   public void testParseRegCSV() throws IOException {
//     Parser<List<String>> incomeByRaceParser;

//     FileReader fileReader = new FileReader("data/census/income_by_race.csv");
//     TrivialCreator trivialCreator = new TrivialCreator();
//     incomeByRaceParser = new Parser(fileReader, trivialCreator);
//     incomeByRaceParser.parse();
//     List<List<String>> parsedContent = incomeByRaceParser.get_parsedContent();

//     assertEquals(324, parsedContent.size());
//     // assertEquals(9, incomeByRaceParser.parsedContent.get(223).size());
//     // assertEquals(9, incomeByRaceParser.parsedContent.get(0).size());
//     assertEquals(
//         List.of(
//             "7",
//             "Two Or More",
//             "2017",
//             "2017",
//             "44000",
//             "11831",
//             "\"Kent County, RI\"",
//             "05000US44003",
//             "kent-county-ri"),
//         parsedContent.get(143));
//     assertFalse(parsedContent.contains(List.of("Gemini", "Roberto", "Nick")));
//   }

//   // test parsing malformed data
//   @Test
//   public void testParseMalformedCSV() throws IOException {
//     FileReader fileReader = new FileReader("data/malformed/malformed_signs.csv");
//     TrivialCreator trivialCreator = new TrivialCreator();

//     malformedParser = new Parser(fileReader, trivialCreator);
//     malformedParser.parse();

//     List<List<String>> parsedContent = malformedParser.get_parsedContent();

//     System.out.println(parsedContent.toString());

//     assertEquals(13, parsedContent.size());
//     // assertEquals(2, malformedParser.parsedContent.get(0).size());
//     assertEquals(List.of("Aquarius"), parsedContent.get(11));
//     assertEquals(List.of("Gemini", "Roberto", "Nick"), parsedContent.get(3));
//   }

//   // Test parser for a file not found, example for exception testing
//   @Test
//   public void testFileNotFoundParse() throws IOException {
//     Exception exception =
//         assertThrows(
//             FileNotFoundException.class,
//             () -> new Parser(new FileReader("data/census/housing.csv"), new TrivialCreator()));
//   }
// }
