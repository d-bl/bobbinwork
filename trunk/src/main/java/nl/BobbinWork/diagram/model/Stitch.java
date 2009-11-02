/* Stitch.java Copyright 2006-2007 by J. Pol
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
import java.util.Vector;

public class Stitch extends MultiplePairsPartition {

    private final String title;

    public Stitch(Range range, List<PairSegment> pairs, List<Switch> switches,
    		List<Pin> pins, String title) {
		
        this.title = title;
        setPairRange(range);
        int nrOfPairs = pairs.size();
		setThreadConnectors(new Connectors<ThreadSegment>(nrOfPairs * 2));
		setPairConnectors(new Connectors<PairSegment>(pairs));
        
        List<Partition> partitions = getPartitions();
        for (Pin pin:pins) partitions.add(pin);
        for (Switch sw:switches) {
        	partitions.add(sw);
        	int start = sw.getThreadRange().getFirst() - 1;
        	getThreadConnectors().connect(sw.getThreadConnectors(), start);
        }
	}

    @Override
	final Iterator<Drawable> pairIterator () {
		List<Drawable> list = new Vector<Drawable>(4);
		for (PairSegment segment:this.getPairConnectors().getIns()){
			list.add(new Drawable(segment.getCurve(), segment.getStyle()));
			if (segment.hasTwistMark()){
				list.add(new Drawable(segment.getTwistMark(), segment.getStyle()));
			}
		}
		return list.iterator();
	}

  public String getCaption()
  {
    if (title != null) {
      return pairRange.toString() + ": " + title;
    } else {
      return super.getCaption();
    }
  }

}
