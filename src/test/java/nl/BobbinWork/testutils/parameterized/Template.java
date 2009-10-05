package nl.BobbinWork.testutils.parameterized;

import static nl.BobbinWork.testutils.parameterized.TestRunParametersBuilder.run;

import java.util.*;

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
    TemplateClassUnderTest x = new TemplateClassUnderTest( (Double) input[0],
        (Double) input[1] );
    return new Object[] {
        x.getP(), x.getQ()
    };
  }

  @Parameters
  public static Collection<TestRunParameters[]> createParameters()
  {
    final TestRunParameters[][] resultValue = new TestRunParameters[][] {
        run( "1" ).expectsArrayIndexOutOfBounds().withInput(),
        run( "2" ).expects( 1d, 10d ).withInput( 1d, 10d ),
        run( "3" ).expects( 32d, 10d ).withInput( 2d, 5d ),
        run( "4a" ).expectsNullPointer().withInput( null, null ),
        run( "4b" ).expects( NPE ).withInput( null, null ),
        run( "5a" ).expects( SOME_TOLERANCE, 2.5d, 11d ).withInput( 1.1d, 10d ),
        run( "5b" ).expects( ZERO_TOLERANCE, 2.5937424601000023d, 11d )
            .withInput( 1.1d, 10d ),

        /*
         * subsequent ones fail intentionally to demonstrate the reporting and
         * increase coverage of testing the test code
         */
        // TODO improve the failure reporting
        run( "5c" ).expects( ZERO_TOLERANCE, 2.59d, 11d ).withInput(
            1.1d, 10d ),
        run( "5d" ).expects( ZERO_TOLERANCE, 2.3d, 11d ).withInput( 1.1d, 10d ),
        run( "5e" ).expects( SOME_TOLERANCE, 2.7d, 11d ).withInput( 1.1d, 10d ),
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

    // an initialization error that prevent to run any test:
    // Template.createParameters() must return a Collection of arrays.
    // (TestRunParameters[])null,
    };
    return Arrays.asList( resultValue );
  }
}
