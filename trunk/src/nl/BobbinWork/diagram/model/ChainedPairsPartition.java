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

import nl.BobbinWork.diagram.xml.ElementType;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author J. Falkink-Pol
 */
abstract class ChainedPairsPartition extends MultiplePairsPartition {

    abstract void setPairRange(org.w3c.dom.Element element);

    /**
     * Adds a new child to the list and connects the thread/pair Ends's.
     * 
     * @param child
     *            a group or stitch that should become part of the group/pattern
     */
    abstract void addChild(MultiplePairsPartition child);

    /**
     * Creates a new (section of the) tree of Partition's.
     * 
     * @param element
     *            XML element &lt;pattern&gt; or &lt;group&gt;
     */
    ChainedPairsPartition(org.w3c.dom.Element element) {
        super(element);
        Vector<ThreadStyle> bobbins = new Vector<ThreadStyle>();
        setPairRange(element);
        for //
        (Node child1 = element.getFirstChild() //
        ; child1 != null //
        ; child1 = child1.getNextSibling()) //
        {
            Node child = child1;
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                ElementType childType = ElementType.valueOf(child.getNodeName());
                if (childType == ElementType.new_bobbins) {
                    ThreadStyle p = new ThreadStyle((Element) child.getFirstChild());
                    String nrs[] = ((Element) child).getAttribute("nrs").split(",");
                    for (String nr:nrs) {
                        // lacemakers start counting with one,
                        // indexes with zero, so subtract one
                        
                        try {
                            int i = Integer.decode(nr).intValue() - 1;
                            bobbins.setSize(Math.max(bobbins.size(), i+1));
                            bobbins.set(i, p);
                        } catch (NumberFormatException e) {
                            throw new RuntimeException ("invalid number:\n<"+child.getNodeName()+" "+//
                                    child.getAttributes().getNamedItem("nrs")+">"); 
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new RuntimeException ("invalid number:\n<"+child.getNodeName()+" "+//
                                    child.getAttributes().getNamedItem("nrs")+">"); 
                        }                        
                    }
                } else if (childType == ElementType.group) {
                    addChild(new Group((Element) child));
                } else if (childType == ElementType.stitch) {
                    addChild(new Stitch((Element) child));
                } else if (childType == ElementType.pin) {
                    getPartitions().add(new Pin((Element) child));
                }
            }
        }
        
        // apply the collected thread styles to the starts of the thread segments
        for (int i = 0; i < bobbins.size(); i++) {
            if (bobbins.get(i) != null) {
                ThreadStyle bobbin = bobbins.get(i);
                ThreadStyle p2 = (ThreadStyle) getThreadEnds().getIns()[i].style;
                p2.setColor(bobbin.getColor());
                p2.setWidth(bobbin.getWidth());
                if (bobbin.getBackGround() != null) {
                    p2.getBackGround().setColor(bobbin.getBackGround().getColor());
                    p2.getBackGround().setWidth(bobbin.getBackGround().getWidth());
                }
            }
        }
    }

}
