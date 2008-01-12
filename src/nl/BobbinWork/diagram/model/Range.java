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

import org.w3c.dom.Element;

import nl.BobbinWork.diagram.xml.ElementType;

/**
 * A range of individual bobbins (alias threads) or pairs of bobbins.
 * 
 * The numbers do not stick to the bobbins/pairs, but to the positions taken by
 * the (pairs of) bobbins. That means after each cross/twist/stitch the bobbins
 * are counted again from left to right. <br>
 * <br>
 * Lacemakers start counting with one, so in the XML source, numbers also start
 * with one.<br>
 * <br>
 * The numbers are not overall numbers for the whole project, but relative
 * within the ancestor, that is the group of bobbins required for the sticth or
 * motiv being described.
 * 
 * @author J. Falkink-Pol
 */
class Range {

    private static final String SEPARATOR = "-";

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
    public int getFirst() {
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
    public int getLast() {
        return last;
    }

    /** Creates a new instance of Range. */
    public Range() {
        first = 0;
        last = 0;
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

    /**
     * Creates a new instance of Range.
     * 
     * @param element
     *            XML element of the form:
     *            <ul>
     *            <li><code>&lt;cross bobbins="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;twist bobbins="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;stitch  pairs="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;group   pairs="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;copy    pairs="<em>first-last</em>"&gt;</code></li>
     *            </ul>
     */
    public Range(Element element) {
        String tag = ElementType.getRangeAttribute(element.getNodeName());
        String value = element.getAttribute(tag);
        try {
            setRange(value);
        } catch (java.lang.NumberFormatException e) {
            invalidRange(element.getNodeName(),tag, value);
        } catch (ArrayIndexOutOfBoundsException e) {
            invalidRange(element.getNodeName(),tag, value);
        }
    }

    private void invalidRange(String elementTag,String attributeTag, String value) {
        throw new RuntimeException("invalid or missing range:\n<"+elementTag+" ... " //
                + attributeTag + "='" + value + "' ...>");
    }

    private void setRange(String s) {

        String xy[] = s.split(SEPARATOR);
        first = java.lang.Integer.valueOf(xy[0]).intValue();
        last = java.lang.Integer.valueOf(xy[1]).intValue();
    }

    /**
     * Duplicates an instance of Range.
     * 
     * @param range
     *            The object to be copied.
     */
    public Range(Range range) {
        this.first = range.first;
        this.last = range.last;
    }

}
