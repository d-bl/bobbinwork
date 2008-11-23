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
 * @author J. Falkink-Pol
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.xml.ElementType;
import nl.BobbinWork.diagram.xml.XmlResources;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TreeExpander {

    public static final String //
            DOM_TO_VIEW = "view", //
            CLONE_TO_ORPHAN = "original", //
            ORPHANE_TO_CLONE = "copy", //
            CLONED = "cloned";

    /** Only static methods, so hide the constructor. */
    private TreeExpander() {
    }

    /** Apply one transformation to the element and its offspring. */
    static private void applyTransformation(VectorTransformation vt, Element el, Object view) {

        el.setUserData(DOM_TO_VIEW, view, null);
        el.removeAttribute("id");

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

    /**
     * Replaces all &lt;copy of="x"&gt; elements by deep clones of &lt;... id="x"&gt;. 
     * The transformations defined by children of the &lt;copy&gt; elements are applied 
     * to the clone. The ID's of cloned children get a prefix to make them unique.
     * The clones and replaced elements are linked to one another via 
     * setUserData(CLONE_TO_ORPHAN) respective setUserData(ORPHAN_TO_CLONE). 
     * 
     * @param root gets its copy elements replaced.
     * @throws XPathExpressionException
     */
    public static void replaceCopyElements(Element root) throws XPathExpressionException {
      
      int idPrefix = 1000000000;
      Map<String, Element> clonables = 
        newIdMap( XmlResources.evaluate( "//@id", root ) );
      
      NodeList elements = XmlResources.evaluate( "//*[@of]", root );
      for (int i=0 ; i < elements.getLength() ; i++) {
        Element toBeReplaced = (Element) elements.item(i);
        String id = toBeReplaced.getAttribute("of");
        Element toBeCloned = clonables.get(id);
        if ( toBeCloned == null  ) {
          throw new RuntimeException("<copy of='" + id + 
              "' ...> refers to nonexistent <... id='" + id + "'>");
        }
        Element clone = replaceWithClone( toBeReplaced, toBeCloned );
        setRange(toBeReplaced, id, clone);
        List<VectorTransformation> list = getTransformations (toBeReplaced);
        Object userData = toBeReplaced.getUserData(DOM_TO_VIEW);
        clone.setUserData(DOM_TO_VIEW, userData, null);
        for (VectorTransformation vt : list) {
          applyTransformation(vt, clone, userData );
        }
        clone.setAttribute( "id", ++idPrefix + "_" + id );    
        clone.removeAttribute("display");    
      }
    }

    private static List<VectorTransformation> getTransformations (Element toBeReplaced) {
      
      List<VectorTransformation> list = new ArrayList<VectorTransformation>();

      for ( Node node = toBeReplaced.getFirstChild() ; node != null ; node = node.getNextSibling() ) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          switch ( ElementType.valueOf( node.getNodeName() ) ) {
          case move   : list.add(new Move  ( node.getAttributes() )); break;
          case rotate : list.add(new Rotate( node.getAttributes() )); break;
          }
        }
      }
      return list;
    }
    
    /** give the clone the pairs/bobbins of the replaced node */
    private static void setRange(Element toBeReplaced, String id, Element clone) {
      
      String nodeName = toBeReplaced.getNodeName();
      String attributeName = ElementType.getRangeAttribute(nodeName);
      if (attributeName == null) {
        throw new RuntimeException("<... of='" + id + "' ...> only allowed on <copy ...>");
      }
      String value = toBeReplaced.getAttribute(attributeName);
      if ( value == null ) {
        throw new RuntimeException("<"+nodeName+" id='" + id + "' ...> has no " + attributeName);
      }
      clone.setAttribute(attributeName, value);
    }

    /**
     * @param attributes
     * @return a map with the attribute values as key 
     *         and elements containing the attribute as value
     * @throws XPathExpressionException
     */
    private static Map<String, Element> newIdMap(NodeList attributes)
    throws XPathExpressionException {
      
      int length = attributes.getLength();
      int capacity = (int) ( length / 0.75 );
      Map<String,Element> originals = new HashMap<String,Element>( capacity );
      for (int i=0 ; i < length ; i++) {
        Attr attribute = (Attr) attributes.item(i);
        Element attrOwner = attribute.getOwnerElement();
        String id = attribute.getNodeValue();
        originals.put(id, attrOwner);
      }
      return originals;
    }
    
    /**
     * Replace a subtree with a clone of another subtree.
     * Keep the replaced subtree linked with the cloned subtree.
     * 
     * @param toBeReplaced
     * @param toBeCloned
     */
    private static Element replaceWithClone(Element toBeReplaced, Element toBeCloned) {
      
      Element deepClone = (Element) toBeCloned.cloneNode(true);
      
      deepClone.setUserData(CLONED, Boolean.valueOf(true), null);
      deepClone.setUserData(CLONE_TO_ORPHAN, toBeReplaced, null);
      toBeReplaced.setUserData(ORPHANE_TO_CLONE, deepClone, null);
      
      toBeReplaced.getParentNode().replaceChild( deepClone, toBeReplaced);
      return deepClone;
    }
}
