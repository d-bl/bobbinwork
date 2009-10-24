package nl.BobbinWork.testutils.parameterized;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * A base class for parameterized test that have one or more inputs and one or
 * more expected results. The expected results may be a values or one exception.
 * Tests that use this class as a base must do two things:
 * <ul>
 * <li>override {@link #produceActualResults(HappyTestRunParameters)} with the
 * actual test that produces the expected result. This "test" must <b>not</b> be
 * annotated with the <tt>@Test</tt> annotation</li>
 * <li>provide a <tt>public static</tt> method with a <tt>@Parameters</tt>
 * annotation that provides the inputs and expected results for the various
 * cases you want to test.</li>
 * </ul>
 * 
 * @author Joke Pol
 */
@RunWith(Parameterized.class)
public abstract class ParameterizedFixture
{
  private final TestRunParameters parameters;

  /**
   * Constructs an instance of a test object. An instance is created by the
   * <tt>@Parameterized</tt> runner for each set of test parameters.
   * 
   * @param parameters
   *          the parameters (input and expected output) to use in this run of
   *          the test
   */
  protected ParameterizedFixture(final TestRunParameters parameters)
  {
    if (parameters == null)
      throw new IllegalArgumentException( "parameters should not be null" );
    /* the cast causes clearer error reporting than reflection */
    this.parameters = (TestRunParameters) parameters;
  }

  /**
   * The test that the runner will call. This method must not be called
   * otherwise.
   */
  @Test
  public final void doTest()
  {
    try {
      Object[] actualResults = produceActualResults( parameters.getInput() );
      parameters.assertResult( actualResults );
    } catch (final Exception exception) {
      parameters.assertResult( exception );
    }
  }

  /**
   * The actual test code that produces one or more actual results. If the
   * expected result is an exception it is of course not returned but thrown.
   * 
   * @param inputs
   *          The parameters for the tested method or constructor. The values
   *          originate from
   *          {@link TestRunParameters.Builder#withInput(Object...)}. Created by
   *          the method annotated with parameters they are passed on via the
   *          constructor of a test instance.
   * @return The actual result values that will be compared with
   *         {@link TestRunParametersBuilder#expects(Object...)} or
   *         {@link TestRunParametersBuilder#expects(Double[], Object...)}. If
   *         one of the objects does not override {@link Object#equals(Object)}
   *         the tests will all fail.
   * @throws Exception
   *           The actual result that will be compared with
   *           {@link TestRunParametersBuilder#expects(Class, String...)}.
   */
  protected abstract Object[] produceActualResults(
      final Object[] inputs) throws Exception;
}
