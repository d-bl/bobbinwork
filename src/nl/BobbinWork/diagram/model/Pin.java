/* Pin.java Copyright 2006-2007 by J. Falkink-Pol
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
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A pin supporting a stitch or pair.
 * 
 * @author J. Falkink-Pol
 * 
 */
public class Pin extends Partition {

	private static final int DIAMETER = 2;
	private static final int RADIUS = DIAMETER*2;
	private static final Style STYLE = new Style();
    Point position = null;

    public Pin(Point position2) {
		STYLE.setWidth(0);
        position = position2;
    }

    public Shape getHull() {
        Polygon p = new Polygon();
        for (int i = 0; i < 4; i++) {
            p.addPoint((int) position.x, (int) position.y);
        }
        return p;
    }

    public int getNrOfPairs() {
        return 0;
    }

    final Iterator<Drawable> threadIterator () {
		return iterator();
	}

	final Iterator<Drawable> pairIterator () {
		return iterator();
	}
	
	final Iterator<Drawable> pinIterator () {
		return iterator();
	}
	
	private Iterator<Drawable> iterator() {
		double x = position.x - DIAMETER;
		double y = position.y - DIAMETER;
		Ellipse2D.Double dot = new Ellipse2D.Double(x, y, RADIUS, RADIUS);
		Drawable[] a = {new Drawable(dot,STYLE)};
		return Arrays.asList(a).iterator();
	}
}
