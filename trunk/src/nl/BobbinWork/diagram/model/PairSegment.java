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


/**
 * A segment of a continuous line. From straight to S-shape.
 * </p>
 * <p>
 * 
 * Subsequent segments in a chain share their start and end point. For pairs
 * each segment has its own Style. Threads have a single ThreadStyle for the
 * whole chain.
 * 
 * @author J. Falkink-Pol
 */

public class PairSegment extends Segment {

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
	 *            length of the linesegment perpendiculair through the pair
	 *            segment to indicate a twist
	 */
    public PairSegment(Point start, Point c1, Point c2, Point end, int twistMarkLength) {
    	super(start,c1,c2,end);
    	this.twistMarkLength = twistMarkLength;
    }
    
    void draw(java.awt.Graphics2D g2) {
        super.draw(g2);
        if (twistMarkLength > 0) {
            g2.draw(createTwistMark(getCurve(),twistMarkLength));
        }
    }
    
}
