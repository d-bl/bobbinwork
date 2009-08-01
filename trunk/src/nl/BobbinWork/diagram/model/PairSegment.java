/* Segment.java Copyright 2006-2007 by J. Pol
 *
 * This file is part of BobbinWork.
 *
 * BobbinWork is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BobbinWork is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BobbinWork.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.BobbinWork.diagram.model;

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A line segment representing a pair of threads.
 * 
 * All segments in one stitch share a single style instance. Different stitches
 * have different style instances. Segments in a single stitch stick together
 * like Siamese twins. Movements are also restricted by pins.
 * 
 * @author J. Pol
 */

public class PairSegment extends Segment {

	/**
	 * A value > zero implies a twist mark should be drawn in a pair diagram. 
	 * Only allowed in "pseudo" stitches of just one pair.
	 * 
	 * Not yet implemented: 
	 * 
	 * A non-zero value should exceed (at least by 4?) the width of the style.
	 * 
	 * The style in such a "pseudo" stitch is shared with a sibling:
	 * in an open diagram with the next stitch, in a close diagram with the
	 * preceding stitch.
	 */
	private int twistMarkLength = 0;
	protected static final double DEFAULT_CORRECTION = 0.15;
	
    /**
	 * @param start
	 *            start point of the bezier curve
	 * @param c1
	 *            optional first control point of the bezier curve
	 * @param c2
	 *            optional second control point of the bezier curve
	 * @param end
	 *            end point of the bezier curve
	 * @param twistMarkLength
	 *            length of the line segment perpendicular through the pair
	 *            segment to indicate a twist
	 */
    public PairSegment(Point start, Point c1, Point c2, Point end, int twistMarkLength) {
    	super(start,c1,c2,end);
    	this.twistMarkLength = twistMarkLength;
    }
    
	public boolean hasTwistMark () {
		return twistMarkLength > 0;
    	
    }
    public Shape getTwistMark () {
    	return createTwistMark(getCurve(),twistMarkLength, DEFAULT_CORRECTION);
    }

	protected static Line2D createTwistMark(CubicCurve2D curve, int length, double correction) {
	
	    // http://code.google.com/p/bobbinwork/wiki/MathSolutions#Twist_marks
	
		Point2D start = curve.getP1();
	    Point2D c1 = curve.getCtrlP1();
	    Point2D c2 = curve.getCtrlP2();
	    Point2D end = curve.getP2();
	
	    // approximation to divide the curve in more or less equal lengths
	    // TODO optimize somehow
	    double s = start.distance(c1) + start.distance(c2);
	    double e = end.distance(c1) + end.distance(c2);
	    double t = (e / (e+s)) * (1-2*correction) + correction;
	    
	    // applying Casteljau
	    Point2D a = pointBetween(start, c1, t);
	    Point2D b = pointBetween(c1, c2, t);
	    Point2D c = pointBetween(c2, end, t);
	    Point2D p = pointBetween(a, b, t);
	    Point2D q = pointBetween(b, c, t);
	    Point2D z = pointBetween(p, q, t);
	
	    double dx = z.getX() - p.getX();
	    double dy = z.getY() - p.getY();
	    if ( dx==0 && dy == 0 ) {
	    	// TODO t was 0 or 1 (what should not happen)
	    	// or c1=c2 and start=end (in which case nothing gets solved)
	        dx = z.getX() - q.getX();
	        dy = z.getY() - q.getY();
	    }
	    double scale = ((double)length) / (Math.hypot(dx, dy) * 2.0);
	    dx *= scale;
	    dy *= scale;
	
	    return new Line2D.Double(//
	              z.getX() + dy, //
	              z.getY() - dx, //
	              z.getX() - dy, //
	              z.getY() + dx);
	
	}
}
