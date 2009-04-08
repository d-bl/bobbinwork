/* Point.java Copyright 2006-2007 by J. Falkink-Pol
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
 *
 * @author J. Falkink-Pol
 */
@SuppressWarnings("serial")
public
class Point extends java.awt.geom.Point2D.Double { 
    
    private static final String SEPARATOR = ",";
    
    /** Creates a new instance of Point. 
     * 
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** Creates a new instance of Point, by splitting the string in an x and y component.
     * 
     * @param s 
     */
    public Point(String s) {
        
        String xy [] = s.split(SEPARATOR);
        try {
            x = java.lang.Double.valueOf(xy[0]).doubleValue();
            y = java.lang.Double.valueOf(xy[1]).doubleValue();
        } catch (java.lang.NumberFormatException e){ }
    }

    /** Creates a new instance of Point. 
     * 
     * @param p the point to clone.
     */
    public Point(Point p) {
        x = p.x;
        y = p.y;
    }
}
