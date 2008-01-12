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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;

import org.w3c.dom.Element;
import static nl.BobbinWork.math.Annotations.createTwistMark;

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

class Segment {

    private Point start, c1, c2, end;

    private int twistMarkLength = 0;

    protected Segment previous = null, next = null;

    protected Style style = new Style();

    /**
     * Creates a new instance of Segment, from an XML element with segment
     * property attributes.
     * 
     * @param element
     *            XML element, one of: <pair ...>, <back ...>, <front ...>
     */
    public Segment(Element element) {

        if (element.getAttributes().getNamedItem("mark") != null) {
            try {
                twistMarkLength = Integer.parseInt(element.getAttribute("mark"));
            } catch (NumberFormatException e) {
                twistMarkLength = 9 * style.getWidth();
            }
        }
        start = new Point(element.getAttribute("start"));
        end = new Point(element.getAttribute("end"));
        String c1 = element.getAttribute("c1");
        String c2 = element.getAttribute("c2");
        boolean b1 = (c1 == null) || (c1.equals(""));
        boolean b2 = (c2 == null) || (c2.equals(""));
        if (b1 && b2) {
            this.c1 = pointBetween(start, end, 1 / 3);
            this.c2 = pointBetween(end, start, 1 / 3);
        } else if (b1) {
            this.c2 = new Point(c2);
            this.c1 = pointBetween(start, this.c2, 1 / 3);
        } else if (b2) {
            this.c2 = pointBetween(this.c1, start, 1 / 3);
            this.c1 = new Point(c1);
        } else {
            this.c1 = new Point(c1);
            this.c2 = new Point(c2);
        }
    }

    private Point pointBetween(Point a, Point b, double t) {
        return new Point((a.getX() * (1 - t)) + (b.getX() * t), (a.getY() * (1 - t)) + (b.getY() * t));
    }

    /**
     * Creates a new instance of Segment, control points are set on 1/3 of the
     * straight line.
     * 
     * @param start
     *            start point of the bezier curve
     * @param end
     *            end point of the bezier curve
     */
    public Segment(Point start, Point end) {
        this.start = new Point(start);
        this.end = new Point(end);
        this.c1 = pointBetween(start, end, 1 / 3);
        this.c2 = pointBetween(end, start, 1 / 3);
    }

    /**
     * Creates a new instance of Segment with identical control points.
     * 
     * @param start
     *            start point of the bezier curve
     * @param c
     *            first and second control point of the bezier curve
     * @param end
     *            end point of the bezier curve
     */
    public Segment(Point start, Point c, Point end) {
        this.start = new Point(start);
        this.c1 = new Point(c);
        this.c2 = new Point(c);
        this.end = new Point(end);
    }

    /**
     * Creates a new instance of Segment.
     * 
     * @param start
     *            start point of the bezier curve
     * @param c1
     *            first control point of the bezier curve
     * @param c2
     *            second control point of the bezier curve
     * @param end
     *            end point of the bezier curve
     */
    public Segment(Point start, Point c1, Point c2, Point end) {
        this.start = new Point(start);
        this.c1 = new Point(c1);
        this.c2 = new Point(c2);
        this.end = new Point(end);
    }

    Point getStart() {
        return start;
    }

    void setStart(Point start) {
        this.start.setLocation(start);
    }

    Point getC1() {
        return c1;
    }

    void setC1(Point c1) {
        this.c1.setLocation(c1);
    }

    Point getC2() {
        return c2;
    }

    void setC2(Point c2) {
        this.c2.setLocation(c2);
    }

    Point getEnd() {
        return end;
    }

    void setEnd(Point end) {
        this.end.setLocation(end);
    }

    public Segment getPrevious() {
        return previous;
    }

    /**
     * Disconnect the segment from the previous one. The shared point
     * (previous.end and this.start) is cloned. The subclass for threads clones
     * the Style for one part of the chain.
     */
    void disconnectStart() {
        // disconnent old connection
        if ((this.previous != null) && (this.previous.getNext() != null) && (this.previous.getNext() != this)) {
            this.setStart(new Point(this.previous.getEnd()));
            this.previous.setNext(null);
        }
        this.previous = null;
    }

