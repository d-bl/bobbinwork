/* DiagramPanel.java Copyright 2006-2007 by J. Pol
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

package nl.BobbinWork.diagram.gui;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_F7;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;

/**
 * A menu controlling the appearance of the diagram.
 */
@SuppressWarnings("serial")
public class ViewMenu extends JMenu {

    /**
     * A fully dressed JMenu, controlling the appearance of the high lights on
     * the diagram
     */
	static private class DiagramHighlightsMenu extends JMenu {

        private DiagramHighlightsMenu(final DiagramPanel diagramPanel, final JFrame parentForDialogs) {
        	applyStrings(this, "MenuView_highlight");
        	
        	JMenuItem//

            jMenuItem = new LocaleMenuItem("MenuHighlight_AreaColor"); //$NON-NLS-1$
            jMenuItem.setBackground(new Color(diagramPanel.getAreaHighlight().getRGB()));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(//
                    		parentForDialogs, getString("Dialog_AreaHiglight"), diagramPanel.getAreaHighlight());
                    if (color != null) {
                        ((JMenuItem) e.getSource()).setBackground(color);
                        diagramPanel.setAreaHighlight(color);
                    }
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem("MenuHighlight_ThreadColor"); //$NON-NLS-1$
            jMenuItem.setBackground(new Color(diagramPanel.getThreadHighlight().getRGB()));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(//
                            parentForDialogs, getString("Dialog_ThreadHighlight"), diagramPanel.getThreadHighlight());
                    if (color != null) {
                        ((JMenuItem) e.getSource()).setBackground(color);
                        diagramPanel.setThreadHighlight(color);
                    }
                }
            });
            add(jMenuItem);
        }
    }

    /** Creates a fully dressed JMenu, controlling the view of the diagram */
    public ViewMenu(
    		final DiagramPanel diagramPanel, 
    		final JFrame parentForDialogs,
    		final ThreadStyleToolBar threadStyleToolBar) {
    	
        applyStrings(this, "MenuView_view"); //$NON-NLS-1$

        JMenuItem//

        jMenuItem = new LocaleMenuItem("MenuView_zoomIn",VK_I, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.setScreenScale(diagramPanel.getScreenScale() * 1.25);
            }
        });
        add(jMenuItem);

        jMenuItem = new LocaleMenuItem("MenuView_zoomOut",VK_J, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.setScreenScale(diagramPanel.getScreenScale() * 0.8);
            }
        });
        add(jMenuItem);

        jMenuItem = new LocaleMenuItem("MenuView_zoomReset",VK_K, CTRL_DOWN_MASK); //$NON-NLS-1$ 
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.setScreenScale(1d);
            }
        });
        add(jMenuItem);

        add(new javax.swing.JSeparator());

        add(new DiagramHighlightsMenu(diagramPanel,parentForDialogs));

        add(new javax.swing.JSeparator());

        jMenuItem = new LocaleMenuItem("MenuView_thread",VK_F7, SHIFT_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.setDiagramType(true, false);
                if (threadStyleToolBar!=null)threadStyleToolBar.setVisible(true);
            }
        });
        add(jMenuItem);

        jMenuItem = new LocaleMenuItem("MenuView_pair",VK_F7, 0); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.setDiagramType(false, true);
                if (threadStyleToolBar!=null)threadStyleToolBar.setVisible(false);
            }
        });
        add(jMenuItem);

        jMenuItem = new LocaleMenuItem("MenuView_hybrid", VK_F7, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.setDiagramType(true, true);
                if (threadStyleToolBar!=null)threadStyleToolBar.setVisible(true);
            }
        });
        add(jMenuItem);
    }
}
