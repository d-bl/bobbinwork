/* Connectors.java Copyright 2006-2009 by J. Falkink-Pol
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
import java.util.List;
import java.util.Vector;

/**
 * Manages line segments like connectors on pieces of a jig saw puzzle.
 * Connected pieces result into a bigger piece with less connectors than the sum
 * of connectors of the individual pieces.
 * 
 * The <em>point</em>s at the border of a partition are
 * <code>getIns(i).getStart()</code> and <code>getOut(i).getEnd()</code>.
 * 
 * @author J. Falkink-Pol
 */
class Connectors<T extends Segment> {

    /** segments along the top edge of the jig saw piece (left to right) */
	private List<T> ins;

	/** segments along the bottom edge of the jig saw piece (left to right) */
	private List<T> outs;
	
	/**
	 * points along the left edge of the jig saw piece, in bottom up (clockwise)
	 * order
	 */
	private List<Point> lefts;
	
	/**
	 * points along the right edge of the jig saw piece, in top down (clockwise)
	 * order
	 */
	private List<Point> rights;

    /**
	 * Creates a populated instance for threads of a switch or pairs of a
	 * stitch.
	 * 
	 * <pre>
	 * ins:   a   b 
	 *         \ /  
	 *          X   
	 *         / \  
	 * outs:  b   a
	 * </pre>
	 * 
	 * @param segments
	 *            ordered (a,b) as shown in the figure.
	 */
    Connectors(List<T> segments) {
        // TODO take the widths of the threads into account
        ins = segments;
        outs = reverse(segments);
        rights = new Vector<Point>(2);
        lefts = new Vector<Point>(2);
        Shape tmpBounds = getBounds();
        T a = segments.get(0);
        T b = segments.get(segments.size()-1);
        /*
         *     a.s   b.s                    a.C1     b.C1
         *      (     )     a.C1  ___ a.s    /__     __\
         *     / \   / \    b.C1 / __ b.s   /   \   /   \
         * a.C1   \ /  b.C1     / /       a.s    \ /    b.s
         *         X            |/                X
         * b.C2   / \  a.C2     |\               / \
         *     \ /   \ /    a.C2| \__ a.e       /   \
         *      (     )     b.C2 \___ b.e     b.e  a.e 
         *     b.e   a.e
         *    
         * a.s,b.s,b.c1,a.c2,a.e,b.e,b.c2,a.c1
         * a.s,b.s,a.e,b.e,b.c2,a.c2,b.c1,a.c1
         * a.s,a.c1,b.c1,b.s,a.e,b.e
         */
		addPoint(tmpBounds,rights,b.getC1());
		addPoint(tmpBounds,rights,a.getC2());
        addPoint(tmpBounds,lefts,b.getC2());
        addPoint(tmpBounds,lefts,a.getC1());
    }

	/** Creates a copy of the list with the elements in reversed order.
	 * 
	 * @param segments
	 * @return
	 */
	private List<T> reverse(List<T> segments) {
		List<T> list = new Vector<T>(segments.size());
        for (int i = segments.size(); --i >=0; ) {
        	list.add(segments.get(i));
        }
		return list;
	}
    
    /** Adds point to list if point is outside bounds.
     * 
     * @param bounds
     * @param list
     * @param point
     */
    private void addPoint(Shape bounds, List<Point> list, Point point){
        if ( ! bounds.contains(point) )
        	rights.add(point);
    }

    /**
     * Creates a new instance to be populated by
     * <code>connect()</code>.
     * 
     * @param count
     *            the number of chained thread/pair segments.
     */
    Connectors(int count) {
        ins = new Vector<T>(count);
        outs = new Vector<T>(count);
        rights = new Vector<Point>(2);
        lefts = new Vector<Point>(2);
        for (int i=0;i<count;i++) {
        	ins.add(null);
        	outs.add(null);
        }
    }

    /**
     * Connects the starting segments of a diagram partition to the end segments
     * of preceding diagram partitions.
     * 
     * @param child
     *            Connectors of a sub partition of the diagram
     * @param offset
     *            first bobbin/pair used by the sub partition
     */
    void connect(Connectors<T> child, int offset) {
        // visualisation of a diagram partition (stitches/switches):
        // _____
        // |x x|
        // | x |
        // |x x|
        // |_x_|
        int end = Math.min(child.getIns().size(), ins.size() - offset);
        for (int i = 0; i < end; i++) {
            if (ins.get(offset + i) == null) {
                ins.set(offset + i, child.getIns().get(i));
            } else if (outs.get(offset + i) != null) {
                outs.get(offset + i).setNext(child.getIns().get(i));
            }
            if (child.outs.get(i) != null) {
                outs.set(offset + i, child.outs.get(i));
            }
        }
        // TODO take lefts/rights of children into account
    }

    /**
     * @return the thread/pair segments at the start of the diagram partion from
     *         left to right
     */
    List<T> getIns() {
        return ins;
    }

    /** @return @see Partition#getBounds() */
    Shape getBounds() {
    	Bounds shape = new Bounds();
    	System.out.println();
        for (T in:ins) {
        	if (in != null)
        		shape.add(in.getStart());
        }
        for (T out:reverse(outs)) {
        	if (out != null)
        		shape.add(out.getEnd());
        }
        return shape;
    }
}