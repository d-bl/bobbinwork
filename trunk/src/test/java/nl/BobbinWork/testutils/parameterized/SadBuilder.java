package nl.BobbinWork.testutils.parameterized;

public class SadBuilder
    implements PhraseBuilder
{

  private final String                     description;
  private final Class<? extends Exception> exceptionClass;
  private final String[]                   messagePatterns;

  SadBuilder(
      String description,
      Class<? extends Exception> exceptionClass,
      String[] messagePatterns)
  {
    this.description = description;
    this.exceptionClass = exceptionClass;
    this.messagePatterns = messagePatterns;
  }

  @Override
  public TestRunParameters[] withInput(
      Object... inputParamaters)
  {
    return new TestRunParameters[] {
      new SadTestRunParameters( description, inputParamaters, exceptionClass,
          messagePatterns )
    };
  }
}