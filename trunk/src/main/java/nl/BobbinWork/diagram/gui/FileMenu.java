/* FileMenu.java Copyright 2006-2007 by J. Pol
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

import static java.awt.event.InputEvent.ALT_DOWN_MASK;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_F4;
import static java.awt.event.KeyEvent.VK_O;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.io.BWFileFilter;
import nl.BobbinWork.bwlib.io.NamedInputStream;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

     private JFileChooser fileChooser = null;
    
     /**
     * Creates a basic file menu. The items that require file system access 
     * are disabled in a secure environment.
     * @param inputStreamListener 
     *        listens for newly opened input streams. 
     *        The event source will be a NamedInputStream. 
     * @param saveListener 
     *        listens for save requests from the user. 
     *        The event source will be a String with a file name
     *        in case of SaveAs or null otherwise.
     * @param newFileName 
     *        name of the file that is opened as a stream if the new item is pressed
     */
    public FileMenu(
            final ActionListener inputStreamListener, 
            final ActionListener saveListener, 
            final String newFileName) {

        applyStrings(this, "MenuFile_file"); //$NON-NLS-1$

        try { 
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new BWFileFilter( 
                    getString("FileType"), //$NON-NLS-1$
                    "xml,bwml".split(",") )); //$NON-NLS-1$ //$NON-NLS-2$
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            
        } catch (java.security.AccessControlException e) { }
        
        JMenuItem jMenuItem = new LocaleMenuItem( "MenuFile_open", VK_O, CTRL_DOWN_MASK); //$NON-NLS-1$
        addConditionalItem ( jMenuItem, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( getIntputStream(e) ) inputStreamListener.actionPerformed(e);
            }
        });

        // TODO change to Quit for mac
        jMenuItem = new LocaleMenuItem( "MenuFile_exit", VK_F4, ALT_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(jMenuItem);
    }
    
    private void addConditionalItem (JMenuItem jMenuItem, ActionListener actionListener){
        if ( fileChooser == null ) {
            jMenuItem.setEnabled(false); 
        } else {   
            jMenuItem.addActionListener(actionListener);
        }
        add(jMenuItem, 0);
    }
    
    
    public final void clearSelectedFile() {
        if (fileChooser != null) fileChooser.setSelectedFile(null);
    }

    /**
     * Creates a stream after prompting the user for a file to read. 
     * Shows an error message on failure.
     *  
     * @return null on cancel or failure 
     */
    private boolean getIntputStream(ActionEvent e) {

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        String name = fileChooser.getSelectedFile().getAbsolutePath();
        try {
            e.setSource(new NamedInputStream(name, new FileInputStream(name)));
            return true;
        } catch (FileNotFoundException ioe) {
            showError(ioe.getLocalizedMessage(), getString("MenuFile_open")); //$NON-NLS-1$
        }
        return false;
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
