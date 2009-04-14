package nl.BobbinWork.diagram.model;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;

/**
 * A segment of a continuous line. From straight to S-shape. Subsequent segments
 * in a chain share their start and end point.
 * 
 */
public class Segment {

	private Point start;
	private Point c1;
	private Point c2;
	private Point end;
	private Segment previous = null;
	private Segment next = null;
	private Style style = new Style();

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
    public Segment(Point start, Point c1, Point c2, Point end) {
    	this.start = new Point(start);
    	this.end = new Point(end);
    	if (c1==null) {
    		this.c1 = pointBetween(start, end, 1 / 3);
    	} else {
    		this.c1 = new Point(c1);
    	}
    	if (c2==null) {
    		this.c2 = pointBetween(end, start, 1 / 3);
    	} else {
    		this.c2 = new Point(c2);
    	}
    }
    
	protected Point pointBetween(Point a, Point b, double t) {
	    return new Point((a.getX() * (1 - t)) + (b.getX() * t), (a.getY() * (1 - t)) + (b.getY() * t));
	}

	protected Point getStart() {
	    return start;
	}

	protected void setStart(Point start) {
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

	protected Point getEnd() {
	    return end;
	}

	protected void setEnd(Point end) {
	    this.end.setLocation(end);
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

	public Segment getPrevious() {
	    return previous;
	}

	public Segment getNext() {
	    return next;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	/**
	 * @return the style of the segment
	 */
	public Style getStyle() {
	    return style;
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

}
