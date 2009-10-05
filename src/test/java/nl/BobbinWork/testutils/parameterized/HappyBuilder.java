package nl.BobbinWork.testutils.parameterized;

public class HappyBuilder
    implements PhraseBuilder
{

  private final String   description;
  private final Object[] expectedResult;
  private final Double[] deltas;

  HappyBuilder(
      final String description,
      final Object[] expectedResult,
      final Double[] deltas)
  {
    this.description = description;
    this.expectedResult = expectedResult;
    this.deltas = deltas;
  }

  @Override
  public TestRunParameters[] withInput(
      final Object... inputParamaters)
  {
    return new TestRunParameters[] {
      new HappyTestRunParameters( description, inputParamaters, expectedResult,
          deltas )
    };
  }
}
