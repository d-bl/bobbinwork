package nl.BobbinWork.testutils.parameterized;

import java.util.Arrays;

import static junit.framework.Assert.fail;
import static nl.BobbinWork.testutils.Assert.*;

/**
 * Represents the test parameters fed to one test run of a parameterized test.
 * 
 * @author Joke Pol
 */
public class SadTestRunParameters
    extends TestRunParameters
{

  private final String[]                   messagePatterns;
  private final Class<? extends Exception> exceptionClass;

  /**
   * Constructs a an object for a test run that does <i>not</i> throw an
   * exception.
   * 
   * @param description
   *          a description and/or unique tag of this test run
   * @param input
   *          the inputs of the test run
   * @param exceptionClass
   *          the exception expected to be thrown by the test run
   * @param messagePatterns
   *          regular expressions that should match a part of the message of the
   *          expected expression
   */
  SadTestRunParameters(
      final String description,
      final Object[] input,
      final Class<? extends Exception> exceptionClass,
      final String[] messagePatterns)
  {
    super( description, input );
    this.exceptionClass = exceptionClass;
    this.messagePatterns = messagePatterns;
  }

  @Override
  public String toString()
  {
    final StringBuffer result = new StringBuffer();
    String string = String.format(
        "expected exception class: %s or a subclass", exceptionClass );
    result.append( NEW_LINE + string );
    if (messagePatterns != null && messagePatterns.length > 0) {
      result.append( NEW_LINE + "expected excpetion message contains all of: "
          + Arrays.toString( messagePatterns ) );
    }
    result.append( super.toString() );
    return result.toString();
  }

  @Override
  void assertResult(
      final Object[] actual)
  {
    fail( "expected an exception but got: " + Arrays.deepToString( actual )
        + toString() );
  }

  @Override
  void assertResult(
      final Exception actual)
  {
    final String string = toString();
    assertSubClass( "expected another exception class" + string,
        exceptionClass, actual );
    assertContains( "expected another exception message" + string, //
        actual.toString(), messagePatterns );
  }
}
