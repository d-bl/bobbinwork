/* LocaleButton.java Copyright 2006-2008 by J. Pol
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
package nl.BobbinWork.viewer.guiUtils;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import javax.swing.*;

/**
 * A Menu with caption, hint etc. (as far as present) loaded from the
 * Localizer's properties file.
 */
@SuppressWarnings("serial")
public class LocaleMenu
    extends JMenu
{
  /**
   * Convenience JMenu constructor
   * 
   * @param enabled
   *          initial enablement of the button
   * @param bundleKeyBase
   *          for the Localizer bundle for caption, hint etc.
   */
  public LocaleMenu(boolean enabled, String bundleKeyBase, JMenuItem... items)
  {
    super();
    applyStrings( this, bundleKeyBase );
    setEnabled( enabled );
    for (JMenuItem item : items) {
      add( item );
    }
  }
}
