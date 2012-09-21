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
     */
    public FileMenu(final ActionListener inputStreamListener) {

        applyStrings(this, "MenuFile_file"); //$NON-NLS-1$

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new BWFileFilter( 
                getString("FileType"), //$NON-NLS-1$
                "xml,bwml".split(",") )); //$NON-NLS-1$ //$NON-NLS-2$
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        JMenuItem jMenuItem = new LocaleMenuItem( "MenuFile_open", VK_O, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if ( getIntputStream(e) ) inputStreamListener.actionPerformed(e);
                        }
                    });
        add(jMenuItem, 0);

        jMenuItem = new LocaleMenuItem( "MenuFile_exit", VK_F4, ALT_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(jMenuItem);
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
            JOptionPane.showMessageDialog(this, ioe.getLocalizedMessage(), getString("MenuFile_open"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
        }
        return false;
    }
}
