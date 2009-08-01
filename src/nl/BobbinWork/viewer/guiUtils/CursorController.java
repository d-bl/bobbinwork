/* BWTree.java Copyright 2006-2008 by J. Pol
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
 * 
 * -------------
 * 
 * based on an article by Simon White on http://www.catalysoft.com/articles/busyCursor.html
 */
package nl.BobbinWork.viewer.guiUtils;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public final class CursorController {

	public final static Cursor busyCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	private CursorController() {}

	public static ActionListener createListener(final Component parent, final ActionListener mainActionListener) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Cursor savedCursor = parent.getCursor();
				try {
					parent.setCursor(busyCursor);
					mainActionListener.actionPerformed(ae);
				} finally {
					parent.setCursor(savedCursor);
				}
			}
		};
	}
}