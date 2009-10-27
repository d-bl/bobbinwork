/* CPanel.java Copyright 2006-2008 by J. Pol
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

import static java.awt.BorderLayout.*;

import java.awt.*;

import javax.swing.*;

import nl.BobbinWork.bwlib.gui.CMenuBar;

/** A JPanel with a additional convenience constructors. */
@SuppressWarnings("serial")
public class CPanel extends JPanel {

    /** Convenience JPanel constructor */
	public CPanel(Component content, Component... buttonsAndMenus) {
        super(new BorderLayout());
        add(new CButtonBar(buttonsAndMenus), PAGE_START);
        add(content, CENTER);
    }

    /** Convenience JPanel constructor */
	public CPanel(Component content, JMenu menu) {
        super(new BorderLayout());
        add(new CMenuBar(menu), PAGE_START);
        add(content, CENTER);
    }
}
