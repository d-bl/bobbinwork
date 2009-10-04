package nl.BobbinWork.testutils.parameterized;

import static nl.BobbinWork.testutils.parameterized.TestRunParametersBuilder.run;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * A usage example including a demonstration of error and failure reports.
 * 
 * @author Joke Pol
 * 
 */
public class Template
    extends ParameterizedBaseTest
{

  private static final Object[]                    NULL           = (Object[]) null;
  private static final Class<NullPointerException> NPE            = NullPointerException.class;
  private static final Double[]                    ZERO_TOLERANCE = new Double[] {};
  private static final Double[]                    SOME_TOLERANCE = new Double[] {
                                                                    0.1d
                                                                  };

  /**
   * Used by the {@link Parameterized} runner to create a test object
   * 
   * @param parameters
   *          the parameters needed to execute and assert the test
   */
  public Template(final TestRunParameters parameters)
  {
    super( parameters );
  }

  @Override
  protected Object[] produceActualResults(
      final Object[] input)
  {
    final Double a = (Double) input[0];
    final Double b = (Double) input[1];
    return new Object[] {
        Math.pow( a, b ), a * b
    };
  }

  @Parameters
  public static Collection<Object[]> createParameters()
  {

    final Object[][] resultValue = new Object[][] {
        run( "1" ).expectsArrayIndexOutOfBounds().withInput(),
        run( "2" ).expects( 1d, 10d ).withInput( 1d, 10d ),
        run( "3" ).expects( 32d, 10d ).withInput( 2d, 5d ),
        run( "4a" ).expectsNullPointer().withInput( null, null ),
        run( "4b" ).expects( NPE ).withInput( null, null ),
        run( "5" ).expects( SOME_TOLERANCE, 2.5d, 11d ).withInput( 1.1d, 10d ),
        run( "6" ).expects( ZERO_TOLERANCE, 2.6d, 11d ).withInput( 1.1d, 10d ),

        /*
         * subsequent ones fail intentionally to demonstrate the reporting and
         * increase coverage of testing the test code
         */
        // TODO improve the failure reporting
        run( null ).expects().withInput(),
        run( "no exception expected at all" ).expects().withInput(),
        run( "no exception expected at all" ).expects( 1d, 1d ).withInput(
            1d / 0d, 1d ),
        run( "expected another exception class" ).expectsIllegalArgument()
            .withInput( null, null ),
        run( "expected another exception class" ).expects( 2.6d ).withInput(
            1.1d, 10d ),
        run( "expected another exception message" ).expects( Exception.class,
            "xxXX", "YYY" ).withInput( 1.1d, 1 ),
        run( "expected an exception" ).expects( Exception.class ).withInput(
            1.1d, 10d ),
        run( "Unexpected ArrayIndexOutOfBounds" ).expects().withInput(),
        run( "2" ).expects().withInput( 1d, 10d ),
        run( "no exception expected" ).expects( NULL ).withInput( NULL ),

        // the next one reports the error in the test code
        new Object[] {
          null
        },//

        /*
         * subsequent errors don't tell they can't create the Template instance
         * 
         * just: java.lang.reflect.Constructor.newInstance,
         * 
         * the order seems to influence the details of the
         * IllegalArgumentException
         */

        // IllegalArgumentException: argument type mismatch
        new Object[] {
          ""
        },

        // IllegalArgumentException: wrong number of arguments
        new Object[] {},

        // IllegalArgumentException
        new Object[] {
            null, null
        },

    /* initialization errors that prevent to run any test: */

    // Template.createParameters() must return a Collection of arrays.
    // (Object[])null,
    };
    return Arrays.asList( resultValue );
  }
}
