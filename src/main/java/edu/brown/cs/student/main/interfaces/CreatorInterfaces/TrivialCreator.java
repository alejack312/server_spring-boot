package edu.brown.cs.student.main.interfaces.CreatorInterfaces;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.interfaces.CreatorFromRow;
import java.util.List;

public class TrivialCreator implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    if (row.size() == 0) {
      throw new FactoryFailureException("Row is empty", row);
    }
    return row;
  }
}
