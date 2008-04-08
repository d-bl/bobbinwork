/* BWTree.java Copyright 2006-2008 by J. Falkink-Pol
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

import static javax.swing.KeyStroke.getKeyStroke;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import javax.swing.JMenuItem;


@SuppressWarnings("serial")
public class LocaleMenuItem extends JMenuItem {
	
    /** Convenience JMenu constructor */
	public LocaleMenuItem (String bundleKeyBase, int keyEvent, int mask){
		super();
        applyStrings(this, bundleKeyBase);
        setAccelerator(getKeyStroke(keyEvent, mask));
		
	}
    /** Convenience JMenu constructor */
	public LocaleMenuItem (String bundleKeyBase){
		super();
        applyStrings(this, bundleKeyBase);
		
	}
}
