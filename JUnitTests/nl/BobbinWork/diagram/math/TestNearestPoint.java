/* Copyright 2008 by J. Falkink-Pol
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
package nl.BobbinWork.diagram.math;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;

import junit.framework.TestCase;
import static nl.BobbinWork.diagram.math.NearestPoint.*;

/**
 * @author J. Falkink-Pol
 *
 */
public class TestNearestPoint extends TestCase {

	/**
	 * Test method for {@link nl.BobbinWork.diagram.math.NearestPoint#onCurve(java.awt.geom.CubicCurve2D, java.awt.geom.Point2D, java.awt.geom.Point2D)}.
	 */
	public void testOnCurve() {
		
		Point2D p;
        Point2D pn = new Point2D.Double();
        Point2D p1 = new Point2D.Double(0, 0);
        Point2D cp1 = new Point2D.Double(1, 2);
        Point2D cp2 = new Point2D.Double(3, 3);
        Point2D p2 = new Point2D.Double(4, 2);
        CubicCurve2D c = new CubicCurve2D.Double();
        c.setCurve(p1, cp1, cp2, p2);
        
        assertEquals (0.0, onCurve(c, p1, pn) );
        assertEquals (p1, pn );
        assertEquals (0.0, onCurve(c, p1, null) );

        assertEquals (0.0, onCurve(c, p2, pn) );
        assertEquals (p2, pn );
        assertEquals (0.0, onCurve(c, p2, null) );

        p = new Point2D.Double(-2, -1);
        assertEquals (5.0, onCurve(c, p, pn) );
        assertEquals (p1, pn );
        assertEquals (5.0, onCurve(c, p, null) );

        p = new Point2D.Double(5, 1);
        assertEquals (2.0, onCurve(c, p, pn) );
        assertEquals (p2, pn );
        assertEquals (2.0, onCurve(c, p, null) );

        assertTrue (1.0 > onCurve(c, cp1, pn) );
        assertTrue (1.0 > onCurve(c, cp1, null) );

        assertTrue (1.0 > onCurve(c, cp2, pn) );
        assertTrue (1.0 > onCurve(c, cp2, null) );

        p = new Point2D.Double(0, 3);
        assertTrue (4.0 > onCurve(c, cp1, pn) );
        assertTrue (4.0 > onCurve(c, cp1, null) );

        p = new Point2D.Double(3, 4);
        assertTrue (4.0 > onCurve(c, cp2, pn) );
        assertTrue (4.0 > onCurve(c, cp2, null) );
	}
}
