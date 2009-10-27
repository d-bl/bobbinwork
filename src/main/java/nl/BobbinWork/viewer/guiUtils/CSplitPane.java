/* SplitPane.java Copyright 2006-2007 by J. Pol
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

import java.awt.Component;

import javax.swing.JSplitPane;

/** A JToolbar with an additional convenience constructor. */
@SuppressWarnings("serial")
public class CSplitPane extends JSplitPane {

    public static final int DIVIDER_WIDTH = 8;

    /** Convenience JSplitPane constructor */
    public CSplitPane(int dividerposition, int orientation, Component Left, Component Right) {
        super(orientation, Left, Right);
        if (getDividerSize() < DIVIDER_WIDTH) {
            setDividerSize(DIVIDER_WIDTH);
        }
        setBorder(null); // prevent nested borders
        setDividerLocation(dividerposition);
        setOneTouchExpandable(true);
    }
}
