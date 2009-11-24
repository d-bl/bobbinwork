package nl.BobbinWork.testutils.parameterized;

import org.junit.runners.Parameterized;

/**
 * Provides a word in the phrase that creates a {@link TestRunParameters} instance.
 * 
 * @author J. Pol
 *
 */
public interface PhraseBuilder
{
  /**
   * Creates an object array for the parameters method of a
   * {@link Parameterized} class.
   * 
   * @param inputParamaters
   *          input arguments for the test run
   * @return a TestRunParameters object wrapped in an object array
   */
  public abstract TestRunParameters[] withInput(
      Object... inputParamaters);
}