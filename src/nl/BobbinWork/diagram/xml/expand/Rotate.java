/* Rotate.java Copyright 2006-2007 by J. Falkink-Pol
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

/**
 *
 * @author J. Falkink-Pol
 */
class Rotate extends VectorTransformation {
    
    double rx, ry, sin, cos, angleDeg;
    
    public Rotate(org.w3c.dom.NamedNodeMap attrs) {
        
        String sangle = attrs.getNamedItem("angle").getNodeValue();
        String[] xy   = attrs.getNamedItem("centre").getNodeValue().split(",");
        
        try {
            rx = Double.valueOf(xy[0]).doubleValue();
            ry = Double.valueOf(xy[1]).doubleValue();
            angleDeg = Double.valueOf(sangle).doubleValue();
        } catch (NumberFormatException e) {
            throw new RuntimeException ("invalid number\n<rotate "+attrs.getNamedItem("centre")+" "+attrs.getNamedItem("angle")+">");
        }        
        while (angleDeg > 360) { angleDeg -= 360;}
        while (angleDeg < 0)   { angleDeg += 360;}
        double angleRad = ( angleDeg * java.lang.Math.PI ) /180;
        sin      = java.lang.Math.sin(angleRad);
        cos      = java.lang.Math.cos(angleRad);

        // for nice rounded values
        if ( (angleDeg==90) || (angleDeg==270) ) {
            cos = 0;
        } else if ( (angleDeg==180) || (angleDeg==0) || (angleDeg==360) ) {
            sin = 0;
        }
    }
    
    public String newXY(org.w3c.dom.Attr point) {
        
        super.newXY(point);
        
        double dx = x - rx;
        double dy = y - ry;
        
        return  ( rx + (dx*cos) - (dy*sin) )
        + "," + ( ry + (dx*sin) + (dy*cos) );
    }
    public static void main(String[] args) throws java.io.IOException {
        double angleRad;
        angleRad = ( 45 * java.lang.Math.PI ) /180;
        System.out.println(java.lang.Math.sin(angleRad)+" "+java.lang.Math.cos(angleRad));
        angleRad = ( 90 * java.lang.Math.PI ) /180;
        System.out.println(java.lang.Math.sin(angleRad)+" "+java.lang.Math.cos(angleRad));
        angleRad = ( 180 * java.lang.Math.PI ) /180;
        System.out.println(java.lang.Math.sin(angleRad)+" "+java.lang.Math.cos(angleRad));
        angleRad = ( 270 * java.lang.Math.PI ) /180;
        System.out.println(java.lang.Math.sin(angleRad)+" "+java.lang.Math.cos(angleRad));
        
    }

}
