/* GridBoundary.java Copyright 2005-2007 by J. Pol
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

package nl.BobbinWork.grids.PolarGridModel;

/**
 *
 * @author J. Pol
 */
class GridBoundary extends ConcentricCircle {

    /** Ignores an attempt to destroy the first or last circle of a polair grid definition. */
    public void delete () {
        // TODO: throw an error, not just don't refuse
    }
    
    /** </code>PolarGridDef</code> creates the first circle of a polair grid definition. */
    public GridBoundary(double diameter) {
        super(diameter);
    }
    
    /** </code>PolarGridDef</code> creates the last circle of a polair grid definition. */
    public GridBoundary(ConcentricCircle x) {
        super(false,x);
    }
    
}
