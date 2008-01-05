/* JMenuItemAbout.java Copyright 2005-2007 by J. Falkink-Pol
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

import nl.BobbinWork.bwlib.gui.AboutInfo;



/**
 *
 * @author J. Falkink-Pol
 */
public class JMenuItemAbout
        
        extends
        javax.swing.JMenuItem
        
        implements
        java.awt.event.ActionListener {
    
    AboutInfo aboutInfo;
    
    public AboutInfo getAboutInfo() {
        return this.aboutInfo;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        javax.swing.JOptionPane.showMessageDialog
                ( this
                , aboutInfo.getMessage()
                , aboutInfo.getCaption()
                , javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
    }
    
    public JMenuItemAbout( AboutInfo aboutInfo, java.util.ResourceBundle bundle) {
        this.aboutInfo = aboutInfo;
        this.setMnemonic(bundle.getString("MenuHelp_About_Underline").charAt(0));
        this.setText(bundle.getString("MenuHelp_About"));
        this.setToolTipText(bundle.getString("MenuHelp_About_ToolTip"));
        this.addActionListener(this);
    }
    
}
