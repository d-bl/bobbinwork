/* HelpMenu.java Copyright 2006-2007 by J. Pol
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

package nl.BobbinWork.bwlib.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A menu with at least an about item.
 */
@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	
    /**
     * @param parent
     * @param details
     * @param simpleCaption
     */
    public HelpMenu(final Component parent, final String years, final String simpleCaption) {

    	this.versionedCaption = "BobbinWork - " + simpleCaption + " " //$NON-NLS-1$ //$NON-NLS-2$
    		+ this.getClass().getPackage().getImplementationVersion();

    	final String description = "<html> "
    		+ versionedCaption + "<br>Copyright  "  //$NON-NLS-1$ 
    		+ years + " "  //$NON-NLS-1$
    		+ "J. Pol" //$NON-NLS-1$   
    		+ "<hr>"  //$NON-NLS-1$ 
    		+ Localizer.getString("MenuHelp_About_License").replaceAll("\\n", "<br>") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		+ "<hr>"  //$NON-NLS-1$ 
    		+ Localizer.getString("MenuHelp_About_URLs").replaceAll("\\n", "<br>") //$NON-NLS-1$ //$NON-NLS-2$
    		+ "</html>"  //$NON-NLS-1$ 
    		;

    	applyStrings(this, "MenuHelp_help"); //$NON-NLS-1$

        JMenuItem//

        jMenuItem = new LocaleMenuItem("MenuHelp_About"); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(parent, description, simpleCaption,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(jMenuItem);
    }

	private String versionedCaption;
	public String getVersionedCaption() {
		return versionedCaption;
	}

}
