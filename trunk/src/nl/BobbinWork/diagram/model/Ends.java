/* Ends.java Copyright 2006-2007 by J. Falkink-Pol
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

/**
 * Keeps track of the thread/pair segments of a diagram partition.
 * 
 * The <em>points</em> at the border of a diagram partition are
 * <code>getIns(i).getStart()</code> and <code>getOut(i).getEnd()</code>.
 * 
 * @author J. Falkink-Pol
 */
class Ends {

    private Segment[] ins;

    private Segment[] outs;

    /**
     * Creates a populated instance of <code>Ends</code>.
     * 
     * @param segments
     *            in incomming order, for the outgoing segments, the order is
     *            reversed.
     */
    Ends(Segment[] segments) {
        ins = segments;
        outs = new Segment[segments.length];
        int max = segments.length - 1;
        for (int i = 0; i <= max; i++) {
            outs[i] = ins[max - i];
        }
    }

    /**
     * Creates a new instance of <code>Ends</code> to be populated by
     * <code>connect()</code>.
     * 
     * @param count
     *            the number of chained thread/pair segments.
     */
    Ends(int count) {
        ins = new Segment[count];
        outs = new Segment[count];
        for (int i = 0; i < count; i++) {
            ins[i] = outs[i] = null;
        }
    }

    /**
     * Connects the starting segments of a diagram partition to the end segments
     * of preceding diagram partitions.
     * 
     * @param child
     *            ends of a sub partition of the diagram
     * @param offset
     *            first bobbin/pair used by the sub partition
     */
    void connect(Ends child, int offset) {
        // visualisation of a diagram partition (stitches/switches):
        // _____
        // |x x|
        // | x |
        // |x x|
        // |_x_|
        int end = Math.min(child.getIns().length, ins.length - offset);
        if (end == 0 ) {
            System.out.println("nr of threads exceed nr of pairs");
        }
        for (int i = 0; i < end; i++) {
            if (ins[offset + i] == null) {
                ins[offset + i] = child.getIns()[i];
            } else if (outs[offset + i] != null) {
                outs[offset + i].setNext(child.getIns()[i]);
            }
            if (child.outs[i] != null) {
                outs[offset + i] = child.outs[i];
            }
        }
    }

    /**
     * @return the thread/pair segments at the start of the diagram partion from
     *         left to right
     */
    Segment[] getIns() {
        return ins;
    }

    /** @return @see Partition#getHull() */
    Shape getHull() {
        Polygon shape = new Polygon();
        for (int i = 0; i < ins.length; i++) {
            if (ins[i] != null) {
                shape.addPoint((int) ins[i].getStart().x, (int) ins[i].getStart().y);
            }
        }
        for (int i = outs.length - 1; i >= 0; i--) {
            if (outs[i] != null) {
                shape.addPoint((int) outs[i].getEnd().x, (int) outs[i].getEnd().y);
            }
        }
        // TODO take C1/C2 of ins/out[ 0 / length-1] into account
        // TODO take the widths of the threads into account
        // TODO make upper and lower edge more convex but avoid overlap
        return shape;
    }

}
