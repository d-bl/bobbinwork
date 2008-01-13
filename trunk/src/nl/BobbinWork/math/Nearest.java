/* Nearest.java Copyright 2005-2007 by J. Falkink-Pol
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
package nl.BobbinWork.math;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

public class Nearest {
    
    private Nearest(){
        // only static methods so hide constructor
    }
    
    /**
     * @param mouse coordinates for the mouse position
     * @param front thread segment that lies in front of the other
     * @param frontWidth width of the line drawn as front thread
     * @param back thread segment that lies in behind the other
     * @return the thread segment with the shortest distance to the mouse
     */
    public static CubicCurve2D nearest(Point2D mouse, CubicCurve2D front, int frontWidth, CubicCurve2D back, int backWidth){
    	//TODO compute which line is really nearest
        return front;
    }

}
