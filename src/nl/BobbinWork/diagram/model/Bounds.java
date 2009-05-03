/* Bounds.java Copyright 2009 by J. Falkink-Pol
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

import java.awt.Polygon;
import java.util.List;
import java.util.Vector;

public class Bounds<T extends Segment> extends Polygon {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a shape around (usually 2) crossing segments.<br>
	 * <br>
	 * For more or less straight segments, the bounding polygon would be:
	 * a.s,b.s,a.e,b.e. The figures below show some examples where the curves
	 * are not completely within the bounds described above. The start of a
	 * third segment would always be between a.s/b.s and its end between a.e/b.e
	 * 
	 * <pre>
	 *   in a plait      head side     in false foot side
	 * 
	 *     a.s   b.s                     a.C1     b.C1
	 *      (     /     a.C1   ___ a.s    /__     __\
	 *     / \   /       b.C1 / __ b.s   /   \   /   \
	 * a.C1   \ /            / /       a.s    \ /    b.s
	 *         X             |/                X
	 * b.C2   / \            |\               / \
	 *     \ /   \       a.C2| \__ a.e       /   \
	 *      (     \     b.C2  \___ b.e     b.e  a.e 
	 *     b.e   a.e
	 * </pre>
	 * 
	 * The proper polygons would be:
	 * 
	 * <pre>
	 * a.s,b.s,a.e,b.e,b.C2,a.C1
	 * a.s,b.s,a.e,b.e,b.C2,a.C1
	 * a.s,a.C1,b.C1,b.s,a.e,b.e
	 * </pre>
	 * 
	 * To avoid overlap in adjacent bounds (which at the same time avoids
	 * crossing control lines), use getC1c getC2c rather than getC2 getC2<br>
	 * <br>
	 * a formal expression applied to one of the corners:
	 * 
	 * <pre>
	 * if( corner(b.e,a.s,a.C1) &lt; corner(a.C1,a.s,b.s) )
	 * then a.C1 before a.s otherwise a.C1 after a.s
	 * </pre>
	 * 
	 * This does not cover the center figure. Another approach:
	 * 
	 * <pre>
	 * merge a.s,b.s,a.e,b.e with:
	 * if ( Line2D.linesIntersect(a.c1/a.c2,b.c1/b.c2) )
	 * then a.c1,b.c1,a.c2,b.c2 otherwise b.c1,a.c1,a.c2,b.c2
	 * </pre>
	 */
	Bounds(List<T> segments) {
		qadInit (segments,reverse(segments));
	}

	Bounds() {
	}
	
	void merge(Bounds<T> newBounds) {
		// TODO implement this stub
	}
	
	/** TODO Delete when merge is implemented. */
	Bounds(List<T> ins, List<T> outs) {
		qadInit(ins, outs);
	}

	/** quick and dirty implementation */
	private void qadInit(List<T> ins, List<T> outs) {
		for (T segment : ins) {
			if (segment != null) {
				add(segment.getStart());
			}
		}
		for (T segment : reverse(outs)) {
			if (segment != null) {
				add(segment.getEnd());
			}
		}
	}

	/**
	 * Creates a copy of the list with the elements in reversed order.
	 * 
	 * @param segments
	 * @return
	 */
	private List<T> reverse(List<T> segments) {
		List<T> list = new Vector<T>(segments.size());
		for (int i = segments.size(); --i >= 0;) {
			list.add(segments.get(i));
		}
		return list;
	}

	private void add(Point point) {
		addPoint((int) point.x, (int) point.y);
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < npoints; i++) {
			s += "(" + xpoints[i] + ";" + ypoints[i] + ") ";
		}
		return s;
	}

}
