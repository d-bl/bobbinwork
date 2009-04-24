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
	 * Creates a shape through points of (usually 2) segments. Some examples:
	 * 
	 * <pre>
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
	 * </pre>
	 */
	Bounds(List<T> segments) {
		// TODO make it work for all three situations
		T a = segments.get(0);
		T b = segments.get(segments.size() - 1);
		Bounds<T> tmpBounds = new Bounds<T>();
		for (T segment : segments) {
			if (segment != null)
				add(segment.getStart());
			tmpBounds.add(segment.getStart());
		}
		add(tmpBounds, b.getC1());
		add(tmpBounds, a.getC2());
		for (T segment : reverse(segments)) {
			if (segment != null)
				tmpBounds.add(segment.getEnd());
		}
		add(tmpBounds, b.getC2());
		add(tmpBounds, a.getC1());
	}

	Bounds() {
	}
	
	void merge(Bounds<T> newBounds) {
		// TODO implement this stub
	}
	
	/** TODO Delete when merge is implemented. */
	Bounds(List<T> ins, List<T> outs) {
		for (T segment : ins) {
			if (segment != null) {
				add(segment.getStart());
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

	private void add(Bounds<T> tmpBounds, Point point) {
		if (!tmpBounds.contains(point))
			add(point);
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
