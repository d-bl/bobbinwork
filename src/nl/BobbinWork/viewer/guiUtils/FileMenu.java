package nl.BobbinWork.viewer.guiUtils;

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
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import static nl.BobbinWork.bwlib.gui.Localizer.getString;
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
     * 		  listens for newly opened input streams. 
     * 		  The event source will be a NamedInputStream. 
     * @param saveListener 
     * 		  listens for save requests from the user. 
     * 		  The event source will be a String with a file name
     * 		  in case of SaveAs or null otherwise.
     * @param newFileName 
     * 	      name of the file that is opened as a stream if the new item is pressed
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
        
        JMenuItem jMenuItem;
        
        jMenuItem = new LocaleMenuItem("MenuFile_SaveAs"); //$NON-NLS-1$
        addConditionalItem ( jMenuItem, new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if ( getSaveAsFileName(e) ) saveListener.actionPerformed(e);
        	}
        });
        
        jMenuItem = new LocaleMenuItem ("MenuFile_save", VK_S, CTRL_DOWN_MASK); //$NON-NLS-1$
        addConditionalItem( jMenuItem, new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (fileChooser.getSelectedFile() == null) {
        			if ( getSaveAsFileName(e) ) saveListener.actionPerformed(e);
        		} else {
        			e.setSource(fileChooser.getSelectedFile());
        			saveListener.actionPerformed(e);
        		}
        	}
        });
        
        jMenuItem = new LocaleMenuItem( "MenuFile_open", VK_O, CTRL_DOWN_MASK); //$NON-NLS-1$
        addConditionalItem ( jMenuItem, new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if ( getIntputStream(e) ) inputStreamListener.actionPerformed(e);
        	}
        });

        add(new JSeparator());
        
        jMenuItem = new LocaleMenuItem("MenuFile_New", VK_N, CTRL_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		clearSelectedFile();
        		InputStream stream = getClass().getClassLoader().getResourceAsStream(newFileName);
            	e.setSource(new NamedInputStream(newFileName, stream));
            	inputStreamListener.actionPerformed(e);
        	}

        });
        add(jMenuItem);

        add(new JSeparator());

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
