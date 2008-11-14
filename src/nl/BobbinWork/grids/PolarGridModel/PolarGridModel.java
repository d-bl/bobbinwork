/* PolarGridModel.java Copyright 2005-2007 by J. Falkink-Pol
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

import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;
/**
 *
 * @author J. Falkink-Pol
 */
public class PolarGridModel {
    
    public static final int MIN_ANGLE = 10;
    public static final int MAX_ANGLE = 80;
    public static final int MIN_REPEATS = 2;
    public static final int MAX_REPEATS = 200; // otherwise out of memory
    public static final int MIN_DOTS_PER_REPEAT = 2;
    public static final int MAX_DOTS_PER_REPEAT = 200; // otherwise out of memory
    public static final double MIN_DIAMETER = 0.4D;
    public static final double SCALE_MM = 2.83D; // mm on paper
    
    private int numberOfRepeats = 5;
    private int angleOnFootside = 45; // degrees
    private InnerGridCircle innerGridCircle;
    private GridBoundary outerGridCircle;// = (GridBoundary) innerGridCircle.getNext();
    
    
    /** Gets the number of repeats or circle segments. */
    public int getNumberOfRepeats(){
        return numberOfRepeats;
    }
    /** See getNumberOfRepeats. The actual value is made positive with a minimum defined by a constant. */
    public void setNumberOfRepeats(int numberOfRepeats) {
        this.numberOfRepeats = Math.max(Math.abs(numberOfRepeats),MIN_REPEATS);
    }
    
    /** Idem. */
    public void setNumberOfRepeats(String numberOfRepeats) {
        int i = Integer.valueOf(numberOfRepeats).intValue();
        this.setNumberOfRepeats(i);
    }
    
    /** Gets the number of dots per repeat in the inside boundary of the grid. */
    public int getDotsPerRepeat() {
        return this.innerGridCircle.getDotsPerRepeat();
    }
    /** Sets the number of dots per repeat in the inside boundary of the grid.
     * May reset the leg ratios of subsequent DensityChange objects.
     */
    public void setDotsPerRepeat(int dotsPerRepeat) {
        this.innerGridCircle.setDotsPerRepeat(dotsPerRepeat);
    }
    /** Idem. */
    public void setDotsPerRepeat(String dotsPerRepeat) {
        int i = Integer.valueOf(dotsPerRepeat).intValue();
        this.innerGridCircle.setDotsPerRepeat(i);
    }
    
    /** Gets the inside boundary of the grid. */
    public ConcentricCircle getInnerCircle() {
        return this.innerGridCircle;
    }
    /** Gets the outside boundary of the grid. */
    public ConcentricCircle getOuterCircle() {
        return this.outerGridCircle;
    }
    
    /** Gets the angle between diagonals and circles.
     * Dots are placed on alternating positions per circle.
     * Drawing lines through the dots gives a figure like below:
     * <pre>
     * ________
     *  \ /\ /
     * __V__V__
     * </pre>
     * The angle between the outside of the V and the inner circle is given.
     */
    public int getAngleOnFootside() {
        return angleOnFootside;
    }
    /** See getAngleOnFootside.
     * The actual value is kept safely between 0 and 90 degrees.
     * The safe minium and maximum values are defined by constants.
     * Unsafe values are of no practical use for bobbin lace makers
     * and could stress the system too much.
     */
    public void setAngleOnFootside(int angleOnFootside) {
        this.angleOnFootside = Math.min(Math.max(Math.abs(angleOnFootside),MIN_ANGLE),MAX_ANGLE);
    }
    /** Idem. */
    public void setAngleOnFootside(String angleOnFootside) {
        int i = Integer.valueOf(angleOnFootside).intValue();
        this.setAngleOnFootside(i);
    }
    
    /** Creates lines from the center of the grid to the first dot of each repeat. */
    public Line2D[] getRepeats() {
        
        Line2D result[] = new Line2D[ this.getNumberOfRepeats() ];
        
        double aRadians = Math.toRadians( 360D / this.getNumberOfRepeats() );
        double radius = ( this.getInnerCircle().getDiameter() / 2 ) * SCALE_MM;
        
        for ( int i=0 ; i<this.getNumberOfRepeats() ; i++ ){
            result[i] = new Line2D.Double( 0, 0, Math.cos(aRadians*i)*radius, Math.sin(aRadians*i)*radius);
        }
        return result;
    }
    
