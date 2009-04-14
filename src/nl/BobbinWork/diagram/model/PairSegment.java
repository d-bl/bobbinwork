/* Segment.java Copyright 2006-2007 by J. Falkink-Pol
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

import static nl.BobbinWork.diagram.math.Annotations.createTwistMark;

import java.awt.Shape;

/**
 * A line segment representing a pair of threads.
 * 
 * All segments in one stitch share a single style instance. Different stitches
 * have different style instances. Segments in a single stitch stick together
 * like Siamese twins. Movements are also restricted by pins.
 * 
 * @author J. Falkink-Pol
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
    	return createTwistMark(getCurve(),twistMarkLength);
    }
}
