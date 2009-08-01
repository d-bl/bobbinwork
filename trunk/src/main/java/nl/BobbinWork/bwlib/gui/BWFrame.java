/* BWFrame.java Copyright 2006-2007 by J. Pol
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

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class BWFrame extends JFrame {

	protected static final String ICON = "nl/BobbinWork/bwlib/gui/bobbin.gif";

	public BWFrame (String bundle) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(790, 500));
        setBundle(bundle);
        URL iocnURL = getClass().getClassLoader().getResource(ICON);
		setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(getToolkit().getImage(iocnURL));
	}
}
