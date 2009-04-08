/* ThreadSegment.java Copyright 2006-2007 by J. Falkink-Pol
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

/**
 * A segment of a thread.
 * 
 * All segments in one chain share a single style instance. Different chains
 * have different style instances. Segments in a single cross or switch stick
 * together like Siamese twins. Movements are also restricted by pins. When
 * connecting threads the downstream part gets the Style of the upstream part.
 * 
 * @author J. Falkink-Pol
 */
public class ThreadSegment extends Segment {

    /** Creates an unconnected ThreadSegment with a default Style.
     *  
	 * @param start
	 *            start point of the bezier curve
	 * @param c1
	 *            optional first control point of the bezier curve
	 * @param c2
	 *            optional second control point of the bezier curve
	 * @param end
	 *            end point of the bezier curve
     */
    public ThreadSegment(Point start, Point c1, Point c2, Point end) {
        super(start, c1, c2, end);
        setStyle((Style) new ThreadStyle());
    }

    void disconnectStart() {

        ThreadStyle style = new ThreadStyle((ThreadStyle) getPrevious().getStyle());
        super.disconnectStart();

        Segment segment = this;
        while (null != segment) {
            segment.setStyle(style);
            segment = segment.getNext();
        }
    }

    void disconnectEnd() {

        if (getPrevious() != null) {
            ThreadStyle style = new ThreadStyle((ThreadStyle) getPrevious().getStyle());
            super.disconnectEnd();

            Segment segment = getNext();
            while (null != segment) {
                segment.setStyle(style);
                segment = segment.getNext();
            }
        }
    }

    void setNext(Segment next) {
        super.setNext(next);
        while (next != null) {
            next.setStyle(getStyle());
            next = next.getNext();
        }
    }

    void setPrevious(Segment previous) {
        super.setPrevious(previous);
        while (previous != null) {
            previous.setStyle(getStyle());
            previous = previous.getNext();
        }
    }

    void draw(java.awt.Graphics2D g2) {
        // shadow part
        ThreadStyle p = (ThreadStyle) getStyle();
        g2.setPaint(p.getShadow().getColor());
        g2.setStroke(new BasicStroke( //
                p.getShadow().getWidth() * 1, //
                BasicStroke.CAP_BUTT, //
                BasicStroke.JOIN_MITER));
        g2.draw(getCurve ());
        // front part
        super.draw(g2);
    }

    public ThreadStyle getStyle() {
        return (ThreadStyle) super.getStyle();
    }

}
