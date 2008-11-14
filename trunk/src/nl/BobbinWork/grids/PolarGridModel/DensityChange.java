/* DensityChange.java Copyright 2005-2007 by J. Falkink-Pol
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
 * Defines how and where the density of dot changes.
 *
 * @author J. Falkink-Pol
 */
public class DensityChange extends ConcentricCircle implements Dots {
    
    // TODO in java 1.5: enum type

    /** The first ring of the new density overlaps with the last ring of the old density. */
    public static final int DISTANCE_ZERO = 0;

    /** The distance is computed according to the new grid. */
    public static final int DISTANCE_NEW = 1;

    /** The distance is computed according from the average of the old and new grid. */
    public static final int DISTANCE_AVG_OLD_NEW = 2;
    
    /** The distance is computed according to the old grid. */
    public static final int DISTANCE_OLD = 3;

    private LegRatio legs = new LegRatio(1,2);
    private boolean alternate = true;
    private int distanceOption = DISTANCE_OLD;

    public LegRatio getLegRatio(){
        return legs;
    }
    
    /** See setAlternate. */
    public boolean getAlternate() {
        return this.alternate;
    }
    /** Specifies whether the new density continues alternating the dots or not.
     * The effect may be influenced bij evenness of the preceding number of rings with dots.
     */
    public void setAlternate(boolean alternate) {
        this.alternate = alternate;
    }
    
    /** See setDistanceOption. */
    public int getDistanceOption() {
        return this.distanceOption;
    }
    /** Specifies the distance between the last ring of the old density
     * and the first ring of the new density.
     * For possible values see the defined constants.
     */
    public void setDistanceOption(int distance) {
        // TODO: validation / enumeration?
        this.distanceOption = distance;
    }
    
    /** Gets the total number of dots on a ring for one repeat.
     * The last dot is not counted but counted as the first dot of a next repeat.
     */
    public int getDotsPerRepeat(){
        int dots = ((Dots)this.getPrevious()).getDotsPerRepeat();
        return (int) Math.round( (double) dots *  this.legs.getValue() );
    }
    
    /** Creates a new instance of DensityChange.
     *
     * @param previous the grid ring after which to insert the new one
     */
    public DensityChange(ConcentricCircle previous) {
        super(false, previous);
    }
    
}
