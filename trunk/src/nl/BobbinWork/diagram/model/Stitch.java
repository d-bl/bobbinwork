/* Stitch.java Copyright 2006-2007 by J. Falkink-Pol
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

import nl.BobbinWork.diagram.xml.ElementType;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author J. Falkink-Pol
 */
public class Stitch extends MultiplePairsPartition {

    /**
     * @param element XML element &lt;stitch%gt;
     */
    public Stitch(Element element) {
        super(element);
        setPairRange(new Range(element));

        Style style = new Style();

        // allocate arrays for pairEnds and threadEnds TODO: -> vector for new
        // pairs
        int pairCount = getPairRange().getCount();
        Segment[] pairSegments = new Segment[pairCount];
        setThreadEnds(new Ends(pairCount * 2));

        int pairIndex = 0;
        for //
        (Node child = element.getFirstChild() //
        ; child != null //
        ; child = child.getNextSibling() //
        ) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                ElementType childType = ElementType.valueOf(child.getNodeName());
                if ( childType == ElementType.cross ) {
                    addChild(Builder.createCross((Element) child));
                } else if ( childType == ElementType.twist ) {
                    addChild(Builder.createTwist((Element) child));
                } else if ( childType == ElementType.pin ) {
                    getPartitions().add(new Pin((Element) child));
                } else if ( childType == ElementType.style ) {
                    style = new Style((Element) child);
                } else if ( childType == ElementType.pair ) {
                    if (pairIndex < pairSegments.length) {
                        pairSegments[pairIndex++] = Builder.createPairSegment((Element) child);
                    } else {
                        throw new RuntimeException("trying to define pair " +pairIndex+" while only "+pairSegments.length + " declared");
                    }
                }
            }
        }
        setPairEnds(new Ends(pairSegments));
        for (Segment segment:pairSegments) {
            if (segment!=null){
                segment.style = style;
            }
        }
    }

    private void addChild(Switch newSwitch) {
        getPartitions().add(newSwitch);
        int start = newSwitch.getThreadRange().getFirst() - 1;
        getThreadEnds().connect(newSwitch.getThreadEnds(), start);
    }

    public void draw(java.awt.Graphics2D g2, boolean pair, boolean thread) {
        if (pair) {
            Segment v[] = getPairEnds().getIns();
            for (int i = 0; i < v.length; i++) {
                if (v[i]!=null) {
                    v[i].draw(g2);
                }
            }
        }
        super.draw(g2,pair,thread);
    }
}
