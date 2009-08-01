/* TestBWViewer.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.viewer.gui;

import org.uispec4j.Button;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;

public class TestBWViewer extends UISpecTestCase {

	private static final int SECOND = 1000;
    private Button replaceButton;
    private Button deleteButton;

	protected void setUp() throws Exception {
	    super.setUp();
		setAdapter( new MainClassAdapter(
				nl.BobbinWork.viewer.gui.BWViewer.class, 
				new String[] {"nl"} 
		));
	    Window w = getMainWindow();
		this.replaceButton = w.getButton("verwijderen");
	    this.deleteButton = w.getButton("vervangen");
	    UISpec4J.setWindowInterceptionTimeLimit(20*SECOND);
	}	

	public void testInitialAppearance () throws Exception {
		
		assertFalse( replaceButton.isEnabled() );
		assertFalse( deleteButton.isEnabled() );
	}
}
