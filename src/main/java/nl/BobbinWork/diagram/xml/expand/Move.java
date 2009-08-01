/* Move.java Copyright 2006-2007 by J. Pol
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

import org.w3c.dom.NamedNodeMap;

/**
 * 
 * @author J. Pol
 */
class Move extends VectorTransformation {

    double mx, my;

    public Move(NamedNodeMap attrs) {
        try {
            String xy[] = attrs.getNamedItem("shift").getNodeValue().split(",");
            mx = Double.valueOf(xy[0]).doubleValue();
            my = Double.valueOf(xy[1]).doubleValue();
        } catch (Throwable e) {
            mx = getNumber(attrs, "x");
            my = getNumber(attrs, "y");
        }
    }

    private Double getNumber(NamedNodeMap attrs, String attrTag) {
        String s;
        try {
            s = attrs.getNamedItem(attrTag).getNodeValue();
            return Double.valueOf(s).doubleValue();
        } catch (NullPointerException e) {
            return 0d;
        } catch (NumberFormatException e) {
            throw new RuntimeException("invalid number\n<move " + attrs.getNamedItem(attrTag) + " ...>");
        }
    }

    public String newXY(org.w3c.dom.Attr point) {

        super.newXY(point);
        return (x + mx) + "," + (y + my);
    }
}
