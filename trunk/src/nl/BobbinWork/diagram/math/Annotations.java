/* Annotations.java Copyright 2005-2007 by J. Falkink-Pol
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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Annotations {

    private Annotations() {
        // only static methods so hide constructor
    }

    /**
     * Creates a twist mark
     * 
     * @param curve
     *            the curve that should get the twist mark
     * @param length
     *            of the twist mark
     * @return a perpendicular line approximately through the center of the
     *         curve
     * 
     * @author J. Falkink-Pol
     */
    public static Line2D createTwistMark(CubicCurve2D curve, int length) {

        Point2D start = curve.getP1();
        Point2D c1 = curve.getCtrlP1();
        Point2D c2 = curve.getCtrlP2();
        Point2D end = curve.getP2();

        // approximation to divide the curve in more or less equal lengths
        // see divide.gif
        // TODO perhaps quadratic/square root or whatever
        double s = start.distance(c1) + start.distance(c2);
        double e = end.distance(c1) + end.distance(c2);
        double t = e / (e + s);

        // applying casteljau.gif
        Point2D a = pointBetween(start, c1, t);
        Point2D b = pointBetween(c1, c2, t);
        Point2D c = pointBetween(c2, end, t);
        Point2D p = pointBetween(a, b, t);
        Point2D q = pointBetween(b, c, t);
        Point2D z = pointBetween(p, q, t);

        double dx = z.getX() - p.getX();
        double dy = z.getY() - p.getY();
        double scale = length / (Math.hypot(dx, dy) * 2);
        dx *= scale;
        dy *= scale;

        return new Line2D.Float(//
                (int) (z.getX() + dy), //
                (int) (z.getY() - dx), //
                (int) (z.getX() - dy), //
                (int) (z.getY() + dx));

    }

    /**
     * @author J. Falkink-Pol
     */
    static private Point2D pointBetween(Point2D a, Point2D b, double t) {
        return new Point2D.Double((a.getX() * (1 - t)) + (b.getX() * t), (a.getY() * (1 - t))
                + (b.getY() * t));
    }
}
