/* BundleMenu.java Copyright 2005-2007 by J. Falkink-Pol
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

import java.util.ResourceBundle;

import javax.swing.JMenu;

/**
 * @author J. Falkink-Pol
 *
 */
@SuppressWarnings("serial")
public class BundleMenu extends JMenu {

    /**
     * @param bundle   resource bundle
     * @param property property key for caption, key prefix for optional underline and/or tootip 
     */
    public BundleMenu(ResourceBundle bundle, String property) {
        super(bundle.getString(property));
        try {
            setMnemonic(bundle.getString(property + "_underline").toUpperCase().charAt(0)); //$NON-NLS-1$
        } catch (Throwable e) {
            try {
                setMnemonic(bundle.getString(property + "_Underline").toUpperCase().charAt(0)); //$NON-NLS-1$
            } catch (Throwable e2) {
            }
        }
        String hint = null;
        try {
            hint = bundle.getString(property + "_hint"); //$NON-NLS-1$
        } catch (Throwable e) {
            try {
                hint = bundle.getString(property + "_Hint"); //$NON-NLS-1$
            } catch (Throwable e2) {
            }
        }
        if (hint != null ) {
            setToolTipText("<html><body>"+hint.replaceAll("\n","<br>")+"</body></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
    }


}
