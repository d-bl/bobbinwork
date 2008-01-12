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

import org.w3c.dom.Element;

/**
 * @author J. Falkink-Pol
 * 
 */
public class Group extends ChainedPairsPartition {

    /**
     * Creates a new section of the model tree.
     * 
     * @param element
     *            XML group Element, defining a group of Stitch's.
     */
    public Group(Element element) {
        super(element);
    }

    void setPairRange(Element element) {
        setPairRange(new Range(element));

        // allocation as a preparation for the 2nd task of addChild
        int l = getPairRange().getCount();
        setPairEnds(new Ends(l));
        setThreadEnds(new Ends(l * 2));
    }

    void addChild(MultiplePairsPartition child) {
        getPartitions().add(child);
        int first = child.getPairRange().getFirst() - 1;
        getPairEnds().connect(child.getPairEnds(), first);
        getThreadEnds().connect(child.getThreadEnds(), first * 2);
    }

}
