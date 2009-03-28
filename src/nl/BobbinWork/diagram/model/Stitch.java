/* Stitch.java Copyright 2006-2007 by J. Falkink-Pol
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

import java.util.Vector;

public class Stitch extends MultiplePairsPartition {

    public Stitch(Range range, Vector<Segment> pairs, Vector<Switch> switches,
			Vector<Pin> pins) {
		
        setPairRange(range);
        int nrOfPairs = pairs.size();
		setThreadEnds(new Ends(nrOfPairs * 2));

        Segment[] array = pairs.toArray(new Segment[nrOfPairs]);
		setPairEnds(new Ends(array));
        
        Vector<Partition> partitions = getPartitions();
        for (Pin pin:pins) partitions.add(pin);
        for (Switch sw:switches) {
        	partitions.add(sw);
        	int start = sw.getThreadRange().getFirst() - 1;
        	getThreadEnds().connect(sw.getThreadEnds(), start);
        }
	}

    public void draw(java.awt.Graphics2D g2, boolean pair, boolean thread) {
        if (pair) {
            Segment v[] = getPairEnds().getIns();
            for (int i = 0; i < v.length; i++) {
                if (v[i]!=null) {
                    v[i].draw(g2);
                }
            }
        }
        super.draw(g2,pair,thread);
    }
}
