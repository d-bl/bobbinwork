/* BWVApplet.java Copyright 2006-2008 by J. Falkink-Pol
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
import nl.BobbinWork.bwlib.io.InputStreamCreator;

/**
 * A menu that lets a user open a web page as a stream.
 * Most menu items specify predefined URLs of sample diagrams. 
 * One item launches a dialog allowing a user to enter a URL.
 * 
 * @author J. Falkink-Pol
 *
 */
@SuppressWarnings("serial") //$NON-NLS-1$
public class SampleDiagramChooser extends JMenu implements InputStreamCreator {

	private static final String BASE_URL = "http://bobbinwork.googlegroups.com/web/"; //$NON-NLS-1$
	private static final String S = "AAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURD"; //$NON-NLS-1$
    private static final String[] SAMPLE_URLS = new String[] {//
      // "torchon.xml?gda=M4C-rDw" + S + "SHAoqq6MVKgGExQWFmpupE",//$NON-NLS-1$ //$NON-NLS-2$
      // "spiders.xml?gda=4zTd7Tw" + S + "ScIXlog2Wd3wWxjd0fqC3o",//$NON-NLS-1$ //$NON-NLS-2$
      //"flanders.xml?gda=4z7qcT0" + S + "SmCdrehNs8EVCWMltc8R2k",//$NON-NLS-1$ //$NON-NLS-2$
    	  "braid2.xml?gda=UQQDQzs" + S + "RVk4fsgKMsk2Mbh43_kAi9",//$NON-NLS-1$ //$NON-NLS-2$
    	  "braid1.xml?gda=2VRhyDs" + S + "RD4DT6Fua1gH-xvy-DVSKm"};//$NON-NLS-1$ //$NON-NLS-2$
    
    /**
     * Handed down to dialogs.
     */
    private Component parent;
    
    private SampleDiagramChooser externalActionSource = this;
    
    private ActionListener externalActionListener;
    private InputStream inputStream = null;
    private String inputStreamName = null;
	public String getInputStreamName() {
		return inputStreamName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	
    
    /**
     * Gives anonymous ActionListener's access to fields.
     */
    private SampleDiagramChooser self = this;
    
	/**
	 * Creates an inputStream from the specified URL.
	 * 
	 * @param url selected or entered by the user 
	 */
	private void setInputStream(String url) {
		try {
			inputStreamName = ""; //$NON-NLS-1$
			inputStream = (new URL(url)).openStream();
			inputStreamName = url;
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
     * Listens to the menu item that lets the user enter a URL with a dialog.
     */
    private ActionListener freeListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			setInputStream((String)JOptionPane.showInputDialog(
	                self.parent,
	                "", //$NON-NLS-1$
	                "http://"));  //$NON-NLS-1$
    		e.setSource(externalActionSource);
    		externalActionListener.actionPerformed(e);
		}
	};
	
    /**
     * Listens to the menu items with a predefined URL.
     */
    private ActionListener predefinedListener = new ActionListener() {

    	public void actionPerformed(ActionEvent e) {
    		setInputStream(e.getActionCommand());
    		e.setSource(externalActionSource);
    		externalActionListener.actionPerformed(e);
    	}
    };
    
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

        for (int i=0 ; i < SAMPLE_URLS.length ; i++){
            jMenuItem = new JMenuItem(BASE_URL+SAMPLE_URLS[i].replaceAll("\\?.*", "")); //$NON-NLS-1$ //$NON-NLS-2$
            jMenuItem.setActionCommand(BASE_URL + SAMPLE_URLS[i]);
            jMenuItem.addActionListener( predefinedListener );
            add(jMenuItem);
    	}
        
        add(new JSeparator());

        jMenuItem = new LocaleMenuItem("MenuFile_ChooseSample"); //$NON-NLS-1$
    	jMenuItem.addActionListener(freeListener);
        add(jMenuItem);
        
    	// TODO enhancements to prepare in the background
    	// replace with actual links from http://groups.google.com/group/bobbinwork/files
    	// cache files 
    	// cache preview images for the drop down list
	}
}
