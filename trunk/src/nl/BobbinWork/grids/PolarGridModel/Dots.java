/* Dots.java Copyright 2005-2007 by J. Falkink-Pol
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
 * Makes the number of dots on repeat of a ring generaly available.
 *
 * @author J. Falkink-Pol
 */
public interface Dots {
    
    /** Computes the dots per repeat from the preceding ring
     * (if there is one) and leg ratio of this ring,
     * The first ring as to know it number of elements itself.
     */
    public int getDotsPerRepeat();
}
