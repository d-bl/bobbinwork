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

    public Stitch(Range range, List<PairSegment> pairs, List<Switch> switches,
    		List<Pin> pins, String title) {
        super(title);
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

 // @Override
  final Iterator<Drawable> threadIterator()
  {
    // segments are added in reversed order to allow twisted pico's
    final DrawableList drawables = new DrawableList();
    for (int i=getPartitions().size()-1;i>=0;i--){
      Partition partition = getPartitions().get( i );
      if (partition instanceof Switch) {
        drawables.addThreadSegments( ((Switch)partition).getBacks() );
      } else if (partition instanceof Pin) {
        drawables.addPins( (Pin)partition );
      }
    }
    for (int i=0; i<getPartitions().size();i++){
      Partition partition = getPartitions().get( i );
      if (partition instanceof Switch) {
        drawables.addThreadSegments( ((Switch)partition).getFronts() );
      }
    }
    return drawables.iterator();
  }

}