    /** Counts the number of objects in the chain of concentric circles. */
    private int countCircles() {
        int count = 1;
        ConcentricCircle p = this.getInnerCircle();
        while ( p.nextAvailable() ) {
            count++;
            p = (ConcentricCircle) p.getNext();
        }
        return count;
    }
    
    /** Gets the distance between circles of dots.
     *
     * @param diameter can be incremented with the result to get subsequent circles for dots
     * @result         half the distance between two dots on a circle
     */
    private double getDistance(double diameter, int dotsPerCircle) {
        
        double x = (diameter * Math.PI) / (double)dotsPerCircle;
        return x * Math.tan( Math.toRadians( this.getAngleOnFootside() ) );
    }
    
    /** Gets arcs stretching out along one repeat, indicating the boundaries and density changes. */
    public Arc2D[] getBoundaries() {
        
        int count = this.countCircles();
        Arc2D[] result = new Arc2D[count];
        
        double aDegrees = 360D / this.getNumberOfRepeats();
        
        ConcentricCircle p = this.getInnerCircle();
        for ( int i=0 ; i<count ; i++ ) {
            double diameter = p.getDiameter() * SCALE_MM;
            result[i] = new Arc2D.Double(0-(diameter/2), 0-(diameter/2), diameter, diameter, 0, -aDegrees, Arc2D.OPEN);
            p = (ConcentricCircle) p.getNext();
        }
        return result;
    }
    
    /** Gets the co-ordinates for all dots of the grid. */
    public Point2D[] getAllDots() {
        
        Vector<Point2D.Double> v = new Vector<Point2D.Double>();
        
        double aRadians = Math.toRadians( 360D / this.getNumberOfRepeats() );
        
        // incremented variables
        double diameter = this.getInnerCircle().getDiameter()*SCALE_MM;
        double oldDistance = 0; // slowly growing increment for the diameter, reduced at DividingGridRings
        int circleNr = 0; // on even circles the dots are rotated half a distance
        
        DoubleChainedObject p = this.getInnerCircle();
        for  ( ; p.nextAvailable() ; p = p.getNext() ) {
            
            int dotsPerCircle = ((Dots)p).getDotsPerRepeat() * this.getNumberOfRepeats();
            
            // many ways to position the first circle of dots with a higher density
            if ( oldDistance != 0 ) {
                DensityChange dc = (DensityChange) p;
                if ( ! dc.getAlternate() ) {
                    circleNr =  1 - (circleNr % 2);
                }
                double newDistance = this.getDistance(diameter, dotsPerCircle);
                switch ( dc.getDistanceOption() ) {
                    case DensityChange.DISTANCE_OLD:           break;
                    case DensityChange.DISTANCE_ZERO:          diameter -= oldDistance;                             break;
                    case DensityChange.DISTANCE_NEW:           diameter += newDistance-oldDistance;                 break;
                    case DensityChange.DISTANCE_AVG_OLD_NEW:   diameter += (newDistance+oldDistance)/2-oldDistance; break;
                }
            }
            
            // circles of dots up to the next PolarGridRing
            double maxDiameter = ((ConcentricCircle)p.getNext()).getDiameter() * SCALE_MM;
            aRadians = Math.toRadians( 360D / (double) dotsPerCircle );
            do {
                // create the dots on one circle
                for ( int dotNr = 0 ; dotNr < dotsPerCircle ; dotNr++ ) {
                    double a = ( dotNr + ((circleNr%2)*0.5D) ) * aRadians;
                    v.add(new Point2D.Double( (diameter/2D)*Math.cos(a), (diameter/2D)*Math.sin(a) ));
                }
                // increment
                diameter += oldDistance = this.getDistance(diameter, dotsPerCircle);
                circleNr++;
            } while (  diameter < maxDiameter );
        }
        Point2D[] result = new Point2D[ v.size() ];
        System.arraycopy( v.toArray(), 0, result, 0, v.size() );
        return result;
    }
    
    /**
     * Creates a new instance of PolarGridModel with default properties. 
     */
    public PolarGridModel() {
        this.innerGridCircle = new InnerGridCircle(30D, 100D, 6);
        this.outerGridCircle = (GridBoundary) this.getInnerCircle().getNext();
        new DensityChange( this.getInnerCircle() );
    }
    
}
