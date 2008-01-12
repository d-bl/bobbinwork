/* MultipleThreadsPartition.java Copyright 2006-2007 by J. Falkink-Pol
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

import org.w3c.dom.Element;

/**
 * DiagramPanel section containing multiple threads segments.
 * 
 * 
 * @author J. Falkink-Pol
 */
public abstract class MultipleThreadsPartition extends Partition {

    /** Threads going into and coming out of a Group/Stitch/Cross/Twist. */
    private Ends threadEnds;

    /**
     * Create a new tree of MultipleThreadsPartition's.
     * 
     * @param element
     */
    public MultipleThreadsPartition(Element element) {
        super(element);
    }

    Ends getThreadEnds() {
        return threadEnds;
    }

    /**
     * Puts thread segments in the ins and outs. The constructor of the subclasses
     * Cross and Twist offer the back and front segments in the correct order for
     * the ins. The Ends class sets the outs in the the reversed order.
     * 
     * @param threadEnds
     */
    void setThreadEnds(Ends threadEnds) {
        this.threadEnds = threadEnds;
    }

    public Shape getHull(){
        return getThreadEnds().getHull();
        
    }
    
    public int getNrOfPairs() {
        return threadEnds.getIns().length;
    }

}
