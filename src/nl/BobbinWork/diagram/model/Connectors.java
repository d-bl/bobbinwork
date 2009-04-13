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

import java.awt.Polygon;
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

    private List<T> ins;

    private List<T> outs;

    /**
     * Creates a populated instance of <code>Connectors</code>.
     * 
     * @param segments
     *            in incomming order, for the outgoing segments, the order is
     *            reversed.
     */
    Connectors(List<T> segments) {
        ins = segments;
        outs = new Vector<T>(segments.size());
        for (int i = segments.size(); --i >=0; ) {
        	outs.add(segments.get(i));
        }
    }

    /**
     * Creates a new instance of <code>Connectors</code> to be populated by
     * <code>connect()</code>.
     * 
     * @param count
     *            the number of chained thread/pair segments.
     */
    Connectors(int count) {
        ins = new Vector<T>(count);
        outs = new Vector<T>(count);
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
    }

    /**
     * @return the thread/pair segments at the start of the diagram partion from
     *         left to right
     */
    List<T> getIns() {
        return ins;
    }

    /** @return @see Partition#getHull() */
    Shape getHull() {
        Polygon shape = new Polygon();
        for (int i = 0; i < ins.size(); i++) {
            if (ins.get(i) != null) {
                shape.addPoint((int) ins.get(i).getStart().x, (int) ins.get(i).getStart().y);
            }
        }
        for (int i = outs.size() - 1; i >= 0; i--) {
            if (outs.get(i) != null) {
                shape.addPoint((int) outs.get(i).getEnd().x, (int) outs.get(i).getEnd().y);
            }
        }
        // TODO take C1/C2 of ins/out[ 0 / length-1] into account
        // TODO take the widths of the threads into account
        // TODO make upper and lower edge more convex but avoid overlap
        return shape;
    }

}