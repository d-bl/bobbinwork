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

import static nl.BobbinWork.diagram.xml.ElementType.front;
import static nl.BobbinWork.diagram.xml.ElementType.back;
import nl.BobbinWork.diagram.xml.ElementType;

import org.w3c.dom.Element;

/**
 * One bobbin going over one of its neighbours.
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
     * The difference beteen the two is made by the abstract method
     * <code>setThreadEnds</code> determining wether the <code>frontThread</code>
     * starts as left bobbin, or the <code>backThread</code>.
     * 
     * @param element
     *            an XML <code>&lt;cross&gt;</code> or
     *            <code>&lt;twist&gt;</code> element:
     * 
     * <code><br>
     *  &lt;... bobbins=&quot;<em>first</em>-<em>last</em>&quot;&gt;<br>
     *  &nbsp;&nbsp;&lt;back ... /&gt;<br>
     *  &nbsp;&nbsp;&lt;front ... /&gt;<br>
     *  &lt;...&gt;<br>
     * </code> The order of the <code>front</code> and <code>back</code>
     * element doesn't matter.
     */
    public Switch(Element element) {
        super(element);
        threadRange = new Range(element);
        frontThread = createSegment(element,front);
        backThread = createSegment(element,back);
        setThreadEnds(); // TODO: catch NullPointerException throw XML validation error
    }
    private ThreadSegment createSegment (Element element, ElementType tag) {
        return new ThreadSegment((Element) element.getElementsByTagName(tag.toString()).item(0));
    }

    /**
     * Adds the <code>backThread</code> and
     * <code>frontThread</code> <code>ThreadSegment</code>s in the appropriate
     * order to the <code>ThreadEnds</code>.
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
    abstract void setThreadEnds();

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

    public void draw(java.awt.Graphics2D g2, boolean pair, boolean thread) {
        if (thread) {
            backThread.draw(g2);
            frontThread.draw(g2);
        }
    }
}
