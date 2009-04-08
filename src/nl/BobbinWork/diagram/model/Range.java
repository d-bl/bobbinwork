/* Range.java Copyright 2006-2007 by J. Falkink-Pol
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


/**
 * A range of individual bobbins (alias threads) or pairs of bobbins.
 * 
 * The numbers do not stick to the bobbins/pairs, but to the positions taken by
 * the (pairs of) bobbins. That means after each cross/twist/stitch the bobbins
 * are counted again from left to right. <br>
 * <br>
 * Lace makers start counting with one, so in the XML source, numbers also start
 * with one.<br>
 * <br>
 * The numbers are not overall numbers for the whole project, but relative
 * within the ancestor, that is the group of bobbins required for the stitch or
 * motive being described.
 * 
 * @author J. Falkink-Pol
 */
public class Range {

    private int first;

    private int last;

    /**
     * Gets the number of the first (pair of) bobbins.
     * 
     * Count the positions of the (pairs of) bobbins from left to right starting
     * with 1 and relative within the ancester in terms of XML elements.
     * 
     * @return the number of the first (pair of) bobbins.
     */
    int getFirst() {
        return first;
    }

    /**
     * Get the number of the last (pair of) bobbins.
     * 
     * Count the positions of the (pairs of) bobbins from left to right starting
     * with 1 and relative within the ancester in terms of XML elements.
     * 
     * @return the number of the last (pair of) bobbins
     */
    int getLast() {
        return last;
    }

    /**
     * Creates a new instance of Range.
     * 
     * @param first
     *            The first (pair of) bobbins.
     * @param end
     *            The Last (pair of) bobbins.
     */
    public Range(int first, int end) {
        this.first = first;
        this.last = end;
    }

    /**
     * Gets the number of (pairs of) bobbins.
     * 
     * @return The number of (pairs of) bobbins in this <code>Range</code>.
     */
    public int getCount() {
        return last - first + 1;
    }

    public String toString(){
    	return getFirst() + "-" + getLast();
    }
}
