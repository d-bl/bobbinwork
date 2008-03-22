package nl.BobbinWork.bwlib.io;

import static java.awt.event.InputEvent.ALT_DOWN_MASK;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.KeyEvent.VK_F4;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_S;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import static nl.BobbinWork.bwlib.gui.Localizer.getString;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

     private JFileChooser fileChooser;
    
     /**
     * Creates a basic file menu. The menu contains at least an item to open 
     * a new file with default content. If the environments allows access
     * to the file system, also the standard items 
     * save, save as, open and exit are created.
     * 
     * @param restrictedEnvironment 
     *        true if the caller has no access to the file system 
     * @param newFile 
     * 	      the resource to be opened by the classLoader
     * 	 	  when the "new" item is selected from the menu.
     * @param inputStreamListener 
     * 		  listens for newly opened input streams. 
     * 		  The event source type will be a NamedInputStream. 
     * @param saveListener 
     * 		  listens for save requests from the user. 
     * 		  The event source will be a String with a file name
     * 		  in case of SaveAs or null otherwise.
     */
    public FileMenu(
    		boolean restrictedEnvironment, 
    		final ActionListener newListener,
    		final ActionListener inputStreamListener,
    		final ActionListener saveListener) {

        applyStrings(this, "MenuFile_file"); //$NON-NLS-1$

        JMenuItem//

        jMenuItem = new LocaleMenuItem("MenuFile_New", VK_N, CTRL_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            	clearSelectedFile(); 
        		newListener.actionPerformed(e);
        	}
        });
        add(jMenuItem);

        if ( restrictedEnvironment ) return;
        // the remaining IO items only with access rights for the file system

        add(new JSeparator());

        jMenuItem = new LocaleMenuItem( "MenuFile_exit",VK_F4, ALT_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(jMenuItem);
        
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new BWFileFilter( 
        		getString("FileType"), //$NON-NLS-1$
        		"xml,bwml".split(",") )); //$NON-NLS-1$ //$NON-NLS-2$
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        insertSeparator(0);

        jMenuItem = new LocaleMenuItem("MenuFile_SaveAs"); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if ( getSaveAsFileName(e) ) saveListener.actionPerformed(e);
            }
        });
        insert(jMenuItem, 0);

        jMenuItem = new LocaleMenuItem ("MenuFile_save",VK_S, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		if (fileChooser.getSelectedFile() == null) {
        			if ( getSaveAsFileName(e) ) saveListener.actionPerformed(e);
        		} else {
        			e.setSource(fileChooser.getSelectedFile());
        			saveListener.actionPerformed(e);
        		}
            }
        });
        insert(jMenuItem, 0);

        jMenuItem = new LocaleMenuItem( "MenuFile_open",VK_O, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            	if ( getIntputStream(e) ) inputStreamListener.actionPerformed(e);
            }
        });
        insert(jMenuItem, 0);
    }
    
	public void clearSelectedFile() {
		if (fileChooser != null) fileChooser.setSelectedFile(null);
	}

	/**
	 * Sets the source of the event to the name of a new file. 
	 * Shows an error message on failure.
	 *  
	 * @return null on cancel or failure 
	 */
    private Boolean getSaveAsFileName (ActionEvent e) {
    	
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String name = fileChooser.getSelectedFile().toString();
            try {
				if (fileChooser.getSelectedFile().createNewFile()) {
                    e.setSource(new File(name));
                    return true;
                } else {
                    showError( 
                    		getString("MenuFile_SaveAs_error").replaceAll("%FFF%", name),//$NON-NLS-1$
                            getString("MenuFile_SaveAs"));  //$NON-NLS-1$
                }
            } catch (IOException ioe) {
                showError(ioe.getLocalizedMessage(), getString("MenuFile_SaveAs"));
            }
        }
        return false;
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
        String name = fileChooser.getSelectedFile().getAbsolutePath().toString();
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
