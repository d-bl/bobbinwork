/* Group.java Copyright 2006-2007 by J. Pol
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

import java.util.Iterator;
import java.util.List;

/**
 * @author J. Pol
 * 
 */
public class Group extends ChainedPairsPartition {

    private final String title;
    public Group(
    		Range range, 
    		List<Partition> parts, 
    		List<ThreadStyle> styles, String partitionTitle) //
    {
    	super (range, parts);
    	title = partitionTitle;
    	Iterator<ThreadStyle> st = styles.iterator();
    	Iterator<ThreadSegment> segments = getThreadConnectors().getIns().iterator();
    	while (st.hasNext() && segments.hasNext()) {
    		segments.next().getStyle().apply(st.next());
    	}
    }
    
    public String getCaption()
    {
      if (title != null) {
        return pairRange.toString() + ": " + title;
      } else {
        return super.getCaption();
      }
    }

    void initConnectors() {
    	int count = getPairRange().getCount();
    	setPairConnectors(new Connectors<PairSegment>(count));
    	setThreadConnectors(new Connectors<ThreadSegment>(count * 2));
    }
    
	void connectChild(MultiplePairsPartition part) {
		int first = part.getPairRange().getFirst() - 1;
        getPairConnectors().connect(part.getPairConnectors(), first);
        getThreadConnectors().connect(part.getThreadConnectors(), first * 2);
	}
}
