package nl.BobbinWork.testutils.parameterized;

import static nl.BobbinWork.testutils.Assert.*;

import java.util.Arrays;

import org.junit.runners.Parameterized;

/**
 * Represents the test parameters fed to one run of a {@link Parameterized}
 * test.
 * 
 * @author Joke Pol
 */
public class HappyTestRunParameters
    extends TestRunParameters
{

  private final Object[] expectedResult;
  private final Double[] deltas;

  public static class Builder
      implements TestRunParameters.Builder
  {

    private final String   description;
    private final Object[] expectedResult;
    private final Double[] deltas;

    Builder(
        final String description,
        final Object[] expectedResult,
        final Double[] deltas)
    {
      this.description = description;
      this.expectedResult = expectedResult;
      this.deltas = deltas;
    }

    @Override
    public Object[] withInput(
        final Object... inputParamaters)
    {
      return new Object[] {
        new HappyTestRunParameters( description, inputParamaters,
            expectedResult, deltas )
      };
    }
  }

  /**
   * Constructs a an object for a test run that does <i>not</i> throw an
   * exception.
   * 
   * @param description
   *          a description and/or unique tag of this test run
   * @param input
   *          the inputs of the test run
   * @param expectedResult
   *          the expected result of the test run
   * @param deltas
   *          each non null element triggers an assertEquals of doubles with a
   *          tolerance
   */
  HappyTestRunParameters(
      final String description,
      final Object[] input,
      final Object[] expectedResult,
      final Double... deltas)
  {
    super( description, input );
    this.expectedResult = expectedResult;
    this.deltas = deltas;
  }

  @Override
  public String toString()
  {
    final StringBuffer result = new StringBuffer();

    /* directly after the actual result for easy visual comparison */
    result.append( NEW_LINE + "expected:"
        + Arrays.deepToString( expectedResult ) );
    if (deltas != null && deltas.length > 0) {
      result.append( NEW_LINE + "tolerance: " + Arrays.deepToString( deltas ) );
    }
    result.append( super.toString() );
    return result.toString();
  }

  @Override
  void assertResult(
      Object[] actual)
  {
    assertArraysEqualsTolerant( toString(), expectedResult, actual, deltas );
  }

  @Override
  void assertResult(
      Exception exception)
  {
    fail( "no exception expected at all" + toString(), exception );
  }
}
