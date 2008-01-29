package nl.BobbinWork.diagram.math;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import junit.framework.TestCase;
import static nl.BobbinWork.diagram.math.Annotations.*;

public class testAnnotations extends TestCase {
	
	private class Line extends Line2D.Float {
		public Line (float x1,float y1,float x2,float y2) {
			super(x1,y1,x2,y2);
		}
		public boolean equals(Line2D.Float l){
			//FIXME: not used by assertEquals
			return this.getX1() == l.x1 //
			    && this.getY1() == l.y1 //
			    && this.getX2() == l.x2 //
			    && this.getY2() == l.y2 ;
		}
	}

	public void testCreateTwistMark() {

		Point2D p1 = new Point2D.Double(0, 0);
        Point2D cp1 = new Point2D.Double(8, 0);
        Point2D cp2 = new Point2D.Double(7, 0);
        Point2D p2 = new Point2D.Double(9, 0);
        CubicCurve2D c = new CubicCurve2D.Double();
        c.setCurve(p1, cp1, cp2, p2);
        
        assertEquals(new Line(4.5f,-1.0f,4.5f,1.0f), createTwistMark(c,2));

		fail("Not yet implemented");
	}

}