    /**
     * Disconnect the segment from the next one. The shared point (this.end and
     * next.start) is cloned. The subclass for threads clones the Style for one
     * part of the chain.
     */
    void disconnectEnd() {
        // disconnent old connection
        if ((this.next != null) && (this.next.getPrevious() != null) && (this.next.getPrevious() != this)) {
            this.previous.setStart(new Point(this.end));
            this.previous.setPrevious(null);
        }
        this.next = null;
    }

    /**
     * Connects two segments.
     * 
     * The connected segments will share one point: previous.end and this.start.
     * 
     * @param previous
     *            the segment to connect to
     */
    void setPrevious(Segment previous) {
        disconnectStart();
        setStart(pointBetween(previous.getEnd(), start, 0.5));
        previous.setEnd(getStart());
        this.previous = previous;
    }

    public Segment getNext() {
        return next;
    }

    /**
     * Connects two segments.
     * 
     * The connected segments will share one point: this.end and next.start.
     * 
     * @param next
     *            the segment to connect to
     */
    void setNext(Segment next) {
        if (next != null) {
            disconnectEnd();
            setEnd(pointBetween(next.getStart(), end, 0.5));
            next.setStart(getEnd());
            next.previous = this;
        }
        this.next = next;
    }

    /**
     * @return the style of the segment
     */
    public Style getStyle() {
        return style;
    }

    void draw(java.awt.Graphics2D g2) {
        Color color = style.getColor();
        g2.setPaint(color);
        g2.setStroke(new BasicStroke( //
                getStyle().getWidth() * 1.0f, //
                BasicStroke.CAP_BUTT, //
                BasicStroke.JOIN_MITER //
                ));
        g2.draw(getCurve());
        if (twistMarkLength > 0) {
            g2.draw(createTwistMark(getCurve(),twistMarkLength));
        }
    }

    /** Gets the cubic bezier curve defining the shape of the segment. 
     * @return the curve defining the shape of the segment
     */
    public CubicCurve2D getCurve() {
        return new CubicCurve2D.Double(start.x, start.y, c1.x, c1.y, c2.x, c2.y, end.x, end.y);
    }

    /**
     * Gets the polygon (triangle or quadrangle) that completely contains the
     * curve and its control points.
     * 
     * @return the shape containing the curve
     */
    Shape getConvexHull() {
        Shape //
        polygon = new Polygon( //
                new int[] { (int) start.x, (int) c1.x, (int) c2.x }, //
                new int[] { (int) start.y, (int) c1.y, (int) c2.y }, //
                3);
        if (polygon.contains(end.x, end.y)) {
            return polygon;
        }
        polygon = new Polygon( //
                new int[] { (int) start.x, (int) c1.x, (int) end.x }, //
                new int[] { (int) start.y, (int) c1.y, (int) end.y }, //
                3);
        if (polygon.contains(c2.x, c2.y)) {
            return polygon;
        }
        polygon = new Polygon( //
                new int[] { (int) start.x, (int) c2.x, (int) end.x }, //
                new int[] { (int) start.y, (int) c2.y, (int) end.y }, //
                3);
        if (polygon.contains(c1.x, c1.y)) {
            return polygon;
        }
        polygon = new Polygon( //
                new int[] { (int) c1.x, (int) c2.x, (int) end.x }, //
                new int[] { (int) c1.y, (int) c2.y, (int) end.y }, //
                3);
        if (polygon.contains(start.x, start.y)) {
            return polygon;
        }
        polygon = new Polygon( //
                new int[] { (int) start.x, (int) c1.x, (int) c2.x, (int) end.x }, //
                new int[] { (int) start.y, (int) c1.y, (int) c2.y, (int) end.x }, //
                4);
        if (polygon.contains(pointBetween(start, end, 0.5))) {
            return polygon;
        }
        return new Polygon( //
                new int[] { (int) start.x, (int) c2.x, (int) end.x, (int) c1.x }, //
                new int[] { (int) start.y, (int) c2.y, (int) end.x, (int) c1.y }, //
                4);
    }
    
}
