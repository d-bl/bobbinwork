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

import static nl.BobbinWork.diagram.math.NearestPoint.onCurve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;


public class TestNearestPoint {

	private Point2D pn;
	private Point2D p1;
	private CubicCurve2D c;
	private Point2D p2;
	private Point2D cp1;
	private Point2D cp2;

	@Before
	public void setUp() {
		
        pn = new Point2D.Double();
        p1 = new Point2D.Double(0, 0);
        cp1 = new Point2D.Double(1, 2);
        cp2 = new Point2D.Double(3, 3);
        p2 = new Point2D.Double(4, 2);
        c = new CubicCurve2D.Double();
        c.setCurve(p1, cp1, cp2, p2);
	}
	
	@Test
    public void startPoint() {
		assertEquals (0.0, onCurve(c, p1, null) );
        assertEquals (0.0, onCurve(c, p1, pn) );
        assertEquals (p1, pn );
	}
	
	@Test
    public void endPoint() {
		assertEquals (0.0, onCurve(c, p2, null) );
        assertEquals (0.0, onCurve(c, p2, pn) );
        assertEquals (p2, pn );
	}
	
	@Test
    public void controlPoints() {
		double a = onCurve(c, cp1, null);
		assertTrue (1.0 > a );
		double b = onCurve(c, cp1, pn);
		assertTrue (1.0 > b );
		
		double p = onCurve(c, cp2, null);
		assertTrue (1.0 > p );
		double q = onCurve(c, cp2, pn);
		assertTrue (1.0 > q );
	}
	
	@Test
    public void otherPoint1() {
		
		Point2D p = new Point2D.Double(-2, -1);
        assertEquals (5.0, onCurve(c, p, pn) );
        assertEquals (p1, pn );
	}
	
	@Test
    public void otherPoint2() {
		
		Point2D p = new Point2D.Double(5, 1);
        assertEquals (2.0, onCurve(c, p, pn) );
        assertEquals (p2, pn );
	}

	private class subNearestPoint extends NearestPoint {
		subNearestPoint () {
		}
	}

	@Test
	public void constructor() {
		new subNearestPoint();
	}
}
