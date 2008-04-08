/* LegRatio.java Copyright 2005-2007 by J. Falkink-Pol
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

import java.util.TreeSet;

/** Constraint for a change in the density of dots on a polair grid.
 *<p>
 * An asymetrical figure like a Torchon spider can be used to increase the density.
 * An exact number of these figures should fit in a repeat.
 *<p>
 *<pre>
 *      ___
 * __  / __
 *   \/ /
 *   / X     1:2, 2:3, 3:4, 3:5, 4:5, 4:7, 5:6, ...
 *  ( (.)
 *   \ X     leftLegs < rightLegs
 * __/\ \__  leftLegs <= 2 * rightLegs (well, most likely)
 *     \___
 *
 *</pre>
 *
 * @author J. Falkink-Pol
 */
public class LegRatio implements Comparable<LegRatio> {
    
    private int left;
    private int right;
    
    /** Gets a string representation of the value like (1:2). */
    public StringBuffer getRatio() {
        StringBuffer result = new StringBuffer();
        result.append(left).append(':').append(right);
        return result;
    }
    
    /** Sets how the number of dots on the left (read: inner) side
     * compare to the number of dots on the right (read: outer) side
     * of the grid.
     *
     * @param left  number of dots on the inner side of the grid
     * @param right number of dots on the outer side of the grid
     */
    public void setRatio(int left, int right) {
        this.left = left;
        this.right = right;
        // TODO: validation
        // getPrivious().getDotsPerRepeat should be devidable by left.
        // next rings might need adjustment too.
    }
    public void setRatio(String s) {
        String x[] = s.split(":",2);
        int left = Integer.valueOf(x[0]).intValue();
        int right = Integer.valueOf(x[1]).intValue();
        setRatio(left,right);
    }
    
    /** Gets the value to compute the change in number of dots.
     * @return (inner number of dots) * value = (outer number of dots)
     */
    public double getValue() {
        return (double)right / (double)left;
    }
    
    /** Compares the <code>getValue</code> results of the object
     * with <code>getValue</code> results of another object.
     */
    public int compareTo(LegRatio a) {
        if ( this.getValue() == a.getValue() ) {
            return 0;
        } else if ( this.getValue() < a.getValue() ) {
            return -1;
        } else {
            return 1;
        }
    }
    
    /**
     * Gets the possible leg ratios of the asymetrical spiders (or other figures) that increase the grid density.
     *
     * @param dotsPerRepeat the number of dots on one circle for one repeat
     * @return              example "(4:5);(3:4);(2:3);(3:5);(4:7);(1:2)"
     *                      <br>= 0.8;0.75;0.6667;0.6;0.57;0.5
     *                      <br>(4:6) is omitted as it is the same as (2:3)
     *                      <br>Pairs will be omitted if <code>dotsPerRepeat</code>
     *                      can't be divided by the first number of the pair.
     *                      The maximum value for each first number is </code>dotsPerRepeat</code>.
     */
    public static StringBuffer getStringList(int dotsPerRepeat) {
        
        StringBuffer result = new StringBuffer(";");
        LegRatio v[] = getList(dotsPerRepeat);
        int max = v.length;
        
        for ( int i=0 ; i < max ; i++ ) {
            result.append( "(" + v[i].getRatio() + ");" );
        }
        result.deleteCharAt(result.length()-1); // strip the last ";"
        result.deleteCharAt(0); // ... and first
        return result;
    }
    
    /**
     * Gets possible leg ratios.
     *
     * @param dotsPerRepeat the number of dots on one circle for one repeat
     * @return same as <code>getStringList</code> but as an array of LegRatios.
     */
    public static LegRatio[] getList(int dotsPerRepeat) {
        
        TreeSet<LegRatio> ratios = new TreeSet<LegRatio>();
        
        for ( int left=1 ; left<dotsPerRepeat ; left++ ) {
            if ( (dotsPerRepeat % left) == 0) { // fit on the repeat
                for ( int right=left+1 ; right<=(2*left) ; right++ ) {
                    ratios.add( new LegRatio(left, right) );
                }
            }
        }
        
        // TODO: replace the arraycopy with a cast
        LegRatio result[] = new LegRatio [ratios.size()];
        System.arraycopy( ratios.toArray(), 0, result, 0, ratios.size() );
        return result;
    }
    
    /** Creates a new instance of LegRatio. */
    public LegRatio(int left, int right) {
        this.left = left;
        this.right = right;
    }
    
}
