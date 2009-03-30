/* Group.java Copyright 2006-2007 by J. Falkink-Pol
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

/**
 * @author J. Falkink-Pol
 * 
 */
public class Group extends ChainedPairsPartition {

    public Group(//
    		Range range, //
    		Vector<MultiplePairsPartition> parts,//
    		Vector<Pin> pins) {
    	super (range, parts, pins);
    }
    
    void initEnds() {
    	int l = getPairRange().getCount();
    	setPairEnds(new Ends(l));
    	setThreadEnds(new Ends(l * 2));
    }
    
	void connectChild(MultiplePairsPartition part) {
		int first = part.getPairRange().getFirst() - 1;
        getPairEnds().connect(part.getPairEnds(), first);
        getThreadEnds().connect(part.getThreadEnds(), first * 2);
	}
}
