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
 * A segment of a thread. <br>
 * <br>
 * 
 * The full chain of segments shares a single style object. Uppon
 * connect/disconnect style objects are disposed/cloned.
 * 
 * @author J. Falkink-Pol
 */
public class ThreadSegment extends Segment {

    /** @see nl.BobbinWork.diagram.model.Segment#Segment(Point, Point, Point, Point) */
    public ThreadSegment(Point start, Point c1, Point c2, Point end) {
        super(start, c1, c2, end, 0);
        setStyle((Style) new ThreadStyle());
    }

    void disconnectStart() {

        ThreadStyle style = new ThreadStyle((ThreadStyle) previous.getStyle());
        super.disconnectStart();

        Segment segment = this;
        while (null != segment) {
            segment.setStyle(style);
            segment = segment.getNext();
        }
    }

    void disconnectEnd() {

        if (previous != null) {
            ThreadStyle style = new ThreadStyle((ThreadStyle) previous.getStyle());
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
        g2.setPaint(p.getBackGround().getColor());
        g2.setStroke(new BasicStroke( //
                p.getBackGround().getWidth() * 1, //
                BasicStroke.CAP_BUTT, //
                BasicStroke.JOIN_MITER));
        g2.draw(getCurve ());
        // front part
        super.draw(g2);
    }

    /**
     * @return the style of the segment
     */
    public ThreadStyle getStyle() {
        return (ThreadStyle) getStyle();
    }

}
