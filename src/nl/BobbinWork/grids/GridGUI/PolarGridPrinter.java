/* PolarGridPrinter.java Copyright 2005-2007 by J. Falkink-Pol
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

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static java.util.Locale.getDefault;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.gui.PrintMenu;
import nl.BobbinWork.grids.PolarGridModel.PolarGridModel;

/**
 *
 * @author J. Falkink-Pol
 */

@SuppressWarnings("serial")
public class PolarGridPrinter extends JFrame  {
    
	private static final String YEARS = "2005-2008";  //$NON-NLS-1$  
	private static final String CAPTION = "Polar Grids"; // gets extended by the help menu  //$NON-NLS-1$  

    static private final String dir = "nl/BobbinWork/grids/help/";
    static private final String iconUrl = dir + "bobbin.gif";
    static private final String bundleName = "nl/BobbinWork/grids/GridGUI/labels";
    
    private ConfigurationPanel configurationPanel;
    private PreviewPanel previewPanel;
    
    public PolarGridPrinter() {
        
        PolarGridModel pgm = new PolarGridModel();
        configurationPanel = new ConfigurationPanel(pgm);
        previewPanel = new PreviewPanel();
        previewPanel.setPolarGridModel(pgm);
        
        java.awt.Image icon = null;
        
        java.net.URL url = getClass().getClassLoader().getResource(iconUrl);
        if ( url != null ) {
            icon = this.getToolkit().getImage(url);
            setIconImage(icon);
        }
        
        JScrollPane gridScrollPane = new JScrollPane(previewPanel);
        JScrollPane defScrollPane = new JScrollPane(configurationPanel);
        
        gridScrollPane.setPreferredSize(new Dimension(250, 250));
        Dimension dimension = defScrollPane.getPreferredSize();
        dimension.width += 40;
        defScrollPane.setPreferredSize(dimension);
        
        getContentPane().add(WEST, defScrollPane);
        getContentPane().add(CENTER, gridScrollPane);
        configurationPanel.addRepaintingListeners(previewPanel);
        
        /* ---- menu ---- */
        
        JMenu menu;
        JMenuItem jMenuItem;
        
        JMenuBar jMenuBar = new javax.swing.JMenuBar();
        setJMenuBar(jMenuBar);
        jMenuBar.add( new PrintMenu(previewPanel) );
        
        menu = new javax.swing.JMenu(getString("MenuView_View"));
        menu.setMnemonic(getString("MenuView_View_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new LocaleMenuItem("MenuView_Refresh", KeyEvent.VK_F5, 0); 
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(java.awt.event.ActionEvent e) {
                for ( int i=0 ; i<configurationPanel.getComponentCount() ; i++ ) {
                    if ( configurationPanel.getComponent(i).isFocusOwner() ) {
                        configurationPanel.requestFocusInWindow();
                        configurationPanel.getComponent(i).requestFocus();
                        break;
                    }
                }
            }
            
        });
        menu.add(jMenuItem);
        
        menu.add(new javax.swing.JSeparator());
        
        addScaleMenuItem(menu, "MenuView_cm", true);
        addScaleMenuItem(menu, "MenuView_mm", false);
        addScaleMenuItem(menu, "MenuView_inch", false);
        
		final HelpMenu helpMenu = new HelpMenu(this, YEARS,CAPTION);
		this.setTitle(helpMenu.getVersionedCaption());
		jMenuBar.add( helpMenu );
    }

	private void addScaleMenuItem(JMenu menu, String bundleKeyBase,
			boolean enabled) {
		JMenuItem jMenuItem;
		jMenuItem = new LocaleMenuItem(bundleKeyBase); 
		jMenuItem.setEnabled(enabled);
        jMenuItem.setToolTipText(getString("MenuView_scale_ToolTip"));
        menu.add(jMenuItem);
	}
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        
        //Create and set up the window.
        JFrame frame = new PolarGridPrinter();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String args[]){
        
        try {
            setLookAndFeel( getSystemLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Locale locale;
        if ( args.length > 0 ) {
            locale = new Locale(args[0]);
        } else {
            locale = new Locale
            ( getDefault().getLanguage()
            , getDefault().getCountry()
            );
        }
        setBundle(bundleName, locale);
        
        //Schedule a job for the event-dispatching thread:
        invokeLater(new Runnable() { public void run() { createAndShowGUI();} });
    }
}
