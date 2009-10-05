package nl.BobbinWork.testutils.parameterized;

class TemplateClassUnderTest
{
  private double p;
  private double q;

  TemplateClassUnderTest(final Double a, final Double b)
  {
    p = Math.pow( a, b );
    q = a * b;
  }

  public double getP()
  {
    return p;
  }

  public double getQ()
  {
    return q;
  }

}
