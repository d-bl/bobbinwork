/* BWdom.java Copyright 2006-2007 by J. Falkink-Pol
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

package nl.BobbinWork.diagram.xml.expand;

/**
 * 
 * 
 * @author J. Falkink-Pol
 */

import nl.BobbinWork.diagram.xml.ElementType;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TreeExpander {

    /** Only static methods, so hide the constructor. */
    private TreeExpander() {
    }

    public static final String //
            DOM_TO_VIEW = "view", //
            CLONE_TO_ORPHANE = "original", //
            ORPHANE_TO_CLONE = "copy", //
            CLONED = "cloned";

    /** Apply one transformation to the element and its offspring. */
    static private void applyTransformation(VectorTransformation vt, Element el, Object view) {

        el.setUserData(DOM_TO_VIEW, view, null);

        String pointAttributes[] = ElementType.valueOf(el.getNodeName()).getPointAttributes();
        if (pointAttributes != null) {
            for (String tag : pointAttributes) {
                Attr point = el.getAttributeNode(tag);
                try {
                    point.setValue(vt.newXY(point));
                } catch (NullPointerException e) {
                } catch (NumberFormatException e) {
                    throw new RuntimeException("illegal coordinates:\n<" //
                            + el.getNodeName() + " ... " + tag //
                            + "='" + point.getValue() + "' ...>");
                }
            }
        } else {
            for (Node child = el.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    applyTransformation(vt, (Element) child, view);
                }
            }
        }
    }

    /** Find <... id="..."> */
    static private Element findElementById(String id, Element el) {

        for (Node child = el.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childEl = (Element) child;

                try {
                    if (childEl.getAttributeNode("id").getNodeValue().equals(id)) {
                        return childEl;
                    }
                } catch (NullPointerException e) {
                }
                Element x = findElementById(id, childEl);
                if (x != null) {
                    return x;
                }
            }
        }
        return null;
    }

    /**
     * Replace &lt;copy lib="x"&gt; by a clone of &lt;... id="x"&gt;. Apply the
     * children of the &lt;copy&gt; to the clone. Provide the cloned ID's with a
     * prefix to make it unique.
     * 
     * @param el
     * @return exploded tree
     * @throws NullPointerException
     */
    // TODO: not all ID's are yet unique, recompute pairs, etc.
    static private Element expand(Element el, int idPrefix) throws NullPointerException {

        // find the root
        // el.getParentNode().getParentNode().getOwnerDocument()DocumentElement();
        // is original document
        Element root = el;
        while ((root != null) && (root.getNodeName().equals(ElementType.diagram))) {
            root = (Element) root.getParentNode();
        }
        root = (Element) el.getOwnerDocument().getDocumentElement();

        // search starting at the root
        String id;
        try {
            id = el.getAttributes().getNamedItem("of").getNodeValue();
        } catch (NullPointerException e) {
            throw new RuntimeException("<copy of='...'> lacks 'of'");
        }
        Node n = findElementById(id, root);

        if (n == null) {
            throw new RuntimeException("<copy " + el.getAttributes().getNamedItem("of")
                    + " ...> refers to nonexistent <... id='" + id + "'>");
        }

        n.setUserData(CLONED, Boolean.valueOf(true), null);
        Node clone = n.cloneNode(true);

        // give the clone another id
        Node att = clone.getAttributes().getNamedItem("id");
        try {
            att.setNodeValue(el.getAttributes().getNamedItem("id").getNodeValue());
        } catch (java.lang.NullPointerException e) {
            att.setNodeValue(idPrefix + "-" + att.getNodeValue());
        }

        // give the clone the pairs/bobbins of the replaced node
        String rangeTag = ElementType.getRangeAttribute(el.getNodeName());
        if (rangeTag != null) {
            att = clone.getAttributes().getNamedItem(rangeTag);
            try {
                att.setNodeValue(el.getAttributes().getNamedItem(rangeTag).getNodeValue());
            } catch (java.lang.NullPointerException e) {
            }
        }
        clone.setUserData(DOM_TO_VIEW, el.getUserData(DOM_TO_VIEW), null);

        // apply transformations
        for (Node trans = el.getFirstChild(); trans != null; trans = trans.getNextSibling()) {
            if (trans.getNodeType() == Node.ELEMENT_NODE) {

                VectorTransformation vt = null;
                String transName = trans.getNodeName();
                if (transName.equals("move")) {
                    vt = new Move(trans.getAttributes());
                } else if (transName.equals("rotate")) {
                    vt = new Rotate(trans.getAttributes());
                }
                if (vt != null) {
                    applyTransformation(vt, (Element) clone, el.getUserData(DOM_TO_VIEW));
                }
            }
        }

        clone.setUserData(CLONE_TO_ORPHANE, el, null);
        el.setUserData(ORPHANE_TO_CLONE, clone, null);
        return (Element) clone;
    }

    public static void parse(Element el) {

        int idPrefix = 100000000;
        Node next = null;

        for (Node child = el.getFirstChild(); child != null; child = next) {
            next = child.getNextSibling();

            if (child.getNodeName().equals(ElementType.copy.toString())) {
                el.replaceChild(expand((Element) child, ++idPrefix), child);
            } else if (child instanceof Element) {
                parse((Element) child); // recursion
            }
        }
    }
}
