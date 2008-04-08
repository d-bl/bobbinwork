/* RatioComboBox.java Copyright 2005-2007 by J. Falkink-Pol
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

package nl.BobbinWork.grids.GridGUI;

import nl.BobbinWork.grids.PolarGridModel.LegRatio;

/**
 *
 * @author J. Falkink-Pol
 */
@SuppressWarnings("serial")
public class RatioComboBox extends javax.swing.JComboBox {
    
    /** Creates a new instance of RatioComboBox */
    public RatioComboBox() {
    }
    // TODO let RatioComboBox use a twistie and/or model
    
    /** Gets the string representation of the selected item.
     * Usefull to create a listener.
     */
    public String getText() {
        return getSelectedItem().toString();
    }
    
    /** Reconstructs the list of choices
     * depending on the dotsPerRepeat of the preceding circle.
     */
    public void setPossibleRatios( int dotsPerRepeat ) {
        
        String savedValue = this.getText();
        
        //TODO workaround: keep 1 item (1:1) to keep the list dropping down
        this.setSelectedIndex(0);
        while ( this.getItemCount()>1 ) {
            this.removeItemAt(this.getItemCount()-1);
        }
        // create the new items
        LegRatio[] x = LegRatio.getList(dotsPerRepeat);
        for ( int i=0 ; i<x.length ; i++ ){
            this.addItem(x[i].getRatio());
        }
        // select the correct item (if still available)
        int c = this.getItemCount();
        for ( int i=0 ; i<c ; i++ ){
            try {
                if ( this.getItemAt(i).toString().compareToIgnoreCase(savedValue) == 0 ) {
                    this.setSelectedIndex(i);
                }
            }catch (java.lang.NullPointerException e) {}
        }
    }
    /** Idem. But convenient to create a listener with java.beans.EventHandler. */
    public void setPossibleRatios( String dotsPerRepeat ) {
        this.setPossibleRatios( Integer.valueOf(dotsPerRepeat).intValue() );
    }
    
}
