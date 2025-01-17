package edu.brown.cs.student.main.interfaces;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import java.util.List;

public interface CreatorFromRow<T> {
  T create(List<String> row) throws FactoryFailureException;
}
