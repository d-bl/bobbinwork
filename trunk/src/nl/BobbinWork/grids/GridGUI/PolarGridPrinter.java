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

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.util.Locale;

import nl.BobbinWork.bwlib.gui.AboutInfo;
import nl.BobbinWork.grids.PolarGridModel.PolarGridModel;

/**
 *
 * @author J. Falkink-Pol
 */

public class PolarGridPrinter
        extends javax.swing.JFrame
        implements java.awt.event.ActionListener {
    
    static private final AboutInfo aboutInfo = new AboutInfo
            ( " BobbinWork - Polar Grid" // caption
            , "1.7"                      // version
            , "2005-2006"                // years
            , "J. Falkink-Pol"           // author
            );
    static private final String dir = "nl/BobbinWork/grids/help/";
    static private final String site = "http://www.xs4all.nl/~falkink/lace/";
    static private final String iconUrl = dir + "bobbin.gif";
    static private final String bundleName = "nl/BobbinWork/grids/GridGUI/labels";
    static private java.util.ResourceBundle bundle = null; // set in main
    
    private PolarGridDefinitionPanel defPanel = null; // set in constructor
    private PolarGridDrawPanel gridPanel = null; // set in constructor
    
    public PolarGridPrinter() {
        
        PolarGridModel pgm = new PolarGridModel();
        defPanel = new PolarGridDefinitionPanel(pgm,bundle);
        gridPanel = new PolarGridDrawPanel();
        gridPanel.setPolarGridModel(pgm);
        
        java.awt.Image icon = null;
        
        setTitle(aboutInfo.getCaption());
        java.net.URL url = getClass().getClassLoader().getResource(iconUrl);
        if ( url != null ) {
            icon = this.getToolkit().getImage(url);
            setIconImage(icon);
        }
        JMenuItemAbout jMenuItemAbout = new JMenuItemAbout(aboutInfo, bundle);
        
        javax.swing.JScrollPane diagramScrollPane = new javax.swing.JScrollPane(gridPanel);
        getContentPane().add(java.awt.BorderLayout.WEST, defPanel);
        getContentPane().add(java.awt.BorderLayout.CENTER, diagramScrollPane);
        defPanel.addListener(gridPanel);
        diagramScrollPane.setPreferredSize(new java.awt.Dimension(250, 250));
        
        /* ---- menu ---- */
        
        javax.swing.JMenu menu;
        javax.swing.JMenuItem jMenuItem;
        
        javax.swing.JMenuBar jMenuBar = new javax.swing.JMenuBar();
        setJMenuBar(jMenuBar);
        
        menu = new javax.swing.JMenu(bundle.getString("MenuFile_File"));
        menu.setMnemonic(bundle.getString("MenuFile_File_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_New"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_New_Underline").charAt(0));
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setEnabled(false);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_Open"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Open_Underline").charAt(0));
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setEnabled(false);
        menu.add(jMenuItem);
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuFile_Save"));
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Save_Underline").charAt(0));
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
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
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem.setToolTipText(bundle.getString("MenuFile_print_ToolTip"));
        jMenuItem.addActionListener(gridPanel);
        menu.add(jMenuItem);
        
        menu.add(new javax.swing.JSeparator());
        
        jMenuItem = new JMenuItemExit(bundle);
        jMenuItem.setMnemonic(bundle.getString("MenuFile_Exit_Underline").charAt(0));
        jMenuItem.setText(bundle.getString("MenuFile_Exit"));
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menu.add(jMenuItem);
        
        /**/
        
        menu = new javax.swing.JMenu(bundle.getString("MenuView_View"));
        menu.setMnemonic(bundle.getString("MenuView_View_Underline").charAt(0));
        jMenuBar.add( menu );
        
        jMenuItem = new javax.swing.JMenuItem(bundle.getString("MenuView_Refresh"));
        jMenuItem.setMnemonic(bundle.getString("MenuView_Refresh_Underline").charAt(0));
        jMenuItem.setActionCommand("refresh");
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem.addActionListener(this);
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
        jMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
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
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if ( e.getActionCommand().equals("refresh") ) {
            for ( int i=0 ; i<defPanel.getComponentCount() ; i++ ) {
                if ( defPanel.getComponent(i).isFocusOwner() ) {
                    defPanel.requestFocusInWindow();
                    defPanel.getComponent(i).requestFocus();
                    break;
                }
            }
        }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        
        //Create and set up the window.
        javax.swing.JFrame frame = new PolarGridPrinter();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String args[]){
        
        try {
            javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );
            //com.sun.java.swing.plaf.windows.WindowsLookAndFeel() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.util.Locale locale;
        if ( args.length > 0 ) {
            locale = new java.util.Locale(args[0]);
        } else {
            locale = new java.util.Locale
            ( java.util.Locale.getDefault().getLanguage()
            , java.util.Locale.getDefault().getCountry()
            );
        }
        setBundle(bundleName, locale);
        bundle = java.util.ResourceBundle.getBundle( bundleName, locale); // TODO: deprecated
        
        //Schedule a job for the event-dispatching thread:
        javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() { createAndShowGUI();} });
    }
}
