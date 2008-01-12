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

import org.w3c.dom.Element;

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

    /** @see nl.BobbinWork.diagram.model.Segment#Segment(Point, Point) */
    ThreadSegment(Point start, Point end) {
        super(start, end);
        style = (Style) new ThreadStyle();
    }

    /** @see nl.BobbinWork.diagram.model.Segment#Segment(Point, Point, Point) */
    ThreadSegment(Point start, Point c, Point end) {
        super(start, c, end);
        style = (Style) new ThreadStyle();
    }

    /** @see nl.BobbinWork.diagram.model.Segment#Segment(Point, Point, Point, Point) */
    ThreadSegment(Point start, Point c1, Point c2, Point end) {
        super(start, c1, c2, end);
        style = (Style) new ThreadStyle();
    }

    /** Creates a new instance of ThreadSegment. 
     * 
     * @param element
     *            <code>&lt;front&nbsp;...&gt;</code> or
     *            <code>&lt;back&nbsp;...&gt;</code>
     */
    ThreadSegment(Element element) {
        super(element);
        style = (Style) new ThreadStyle();
    }

    void disconnectStart() {

        ThreadStyle style = new ThreadStyle((ThreadStyle) previous.getStyle());
        super.disconnectStart();

        Segment segment = this;
        while (null != segment) {
            segment.style = style;
            segment = segment.getNext();
        }
    }

    void disconnectEnd() {

        if (previous != null) {
            ThreadStyle style = new ThreadStyle((ThreadStyle) previous.getStyle());
            super.disconnectEnd();

            Segment segment = getNext();
            while (null != segment) {
                segment.style = style;
                segment = segment.getNext();
            }
        }
    }

    void setNext(Segment next) {
        super.setNext(next);
        while (next != null) {
            next.style = style;
            next = next.getNext();
        }
    }

    void setPrevious(Segment previous) {
        super.setPrevious(previous);
        while (previous != null) {
            previous.style = style;
            previous = previous.getNext();
        }
    }

    void draw(java.awt.Graphics2D g2) {
        // shadow part
        ThreadStyle p = (ThreadStyle) style;
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
        return (ThreadStyle) style;
    }

}
