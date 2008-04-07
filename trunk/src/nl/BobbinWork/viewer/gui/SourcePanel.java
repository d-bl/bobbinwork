/* SourcePanel.java Copyright 2006-2008 by J. Falkink-Pol
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

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.BobbinWork.bwlib.gui.CMenuBar;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import static nl.BobbinWork.bwlib.gui.Localizer.*;

@SuppressWarnings("serial")
class SourcePanel extends JPanel {

	private SourceArea source = new SourceArea();
	
	SourcePanel(ActionListener sourceListener) {
        super(new BorderLayout());
        add(new CMenuBar(new EditMenu(sourceListener)), PAGE_START);
        add(new JScrollPane(source), CENTER);
        
	}
	
	String getText () {
		return source.getText();
	}
	
	void setText (String s) {
		source.setText(s);
	}

	void read (Reader s, String fileName) throws IOException {
		source.read(s, fileName);
	}
	
    /** A JTextArea with an additional method to insert color codes. */
	private class SourceArea extends JTextArea {

        /**
         * Shows a JColorChooser dialog and pastes the result as a hexadecimal
         * code into the associated text model.
         */
        private void insertColor() {
            Color color = JColorChooser.showDialog(this, source.getText(), Color.BLACK);
            if (color != null) {
                String s = "#" + Integer.toHexString(color.getRGB() & 0xFFFFFF);  //$NON-NLS-1$
                replaceSelection(s);
            }
        }
    }

    /** A fully dressed JMenu, to edit the XML source */
	private class EditMenu extends JMenu {

        /** Creates a fully dressed JMenu, to edit the XML source */
        EditMenu(final ActionListener sourceListener) {

            applyStrings(this, "MenuEdit_edit"); //$NON-NLS-1$

            JMenuItem//
            jMenuItem = new LocaleMenuItem( "MenuEdit_show",VK_F5, 0); //$NON-NLS-1$
            jMenuItem.addActionListener(sourceListener);
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            jMenuItem = new LocaleMenuItem( "MenuEdit_InsertColor"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.insertColor();
                }
            });
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            jMenuItem = new LocaleMenuItem( "MenuEdit_cut",VK_X, CTRL_DOWN_MASK); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.cut();
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem( "MenuEdit_copy",VK_C, CTRL_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.copy();
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem( "MenuEdit_paste",VK_V, CTRL_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.paste();
                }
            });
            add(jMenuItem);
        }
    }
}
