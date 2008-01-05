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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import nl.BobbinWork.diagram.xml.AttributeType;

import org.w3c.dom.Element;

/**
 * A pin supporting a stitch or pair.
 * 
 * @author J. Falkink-Pol
 * 
 */
public class Pin extends Partition {

    Point position = null;

    /**
     * @param element
     *            an XML element of the form
     *            <code>&lt;pin position"<em>x,y</em>"&gt;</code>
     */
    public Pin(Element element) {
        super(element);
        position = new Point(element.getAttribute(AttributeType.position.toString()));
    }

    /**
     * Adss a black dot to the drawing.
     * 
     * @param g2
     *            the drawing
     * @param pair
     *            ignored
     * @param thread
     *            ignored
     */
    public void draw(Graphics2D g2, boolean pair, boolean thread) {
        g2.setPaint(Color.BLACK);
        g2.fill(new Ellipse2D.Double(position.x - 2, position.y - 2, 4, 4));
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

}
