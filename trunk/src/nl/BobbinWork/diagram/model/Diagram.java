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

import org.w3c.dom.Element;

/**
 * @author J. Falkink-Pol
 * 
 */
public class Diagram extends ChainedPairsPartition {

    /**
     * Creates a new tree of Partition's.
     * 
     * @param element
     */
    public Diagram(Element element) {
        super(element);
    }

    /** Called by super constructor. */
    void setPairRange(Element element) {
        setPairRange(new Range(0, 0));
    }

    /** Called by super constructor. */
    void addChild(MultiplePairsPartition child) {
        getPartitions().add(child);
        // TODO init thread/pair-ends
    }
}
