package nl.BobbinWork.diagram.math;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import junit.framework.TestCase;
import static nl.BobbinWork.diagram.math.Annotations.*;

public class testAnnotations extends TestCase {
	
	private String pointToString(Point2D p) {
		return "(" + p.getX() +","+p.getY()+ ")";
	}
	
	private String lineToString(Line2D line) {
		return pointToString(line.getP1()) + "-" + //
		       pointToString(line.getP2());
	}
	
	/**
	 * @param expected
	 * @param actual
	 * @param tolerance maximum distance between the expected and actual point
	 * @return true if the points are close enough, or all coordinates are NaN
	 */
	private boolean bothNaN (Point2D p){
		return !isANumber(p.getX()) && !isANumber(p.getY());
	}
	
	/**
	 * @param n
	 * @return true if n is not NaN
	 */
	private boolean isANumber (double n){
		return n>0 || n <=0;
	}
	
	private void assertEqualLines (Line2D expected, Line2D actual, double tolerance){
		
		assertTrue ("bad test: line shorter than tolerance*2:", //
				 ( 2 * tolerance <= expected.getP1().distance( expected.getP2() ) ) //
				 || equalPoints(expected.getP1(), expected.getP2(), 0)
				 );
		
		String message = "expected line " + lineToString(expected) //
        + " but got " + lineToString(actual);
		assertTrue ( message, equalLines(expected, actual, tolerance) 
                   );
 	}

	private boolean equalPoints (Point2D expected, Point2D actual, double tolerance){
		return ( expected.distance(actual) <= tolerance )  
			|| (bothNaN(expected) && bothNaN(actual));
	}

	private boolean equalLines(Line2D expected, Line2D actual, double tolerance) {
		return ( equalPoints(expected.getP1(), actual.getP1(), tolerance) &&
		                        equalPoints(expected.getP2(), actual.getP2(), tolerance)
		                      ) || 
		                      ( equalPoints(expected.getP1(), actual.getP2(), tolerance) &&
		                        equalPoints(expected.getP2(), actual.getP1(), tolerance)
		                      );
	}

	/**
	 * Test method for {@link nl.BobbinWork.diagram.math.Annotations#CreateTwistMark(java.awt.geom.CubicCurve2D, int)}.
	 */
	public void testCreateTwistMark() {

		// test the test methods

	    assertTrue  (equalPoints(new Point2D.Double(0,0), new Point2D.Double(0,0), 0));
	    assertFalse (equalPoints(new Point2D.Double(0,0), new Point2D.Double(1,1), 0));
	    assertFalse (equalPoints(new Point2D.Double(0,0), new Point2D.Double(1,1), 1));
	    assertTrue  (equalPoints(new Point2D.Double(0,0), new Point2D.Double(1,1), 2));
	    assertTrue  (equalPoints(new Point2D.Double(0/0.0,0/0.0), new Point2D.Double(0/0.0,0/0.0), 0));
	    assertFalse (equalPoints(new Point2D.Double(0/0.0,0/0.0), new Point2D.Double(0/0.0,0), 0));
	    assertFalse (equalPoints(new Point2D.Double(0/0.0,0/0.0), new Point2D.Double(0,0/0.0), 0));
	    
	    assertTrue  (equalLines (new Line2D.Double(4.5,-1, 4.5,1),new Line2D.Double(4.5,-1, 4.5,1),0));
	    assertTrue  (equalLines (new Line2D.Double(4.5,-1, 4.5,1),new Line2D.Double(4.5,1, 4.5,-1),0));
	    assertFalse (equalLines (new Line2D.Double(4.5,-1, 4.5,1),new Line2D.Double(4.5,1, 4.5,1),0));
	    
	    // the real test
	    
	    CubicCurve2D curve = new CubicCurve2D.Double();
		Line2D expectedTwistMark = new Line2D.Double (4.5,-1, 4.5,1);

		// symmetric straight lines
		
		curve.setCurve(0,0, 0,0, 9,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,2,0), 0);
	    
		curve.setCurve(0,0, 4,0, 5,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,2,0), 0);

		// a visually straight symmetrical line doubling itself (at 3 scales)

	    curve.setCurve(0,0, 8,0, 1,0, 9,0);
        assertEqualLines(expectedTwistMark, createTwistMark(curve,2,0), 0);

		curve.setCurve(0,0, 0.8,0, 0.1,0, 0.9,0);
		expectedTwistMark.setLine(0.45,-1, 0.45,1);
        assertEqualLines(expectedTwistMark, createTwistMark(curve,2,0), 0);

		curve.setCurve(0,0, 80,0, 10,0, 90,0);
		expectedTwistMark.setLine(45,-1, 45,1);
        assertEqualLines(expectedTwistMark, createTwistMark(curve,2,0), 0);

		// asymmetrical straight lines
		
		expectedTwistMark.setLine(4.5,-5, 4.5,5);

		curve.setCurve(0,0, 8,0, 6,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0   ), 0.49);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.2 ), 0.8);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.15), 0.47);
	    
		curve.setCurve(0,0, 8.5,0, 8.5,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0   ), 3.2);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.2 ), 0.18);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.15), 0.6);
	    
		curve.setCurve(0,0, 0.5,0, 0.5,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0   ), 3.2);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.2 ), 0.18);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.15), 0.6);
	    
		curve.setCurve(0,0, 0,0, 0,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0   ), 4.5);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.2 ), 0.11);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.15), 1.1);
	    
		curve.setCurve(0,0, 9,0, 9,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0   ), 4.5);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.2 ), 0.11);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.15), 1.1);
	    
		// closed curves
		
		expectedTwistMark.setLine(0,-5, 0,5);
		curve.setCurve(0,0, -9,0, 9,0, 0,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10,0), 0);
	    
		expectedTwistMark.setLine(2,4, 12,4);
		curve.setCurve(0,0, 9,9, 9,0, 0,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0   ), 0.7);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.2 ), 0.7);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10, 0.15), 0.7);
	    
		// impossible twist mark
		
		expectedTwistMark.setLine(0/0.0,0/0.0, 0/0.0,0/0.0); // 4*NaN

		curve.setCurve(0,0, 9,0, 9,0, 0,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10,0), 0);
	    
	    curve.setCurve(0,0, 0,0, 0,0, 0,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10,0), 0);
	}
}
