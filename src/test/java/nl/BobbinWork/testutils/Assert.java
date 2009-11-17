package nl.BobbinWork.testutils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Assert
    extends org.junit.Assert
{
  /*
   * a subclass allows overloading methods.
   * 
   * Less assertions in junit.framework.Assert.
   */
  static final String NEW_LINE = System.getProperty( "line.separator" );

  /**
   * @param identifyingMessage
   *          will be attached to a detailed description of an inequality
   * @param expected
   * @param actual
   * @param deltas
   *          each nun-null element causes the corresponding elements to be
   *          compared with the specified tolerance. Elements beyond the length
   *          of deltas are compared without a tolerance.
   */
  public static void assertArraysEqualsTolerant(
      final String identifyingMessage,
      final Object[] expected,
      final Object[] actual,
      final Double[] deltas)
  {
    assertEquals( identifyingMessage + "number of fields ", expected.length,
        actual.length );

    for (int i = 0; i < expected.length; i++) {
      final String msg =
          identifyingMessage + NEW_LINE + "fails at index " + i + "; ";
      if (deltas == null || i >= deltas.length || deltas[i] == null) {
        assertEquals( msg, expected[i], actual[i] );
      } else {
        assertEquals( msg, expected[i].getClass(), actual[i].getClass() );
        assertEquals( msg, (Double) expected[i], (Double) actual[i], deltas[i] );
      }
    }
  }

  public static void assertEquals(
      final String msg,
      final Object expected,
      final Object actual)
  {
    org.junit.Assert.assertEquals( msg, expected, actual );

    // safety net for JUnit 4.3.1. which comes with Eclipse
    final String note =
        NEW_LINE + "should have failed earlier, fixed in JUnit 4.5 or later"
            + NEW_LINE;
    junit.framework.Assert.assertEquals( msg + note, expected, actual );
  }

  /**
   * Asserts that essential information is present in the message of the
   * exception.
   * 
   * @param identifyingMessage
   * @param expected
   *          regular expressions that should all be found in the message of the
   *          thrown exception
   * @param actual
   *          the exception that is thrown
   */
  public static void assertContains(
      final String identifyingMessage,
      final String[] expected,
      final Throwable actual)
  {
    if (expected == null || expected.length == 0) return;
    assertTrue( identifyingMessage + NEW_LINE + "got no message at all", actual
        .getMessage() != null );
    
    // new lines break the matches method
    final String thrownMessage = actual.getMessage().replaceAll( "\\s+", " " );
    
    for (final String pattern : expected) {
      final String msg = identifyingMessage + NEW_LINE + //
          "expected: " + pattern + NEW_LINE + "got: " + actual;
      assertTrue( msg + pattern, thrownMessage.matches( ".*" + pattern + ".*" ) );
    }
  }

  /**
   * @param identifyingMessage
   *          an identifying message describing the context of the problem
   * @param expected
   *          the class or a superclass of the expected exception
   * @param actual
   *          the exception that was actually thrown
   */
  public static void assertSubClass(
      final String identifyingMessage,
      final Class<? extends Throwable> expected,
      final Throwable actual)
  {
    try {
      actual.getClass().asSubclass( expected );
    } catch (Exception ClassCastException) {
      final StringBuffer stringBuffer = new StringBuffer();
      if (identifyingMessage != null)
        stringBuffer.append( identifyingMessage + NEW_LINE );
      stringBuffer.append( "got: " + actual.getClass().getName() );
      fail( stringBuffer.toString() );
    }
  }

  /**
   * Fails with a concatenation of the specified message, the message of the
   * cause and the stack trace of the cause.
   * 
   * @param identifyingMessage
   *          a message describing the details of the problem
   * @param cause
   *          the exception that was thrown but not expected.
   */
  public static void fail(
      final String identifyingMessage,
      final Throwable cause)
  {

    final StringWriter stringWriter = new StringWriter();
    final PrintWriter printWriter = new PrintWriter( stringWriter );

    if (identifyingMessage != null)
      printWriter.append( identifyingMessage + NEW_LINE );
    printWriter.append( "got: " );
    cause.printStackTrace( printWriter );

    printWriter.flush();
    stringWriter.flush();
    fail( stringWriter.toString() );
  }
}
