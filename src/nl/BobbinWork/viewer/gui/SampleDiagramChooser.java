/* SampleDiagramChooser.java Copyright 2006-2008 by J. Pol
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

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.io.NamedInputStream;

/**
 * A menu that lets a user open a web page as a stream.
 * Most menu items specify predefined URLs of sample diagrams. 
 * One item launches a dialog allowing a user to enter a URL.
 * 
 * @author J. Pol
 *
 */
@SuppressWarnings("serial") //$NON-NLS-1$
public class SampleDiagramChooser extends JMenu {

	private static final String BASE_URL = "http://bobbinwork.googlecode.com/svn-history/r293/wiki/diagrams/"; //$NON-NLS-1$
    private static final String[] SAMPLE_URLS = new String[] {//
    	  "snow.xml", //$NON-NLS-1$
    	  "flanders.xml", //$NON-NLS-1$
    	  "braid-half-stitch.xml", //$NON-NLS-1$
    	  "braid-chaos.xml", //$NON-NLS-1$
    	  "braid-row-cloth-row-half-stitch.xml", //$NON-NLS-1$
    };
    /**
     * Handed down to dialogs.
     */
    private Component parent;
    
    private ActionListener externalActionListener;
    private InputStream inputStream = null;
    private String inputStreamName = null;
    
    /**
     * Gives anonymous ActionListener's access to fields.
     */
    private SampleDiagramChooser self = this;
    
	/**
	 * Creates an inputStream from the specified URL.
	 * @param e 
	 * 
	 * @param url selected or entered by the user 
	 * @return 
	 */
	private void createInputStream(ActionEvent e, String url) {
		try {
			inputStreamName = ""; //$NON-NLS-1$
			inputStream = (new URL(url)).openStream();
			inputStreamName = url;
			e.setSource(new NamedInputStream(inputStreamName, inputStream));
			externalActionListener.actionPerformed(e);

		} catch (MalformedURLException e1) {
		    JOptionPane.showMessageDialog(parent, //
		            url + "\n" + e1.getLocalizedMessage(),// //$NON-NLS-1$
		            getString("MenuFile_LoadSample_invalid_URL"), // //$NON-NLS-1$
		            JOptionPane.ERROR_MESSAGE);
		    return;
		} catch (IOException e1) {
			JOptionPane.showInputDialog(
					parent,
	                "", //$NON-NLS-1$
	                url+"\n"); //$NON-NLS-1$
			return;
		}
	}
	
	/**
	 * @param parent handed down to dialogs
	 * @param externalActionListener triggered when an InputStream is created from a user selected URL 
	 */
	public SampleDiagramChooser (Component parent, ActionListener externalActionListener){
    	super();
    	this.externalActionListener = externalActionListener;
    	this.parent = parent;
        applyStrings(this, "MenuFile_LoadSample"); //$NON-NLS-1$

    	JMenuItem jMenuItem;

        for (String f:SAMPLE_URLS) {
            jMenuItem = new JMenuItem(BASE_URL + f);
            jMenuItem.setActionCommand(BASE_URL + f);
            jMenuItem.addActionListener( new ActionListener() {

            	public void actionPerformed(ActionEvent e) {
            		createInputStream(e,e.getActionCommand());
            	}
            } );
            add(jMenuItem);
    	}
        
        add(new JSeparator());

        jMenuItem = new LocaleMenuItem("MenuFile_ChooseSample"); //$NON-NLS-1$
    	jMenuItem.addActionListener(new ActionListener() {

    		public void actionPerformed(ActionEvent e) {
    			createInputStream(e,(String)JOptionPane.showInputDialog(
    	                self.parent,
    	                "", //$NON-NLS-1$
    	                "http://"));  //$NON-NLS-1$
    			}
    	});
        add(jMenuItem);
        
    	// TODO enhancements to prepare in the background
    	// replace with actual links from http://groups.google.com/group/bobbinwork/files
    	// cache files 
    	// cache preview images for the drop down list
	}
}
