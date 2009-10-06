package nl.BobbinWork.testutils.parameterized;

class TemplateTested
{
  private double p;
  private double q;

  TemplateTested(final Double a, final Double b)
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
