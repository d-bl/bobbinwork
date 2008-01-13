/* MultiplePairsPartition.java Copyright 2006-2007 by J. Falkink-Pol
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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import static nl.BobbinWork.math.Nearest.nearest;

import org.w3c.dom.Element;

/**
 * DiagramPanel section containing multiple pair segments.
 * 
 * @author J. Falkink-Pol
 * 
 */
abstract class MultiplePairsPartition extends MultipleThreadsPartition {

    private java.util.Vector<Partition> partitions = new java.util.Vector<Partition>();

    public java.util.Vector<Partition> getPartitions() {
        return partitions;
    }

    public void draw(java.awt.Graphics2D g2, boolean pair, boolean thread) {
        if (visible) {
            java.util.Vector<Partition> v = getPartitions();
            for (int i = 0; i < v.size(); i++) {
                Partition n = v.get(i);
                n.draw(g2, pair, thread);
            }
        }
    }

    public Shape getHull() {
        return super.getHull();
        // TODO merge with partions[].getBounds for pins
    }

    /**
     * Searches for a <code>Cross</code> or <code>Twist</code> with the
     * coordinates within its bounds. Zooms in on diagram sections as long as
     * the specified position is within the bounds of the section.
     * 
     * 
     * @param x
     *            position alon the x-axis
     * @param y
     *            position alon the y-axis
     * 
     * @return null or a <code>Cross</code> or <code>Twist</code> with the
     *         coordinates within its bounds.
     * 
     * @see nl.BobbinWork.diagram.model.Ends#getHull
     */
    public MultipleThreadsPartition getSwitchAt(int x, int y) {
        Vector<Partition> v = getPartitions();
        for (int i = v.size() - 1; i >= 0; i--) {
            Partition p = v.get(i);
            if (p instanceof MultipleThreadsPartition) {
                MultipleThreadsPartition n = (MultipleThreadsPartition) p;
                if (n.getHull().contains(x, y)) {
                    if (n instanceof Switch) {
                        return n;
                    } else if (n instanceof MultiplePairsPartition) {
                        return ((MultiplePairsPartition) n).getSwitchAt(x, y);
                    }
                }
            }
        }
        return null;
    }

    public ThreadSegment getThreadAt(int x, int y) {
        Switch s = (Switch) getSwitchAt(x, y);
        if (s != null) {
            ThreadSegment front = s.getFront();
            ThreadSegment back = s.getBack();
            CubicCurve2D frontCurve = front.getCurve();
            CubicCurve2D nearest = nearest( //
                    new Point2D.Double(x, y), //
                    frontCurve, //
                    front.getStyle().getBackGround().getWidth(), //
                    back.getCurve(), //
                    back.getStyle().getBackGround().getWidth() //
                    );
            if (frontCurve==nearest){
                return front;
            }
            return back;

        } else {
            return null;
        }

    }

    /** Range of pairs of the parent used in this Group/Stitch. */
    private Range pairRange = null;

    Range getPairRange() {
        return pairRange;
    }

    void setPairRange(Range pairs) {
        this.pairRange = pairs;
    }

    /** Pairs going into and coming out of a Group/Stitch. */
    private Ends pairEnds = null;

    Ends getPairEnds() {
        return pairEnds;
    }

    void setPairEnds(Ends pairEnds) {
        this.pairEnds = pairEnds;
    }

    /** Create a new tree of MultipleThreadsPartition's. */
    /**
     * @param element
     *            XML element &lt;pattern&gt; or &lt;group&gt;
     */
    public MultiplePairsPartition(Element element) {
        super(element);
    }

}
