package nl.BobbinWork.testutils.parameterized;

import static java.util.Arrays.deepToString;
import static nl.BobbinWork.testutils.Assert.*;

import java.util.Arrays;

import org.junit.Ignore;

/**
 * Represents the test parameters fed to one test run of a parameterized test.
 * 
 * @author J. Pol
 */
@Ignore("This is a test tool, not a tester")
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
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append( super.toString() );
    stringBuffer.append( NEW_LINE );
    stringBuffer.append( "expected exception class: " );
    stringBuffer.append( exceptionClass );
    stringBuffer.append( " or a subclass" );
    if (messagePatterns != null && messagePatterns.length > 0) {
      stringBuffer.append( NEW_LINE );
      stringBuffer.append( "expected excpetion message contains all of: " );
      stringBuffer.append( Arrays.toString( messagePatterns ) );
    }
    return stringBuffer.toString();
  }

  @Override
  void assertResult(
      final Object[] actual)
  {
    fail( toString() + NEW_LINE + "got: " + deepToString( actual ) );
  }

  @Override
  void assertResult(
      final Exception actual)
  {
    String message = toString();
    assertSubClass( message, exceptionClass, actual );
    assertContains( message, messagePatterns, actual );
  }
}
