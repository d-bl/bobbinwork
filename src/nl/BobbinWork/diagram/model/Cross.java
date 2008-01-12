/* Cross.java Copyright 2006-2007 by J. Falkink-Pol
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
 * A bobbin going over the next bobbin.
 * 
 * Having two bobbins in each hand usually the right bobbin in the left hand
 * goes over the left bobbin in the right hand.
 * 
 * @author J. Falkink-Pol
 */
public class Cross extends Switch {

    /**
     * @see nl.BobbinWork.diagram.model.Switch#Switch(Element) super
     * @param element
     *            XML element <code>&lt;cross&gt;</code>
     */
    public Cross(Element element) {
        super(element);
    }

    void setThreadEnds() {
        Segment[] ins = { getFront(), getBack() };
        super.setThreadEnds(new Ends(ins));
    }

}
