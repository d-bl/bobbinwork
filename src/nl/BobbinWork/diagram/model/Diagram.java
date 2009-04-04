/* Diagram.java Copyright 2006-2007 by J. Falkink-Pol
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
 * @author J. Falkink-Pol
 * 
 */
public class Diagram extends ChainedPairsPartition {

    public Diagram(//
    		List<MultiplePairsPartition> parts, //
    		List<Pin> pins) {
    	super (new Range(0, 0), parts, pins);
    }
    void connectChild(MultiplePairsPartition part) {}
    void initEnds() {}
}
