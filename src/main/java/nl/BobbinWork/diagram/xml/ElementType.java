/* ElementType.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.diagram.xml;

import javax.swing.ImageIcon;

import nl.BobbinWork.diagram.model.Partition;

import org.w3c.dom.Element;

/*
 * Names of XML elements.
 * 
 * @author J. Pol
 * 
 */

public enum ElementType {

    /**
     * A bobbin going over his right neighbour.
     * 
     * @see nl.BobbinWork.diagram.model.Cross
     */
    cross(AttributeType.bobbins),

    /**
     * A bobbin going over his left neighbour.
     * 
     * @see nl.BobbinWork.diagram.model.Twist
     */
    twist(AttributeType.bobbins),

    /**
     * A couple of cross's and/or twists
     * 
     * @see nl.BobbinWork.diagram.model.Stitch
     */
    stitch(AttributeType.pairs),

    /**
     * A group of stitches and/or other groups.
     * 
     * @see nl.BobbinWork.diagram.model.Group
     */
    group(AttributeType.pairs),

    /**
     * Repeat a previously defines (group of) stitches.
     */
    copy(AttributeType.pairs),

    /**
     * Change the orientation of a repeated partition.
     */
    rotate(),

    /**
     * Change the position of a repeated partition.
     */
    move(),

    /**
     * Thread section behind another one, a section spans a single cross or
     * twist.
     * 
     * @see nl.BobbinWork.diagram.model.ThreadSegment
     */
    back(true),

    /**
     * Thread segment in front of another one, a segment spans a single cross or
     * twist.
     * 
     * @see nl.BobbinWork.diagram.model.ThreadSegment
     */
    front(true),

    /**
     * Segment of a pair, a segment spans a single stitch.
     */
    pair(true),

    /**
     * Name or title for a (group of) stitches.
     */
    title(),

    /**
     * Name or title for a (group of) stitches.
     */
    value(),

    /**
     * Position of a pin supporting a stitch or pair.
     */
    pin(new AttributeType[] {AttributeType.position}),

    /**
     * Presentation of a pair segment or the core of a complete thread.
     * 
     * @see nl.BobbinWork.diagram.model.Style
     */
    style(),

    /**
     * Presentation of the "shadow" of a thread.
     * 
     * @see nl.BobbinWork.diagram.model.Style
     */
    shadow(), //

    /**
     * Bobbins added to the lace project.
     */
    new_bobbins(), //

    /**
     * Sequence of thread segments not following the ordinary over-under rules.
     */
    bobbin(), //

    diagram(); //

    /* ========== so far the names of XML elements ========= */

    private AttributeType rangeAttribute = null;

    /**
     * attributes designating points (x/y coordinates) subject to
     * transformations like moves
     */
    private String pointAttributes[] = null;

    private boolean segment = false;

    /** the class for the object to be constructed from the element */
    private Class<Partition> myClass;

    Class<Partition> getMyClass(Element element) {
        return myClass;
    }

    /**
     * Constructs the constant for an Element that has a range attribute.
     * 
     * @param rangeAttribute
     *            "pairs" or "bobbins"
     */
    private ElementType(AttributeType rangeAttribute) {
        this.rangeAttribute = rangeAttribute;
        setIcon();
    }

    /**
     * Constructs the enum constant for an XML element tag that describes a
     * segment.
     * 
     * @see nl.BobbinWork.diagram.model.Segment
     * 
     * @param segment
     *            true if Element has at least the attributes <code>start</code>
     *            and <code>end</code>, and optionally <code>c1</code> and
     *            <code>c1</code>
     * 
     */
    private ElementType(boolean segment) {
        this.segment = segment;
        pointAttributes = new String[] { AttributeType.start.toString(), //
                AttributeType.c1.toString(), //
                AttributeType.c2.toString(), //
                AttributeType.end.toString() };
        setIcon();
    }

    /**
     * Constructs the constant for an Element that one or more attributes
     * designating points involved in transformations like move.
     * 
     * @param attributes
     *            attribute tags of point involved in transformations like move.
     */
    private ElementType(AttributeType attributes[]) {
        pointAttributes = new String[attributes.length];
        int i = 0;
        for (AttributeType at : attributes) {
            pointAttributes[i++] = at.toString();
        }
        setIcon();
    }

    /**
     * Constructs the enum constant for an XML element tag.
     * 
     */
    private ElementType() {
        setIcon();
    }

    private ImageIcon icon;

    private void setIcon() {
    	String f = "elicons/" + this.name() + ".gif";
        java.net.URL iconURL = ElementType.class.getResource(f);
        if (iconURL != null) {
            icon = new ImageIcon(iconURL);
        } else {
            icon = null;
        }

    }

    /**
     * Gets the icon representing an XML element.
     * 
     * @param s
     *            name of the XML element
     * 
     * @return null if <code>s</code> is not a valid
     *         <code>ElementType.name()</code>.
     */
    public static ImageIcon getIcon(String s) {
        return valueOf(s).icon;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Checks if an XML element defines a segment.
     * 
     * <code>Segment</code> elements have the mandatory attributes
     * <code><ul><li>start</li><li>end</li></ul></code> and the optional attributes
     * <code><ul><li>c2</li><li>c2</li></ul></code>
     * 
     * @param s
     *            name of an XML element.
     * 
     * @return true if <code>s</code> equals "pair" or "thread", otherwise
     *         false.
     */
    public static boolean isSegment(String s) {
        return valueOf(s).segment;
    }

    public boolean isSegment() {
        return segment;
    }

    /**
     * gets (some of) the attribute tags for this type of element
     * 
     * @return tags of attributes subject to transformations like move
     */
    public String[] getPointAttributes() {
        return pointAttributes;
    }

    /**
     * Gets the attribute tag for this type of XML element.
     * 
     * @param elementTag
     *            name of an XML element
     * 
     * @return "pairs" for element names stitch, group or copy<br>
     *         "bobbins" for element names cross or twist<br>
     *         otherwise null
     */
    static public String getRangeAttribute(String elementTag) {
        try {
            return ElementType.valueOf(elementTag).rangeAttribute.name();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
