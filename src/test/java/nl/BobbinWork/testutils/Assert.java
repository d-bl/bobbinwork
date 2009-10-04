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
   * @param messageExtension
   *          will be attached to a detailed description of an inequality
   * @param expected
   * @param actual
   * @param deltas
   *          each nun-null element causes the corresponding elements to be
   *          compared with the specified tolerance. Elements beyond the length
   *          of deltas are compared without a tolerance.
   */
  public static void assertArraysEqualsTolerant(
      String messageExtension,
      Object[] expected,
      Object[] actual,
      Double[] deltas)
  {
    assertEquals( "expected another number of result fields" + NEW_LINE
        + messageExtension, expected.length, actual.length );

    for (int i = 0; i < expected.length; i++) {
      final String msg = "expected[" + i + "] != actual[" + i + "]" + NEW_LINE
          + messageExtension;
      if (deltas == null || i >= deltas.length || deltas[i] == null) {
        assertEquals( msg, expected[i], actual[i] );
      } else {
        assertEquals( msg, expected[i].getClass(), actual[i].getClass() );
        assertEquals( msg, (Double) expected[i], (Double) actual[i], deltas[i] );
      }
    }
  }

  /**
   * Asserts that all the patterns are found in the value.
   * 
   * @param messageExtension
   * @param value
   * @param patterns
   *          regular expressions
   */
  public static void assertContains(
      String messageExtension,
      String value,
      String[] patterns)
  {
    for (final String pattern : patterns) {
      assertTrue( pattern + " not found" + NEW_LINE + messageExtension, value
          .toString().matches( ".*" + pattern + ".*" ) );
    }
  }

  /**
   * @param message
   *          an identifying message describing the context of the problem
   * @param expected
   *          the class or a superclass of the expected exception
   * @param actual
   *          the exception that was actually thrown
   */
  public static void assertSubClass(
      String message,
      Class<? extends Throwable> expected,
      Throwable actual)
  {
    try {
      actual.getClass().asSubclass( expected );
    } catch (Exception ClassCastException) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append( "got: " + actual.getClass().getName() + NEW_LINE );
      if (message != null) stringBuffer.append( message + NEW_LINE );
      fail( stringBuffer.toString() );
    }
  }

  /**
   * Fails with a concatenation of the specified message,
   * the message of the cause and the stack trace of the cause.
   * 
   * @param message
   *          a message describing the details of the problem
   * @param cause
   *          the exception that was thrown but not expected.
   */
  public static void fail(
      String message,
      Throwable cause)
  {

    final StringWriter stringWriter = new StringWriter();
    final PrintWriter printWriter = new PrintWriter( stringWriter );

    if (message != null) printWriter.append( message + NEW_LINE );
    String string = cause.getMessage();
    if (string != null) printWriter.append( "----"+string + "----"+ NEW_LINE );
    cause.printStackTrace( printWriter );

    printWriter.flush();
    stringWriter.flush();

    fail( stringWriter.toString() );
  }
}
