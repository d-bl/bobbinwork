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
	 * @param tolerance maximum distance between both points
	 * @return true if the points are close enough
	 */
	private boolean equalPoints (Point2D expected, Point2D actual, double tolerance){
		return expected.distance(actual) <= tolerance;
	}
	
	private void assertEqualLines (Line2D expected, Line2D actual, double tolerance){
		
		String message = "expected line " + lineToString(expected) //
                            + " but got " + lineToString(actual);
		
		assertTrue ("bad test: line shorter than tolerance*2:", //
				 2 * tolerance <= expected.getP1().distance( expected.getP2() ) );
		
		assertTrue ( message, ( equalPoints(expected.getP1(), actual.getP1(), tolerance) &&
		                        equalPoints(expected.getP2(), actual.getP2(), tolerance)
		                      ) || 
		                      ( equalPoints(expected.getP1(), actual.getP2(), tolerance) &&
		                        equalPoints(expected.getP2(), actual.getP1(), tolerance)
		                      ) 
                   );
 	}

	/**
	 * Test method for {@link nl.BobbinWork.diagram.math.Annotations#CreateTwistMark(java.awt.geom.CubicCurve2D, int)}.
	 */
	public void testCreateTwistMark() {

		CubicCurve2D curve = new CubicCurve2D.Double();
		Line2D expectedTwistMark = new Line2D.Double (4.5,-1, 4.5,1);

		// symmetric straight lines
		
		curve.setCurve(0,0, 0,0, 9,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,2), 0);
	    
		curve.setCurve(0,0, 4,0, 5,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,2), 0);

		// a visually straight symmetrical line doubling itself (at 3 scales)

	    curve.setCurve(0,0, 8,0, 1,0, 9,0);
        assertEqualLines(expectedTwistMark, createTwistMark(curve,2), 0);

		curve.setCurve(0,0, 0.8,0, 0.1,0, 0.9,0);
		expectedTwistMark.setLine(0.45,-1, 0.45,1);
        assertEqualLines(expectedTwistMark, createTwistMark(curve,2), 0);

		curve.setCurve(0,0, 80,0, 10,0, 90,0);
		expectedTwistMark.setLine(45,-1, 45,1);
        assertEqualLines(expectedTwistMark, createTwistMark(curve,2), 0);

		// asymmetrical straight lines
		
		expectedTwistMark.setLine(4.5,-5, 4.5,5);

		curve.setCurve(0,0, 8,0, 6,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10), 0.5);
	    
		curve.setCurve(0,0, 6,0, 8,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10), 1.1);
	    
		curve.setCurve(0,0, 0,0, 0,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10), 4.5);
	    
		curve.setCurve(0,0, 9,0, 9,0, 9,0);
	    assertEqualLines(expectedTwistMark, createTwistMark(curve,10), 1.1);
	    
        fail("Too much tolerance required");
	}
}
