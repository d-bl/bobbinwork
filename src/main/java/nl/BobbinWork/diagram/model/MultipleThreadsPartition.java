/* MultipleThreadsPartition.java Copyright 2006-2007 by J. Pol
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

import java.util.List;


/**
 * Diagram section containing multiple threads segments.
 * 
 * 
 * @author J. Pol
 */
public abstract class MultipleThreadsPartition extends Partition {

    /** Threads going into and coming out of a Group/Stitch/Cross/Twist. */
    private Connectors<ThreadSegment> threadConnectors;

    MultipleThreadsPartition(){}
    
    Connectors<ThreadSegment> getThreadConnectors() {
        return threadConnectors;
    }

    /**
     * Puts thread segments in the ins and outs. The constructor of the subclasses
     * Cross and Twist offer the back and front segments in the correct order for
     * the ins. The Connectors class sets the outs in the the reversed order.
     * 
     * @param threadConnectors
     */
    void setThreadConnectors(Connectors<ThreadSegment> threadConnectors) {
        this.threadConnectors = threadConnectors;
    }

    public Bounds<ThreadSegment> getBounds(){
        if (threadConnectors==null) return null;
        return threadConnectors.getBounds();
        
    }
    
    public int getNrOfPairs() {
        final List<ThreadSegment> ins = threadConnectors.getIns();
        if (ins == null)return 0;
        return ins.size();
    }
}
