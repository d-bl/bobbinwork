/* Switch.java Copyright 2006-2007 by J. Falkink-Pol
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

import java.awt.geom.CubicCurve2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

/**
 * Two bobbins next to one another change positions.
 * 
 * @author J. Falkink-Pol
 */
public abstract class Switch extends MultipleThreadsPartition {

    /** Two bobbins exchanging their positions. */
    private ThreadSegment frontThread = null, backThread = null;

    /** The range of threads/bobbins involved in the cross/twist. */
    private Range threadRange = null;

    /**
     * Creates a new instance of a <code>Cross</code> or <code>Twist</code>.
     * The difference between the two is made by the abstract method
     * <code>setThreadConnectors</code> determining whether the <code>frontThread</code>
     * starts as left bobbin, or the <code>backThread</code>.
     */
    Switch(Range range, ThreadSegment front, ThreadSegment back) {
    	threadRange = range;
    	frontThread = front;
    	backThread = back;
        setThreadConnectors(); 
	}
    
    /**
     * Adds the <code>backThread</code> and
     * <code>frontThread</code> <code>ThreadSegment</code>s in the appropriate
     * order to the <code>ThreadConnectors</code>.
     * 
     * For a <code>Cross</code> the <code>frontThread</code> segment starts as the
     * left bobbin, for a <code>Twist</code> the <code>backThread</code>.
     * 
     * For each <code>Cross</code> and <code>Twist</code> two
     * <code>ThreadSegment</code>s are drawn. The end points of one
     * <code>Cross</code> and <code>Twist</code> are the start points of the next.
     * While constructing a pattern cross by twist in the same order as a lace
     * maker performs these actions, these segments are connected, eliminating
     * gaps and giving the whole thread a single style (color/width).
     */
    abstract void setThreadConnectors();

    /**
     * Gets the numbers of the thread/bobbin positions involved in the
     * cross/twist.
     * 
     * @return range of positions.
     */
    Range getThreadRange() {
        return threadRange;
    }

    /**
     * Gets the segment of the thread lying behind the other thread.
     * 
     * @return the thread segment in the background.
     */
    public ThreadSegment getBack() {
        return backThread;
    }

    /** Gets the segment of the thread lying on top of the other thread. */
    /**
     * @return the thread segment in frontThread of the other one.
     */
    public ThreadSegment getFront() {
        return frontThread;
    }

    @Override
	final Iterator<Drawable> threadIterator () {
		CubicCurve2D backCurve = backThread.getCurve();
		CubicCurve2D frontCurve = frontThread.getCurve();
		ThreadStyle backStyle = backThread.getStyle();
		ThreadStyle frontStyle = frontThread.getStyle();
		Drawable[] a = {//
				new Drawable(backCurve, backStyle.getShadow()),
				new Drawable(backCurve, backStyle),
				new Drawable(frontCurve, frontStyle.getShadow()),
				new Drawable(frontCurve, frontStyle),
				};
		return Arrays.asList(a).iterator();
	}

	@Override
	final Iterator<Drawable> pairIterator() {
		return new Vector<Drawable>().iterator();
	}
	
	@Override
	final Iterator<Drawable> pinIterator() {
		return new Vector<Drawable>().iterator();
	}
}
