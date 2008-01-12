/* InnerGridCircle.java Copyright 2005-2007 by J. Falkink-Pol
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
 * @author J. Falkink-Pol
 */
class InnerGridCircle extends GridBoundary implements Dots {
    
    private int dotsPerRepeat;
    
    /** See PolarGridDef.getDotsPerRepeat(). */
    public int getDotsPerRepeat() {
        return dotsPerRepeat;
    }
    
    /** See PolarGridDef.setDotsPerRepeat(). */
    public void setDotsPerRepeat(int dotsPerRepeat) {
        this.dotsPerRepeat = Math.max(Math.abs(dotsPerRepeat),1);
        if ( this.nextAvailable() ) {
            // TODO: correct LegRatios of subsequent rings
        }
    }
    
    /** Creates new instances of the innermost and outermost grid circles. */
    public InnerGridCircle(double innerDiameter, double outerDiamater, int dotsPerRepeat) {
        super(innerDiameter);
        this.setDotsPerRepeat(dotsPerRepeat);
        new GridBoundary( this );
    }
}
