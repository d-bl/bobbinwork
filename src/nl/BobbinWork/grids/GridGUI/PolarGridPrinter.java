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

import nl.BobbinWork.grids.PolarGridModel.PolarGridModel;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterJob;
import java.util.Locale;

import static java.awt.BorderLayout.WEST;
import static java.awt.BorderLayout.CENTER;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.setLookAndFeel;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.KeyStroke.getKeyStroke;

/**
 *
 * @author J. Falkink-Pol
 */

@SuppressWarnings("serial")
public class PolarGridPrinter extends JFrame  {
    
    static private final AboutInfo aboutInfo = new AboutInfo
            ( " BobbinWork - Polar Grid" // caption
            , "2005-2006"                // years
            , "J. Falkink-Pol"           // author
            );
    static private final String dir = "nl/BobbinWork/grids/help/";
    static private final String site = "http://www.xs4all.nl/~falkink/lace/";
    static private final String iconUrl = dir + "bobbin.gif";
    static private final String bundleName = "nl/BobbinWork/grids/GridGUI/labels";
    static private java.util.ResourceBundle bundle = null; // set in main
    
    private ConfigurationPanel configurationPanel = null; // set in constructor
    private PreviewPanel previewPanel = null; // set in constructor
    
    public PolarGridPrinter() {
        
        PolarGridModel pgm = new PolarGridModel();
        configurationPanel = new ConfigurationPanel(pgm,bundle);
        previewPanel = new PreviewPanel();
        previewPanel.setPolarGridModel(pgm);
        
        java.awt.Image icon = null;
        
        setTitle(aboutInfo.getCaption());
        java.net.URL url = getClass().getClassLoader().getResource(iconUrl);
        if ( url != null ) {
            icon = this.getToolkit().getImage(url);
            setIconImage(icon);
        }
        JMenuItemAbout jMenuItemAbout = new JMenuItemAbout(aboutInfo, bundle);
        
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
        
        menu = new javax.swing.JMenu(bundle.getString("MenuFile_File"));
        menu.setMnemonic(bundle.getString("MenuFile_File_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_New"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_New_Underline").charAt(0));
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setEnabled(false);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_Open"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Open_Underline").charAt(0));
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setEnabled(false);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_Save"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Save_Underline").charAt(0));
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setEnabled(false);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_Save_as"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Save_as_Underline").charAt(0));
        jMenuItem.setEnabled(false);
        menu.add(jMenuItem);
        
        menu.add(new javax.swing.JSeparator());
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_Print"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Print_Underline").charAt(0));
        jMenuItem.setActionCommand("print");
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setToolTipText(bundle.getString("MenuFile_print_ToolTip"));
        jMenuItem.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent actionEvent) {
	            PrinterJob printJob = PrinterJob.getPrinterJob();
	            printJob.setPrintable(previewPanel);
	            if (printJob.printDialog()) {
	                try {
	                    printJob.print();
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
			}});
        menu.add(jMenuItem);
        
        menu.add(new javax.swing.JSeparator());
        
        jMenuItem = new JMenuItemExit(bundle);
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Exit_Underline").charAt(0));
        jMenuItem.setText(bundle.getString("MenuFile_Exit"));
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menu.add(jMenuItem);
        
        /**/
        
        menu = new javax.swing.JMenu(bundle.getString("MenuView_View"));
        menu.setMnemonic(bundle.getString("MenuView_View_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuView_Refresh"));
        jMenuItem.setMnemonic(bundle.getString("MenuView_Refresh_Underline").charAt(0));
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_F5, 0));
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
        
        String scaleToolTip = bundle.getString("MenuView_scale_ToolTip");
        
        jMenuItem = new javax.swing.JRadioButtonMenuItem(bundle.getString("MenuView_mm"),true);
        jMenuItem.setMnemonic(bundle.getString("MenuView_mm_Underline").charAt(0));
        jMenuItem.setToolTipText(scaleToolTip);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JRadioButtonMenuItem(bundle.getString("MenuView_cm"),false);
        jMenuItem.setMnemonic(bundle.getString("MenuView_cm_Underline").charAt(0));
        jMenuItem.setEnabled(false);
        jMenuItem.setToolTipText(scaleToolTip);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JRadioButtonMenuItem(bundle.getString("MenuView_inch"),false);
        jMenuItem.setMnemonic(bundle.getString("MenuView_inch_Underline").charAt(0));
        jMenuItem.setEnabled(false);
        jMenuItem.setToolTipText(scaleToolTip);
        menu.add(jMenuItem);
        
        /**/
        
        String unknownBrowser = "can't find your Browser";
        
        menu = new javax.swing.JMenu(bundle.getString("MenuHelp_Help"));
        menu.setMnemonic(bundle.getString("MenuHelp_Help_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new JMenuItemBrowse(unknownBrowser, icon, aboutInfo.getCaption());
        jMenuItem.setAccelerator(getKeyStroke(KeyEvent.VK_F1, 0));
        jMenuItem.setText(bundle.getString("MenuHelp_Overview"));
        jMenuItem.setMnemonic(bundle.getString("MenuHelp_Overview_Underline").charAt(0));
        jMenuItem.setActionCommand(dir+"help.html");
        menu.add(jMenuItem);
        
        jMenuItem = new JMenuItemBrowse(unknownBrowser, icon, aboutInfo.getCaption());
        jMenuItem.setText(bundle.getString("MenuHelp_Spiders"));
        jMenuItem.setMnemonic(bundle.getString("MenuHelp_Spiders_Underline").charAt(0));
        jMenuItem.setActionCommand(dir+"spiders.html");
        menu.add(jMenuItem);
        
        jMenuItem = new JMenuItemBrowse(unknownBrowser, icon, aboutInfo.getCaption());
        jMenuItem.setText(bundle.getString("MenuHelp_Website"));
        jMenuItem.setMnemonic(bundle.getString("MenuHelp_Website_Underline").charAt(0));
        jMenuItem.setActionCommand(site+"grid-round-EN.html");
        menu.add( jMenuItem );
        
        jMenuItem = new JMenuItemBrowse(unknownBrowser, icon, aboutInfo.getCaption());
        jMenuItem.setText(bundle.getString("MenuHelp_Release_notes"));
        jMenuItem.setMnemonic(bundle.getString("MenuHelp_Release_notes_Underline").charAt(0));
        jMenuItem.setActionCommand(dir+"ReleaseNotes.html");
        menu.add(jMenuItem);
        
        menu.add( jMenuItemAbout );
        
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
            //com.sun.java.swing.plaf.windows.WindowsLookAndFeel() );
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
        bundle = getBundle( bundleName, locale); // TODO: deprecated
        
        //Schedule a job for the event-dispatching thread:
        invokeLater(new Runnable() { public void run() { createAndShowGUI();} });
    }
}
