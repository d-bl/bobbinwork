/* PolarGridPrinter.java Copyright 2005-2007 by J. Pol
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

package nl.BobbinWork.grids.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import nl.BobbinWork.bwlib.gui.BWFrame;
import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.gui.PrintMenu;
import nl.BobbinWork.grids.PolarGridModel.PolarGridModel;

/**
 *
 * @author J. Pol
 */

public class PolarGridPrinter {
    
	private static final String YEARS = "2005-2008";  //$NON-NLS-1$  
	private static final String BASE_CAPTION = "Polar Grids"; //$NON-NLS-1$  

    static private final String LOCALIZER_BUNDLE_NAME = "nl/BobbinWork/grids/GridGUI/labels"; //$NON-NLS-1$
    
	private JFrame frame = new BWFrame(LOCALIZER_BUNDLE_NAME); 

    public PolarGridPrinter() {
        
        final PolarGridModel pgm = new PolarGridModel();
        final PreviewPanel previewPanel = new PreviewPanel();
        final ConfigurationPanel configurationPanel = new ConfigurationPanel(pgm);
        final JScrollPane previewScrollPane = new JScrollPane(previewPanel);
        final JScrollPane configurationScrollPane = new JScrollPane(configurationPanel);
        
        previewPanel.setPolarGridModel(pgm);
        configurationPanel.addRepaintingListeners(previewPanel);
        previewScrollPane.setPreferredSize(new Dimension(250, 250));
        addSpaceForScrollBar(configurationScrollPane);
        
        frame.getContentPane().add(WEST, configurationScrollPane);
        frame.getContentPane().add(CENTER, previewScrollPane);
        frame.setJMenuBar( createMenuBar (previewPanel) );
    }

	private void addSpaceForScrollBar(JScrollPane scrollPane) {
		
		// don't let the need for one scroll bar introduce the need for another
		
		Dimension dimension = scrollPane.getPreferredSize();
        dimension.width += 40;
        dimension.height += 40;
        scrollPane.setPreferredSize(dimension);
	}

	private JMenuBar createMenuBar(final PreviewPanel previewPanel) {
		
		JMenu menu;
        JMenuItem jMenuItem;
        
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add( new PrintMenu(previewPanel) );
        
        menu = new javax.swing.JMenu(getString("MenuView_View"));
        menu.setMnemonic(getString("MenuView_View_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new LocaleMenuItem("MenuView_Refresh", KeyEvent.VK_F5, 0); 
        jMenuItem.addActionListener(new ActionListener(){

        	public void actionPerformed(ActionEvent e) {
        		previewPanel.repaint();
    	    }
        });
        menu.add(jMenuItem);
        
        menu.add(new javax.swing.JSeparator());
        
        addScaleMenuItem(menu, "MenuView_cm", true);
        addScaleMenuItem(menu, "MenuView_mm", false);
        addScaleMenuItem(menu, "MenuView_inch", false);
        
		final HelpMenu helpMenu = new HelpMenu(frame, YEARS,BASE_CAPTION);
		frame.setTitle(helpMenu.getVersionedCaption());
		jMenuBar.add( helpMenu );
		return jMenuBar;
	}

	private void addScaleMenuItem(
			JMenu menu, 
			String bundleKeyBase,
			boolean enabled) {
		
		JMenuItem jMenuItem;
		jMenuItem = new LocaleMenuItem(bundleKeyBase); 
		jMenuItem.setEnabled(enabled);
        jMenuItem.setToolTipText(getString("MenuView_scale_ToolTip"));
        menu.add(jMenuItem);
	}
    
	public static void main(String[] args) {

		if (args.length > 0) setBundle(LOCALIZER_BUNDLE_NAME, new Locale(args[0]));
		new PolarGridPrinter().frame.setVisible(true);
	}
}
