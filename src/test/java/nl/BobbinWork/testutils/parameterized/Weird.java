package nl.BobbinWork.testutils.parameterized;

import org.junit.Test;

public class Weird
{
  @Test
  public void weird()
  {
    Object[] expected = new Object[]{(Double) 1.59d};
    Object[] actual = new Object[]{Math.pow( 1.1d, 10d )};
    // well... isolated not so weird, but run 5c in TemplateTester fails a line later
    nl.BobbinWork.testutils.Assert.assertArraysEqualsTolerant( "mine ", expected, actual,new Double[] {});
  }
}
