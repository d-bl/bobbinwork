package nl.BobbinWork.testutils.parameterized;

import java.util.Arrays;

public abstract class TestRunParameters
{

  static final String    NEW_LINE = System.getProperty( "line.separator" );
  private final String   description;
  private final Object[] input;

  TestRunParameters(final String description, final Object[] input)
  {
    /* Not public: disables subclasses outside the package */

    this.description = description;
    this.input = input;
  }

  @Override
  public String toString()
  {
    final StringBuffer message = new StringBuffer();

    if (description != null) {
      message.append( NEW_LINE );
      message.append( "tag: " );
      message.append( description );
    }
    message.append( NEW_LINE );
    message.append( "input:" );
    message.append( Arrays.deepToString( getInput() ) );
    return message.toString();
  }

  abstract void assertResult(
      final Object[] actual);

  abstract void assertResult(
      final Exception exception);

  Object[] getInput()
  {
    return input;
  }
}
