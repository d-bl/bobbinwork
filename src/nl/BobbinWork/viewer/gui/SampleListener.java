/* BWVApplet.java Copyright 2008-2008 by J. Falkink-Pol
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import static nl.BobbinWork.bwlib.gui.Localizer.getString;

public class SampleListener implements ActionListener {

    private static final String[] SAMPLE_URLS = new String[] {//
		"http://bobbinwork.googlegroups.com/web/torchon.xml?gda=M4C-rDwAAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURDSHAoqq6MVKgGExQWFmpupE",//$NON-NLS-1$
		"http://bobbinwork.googlegroups.com/web/spiders.xml?gda=4zTd7TwAAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURDScIXlog2Wd3wWxjd0fqC3o",//$NON-NLS-1$
		"http://bobbinwork.googlegroups.com/web/flanders.xml?gda=4z7qcT0AAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURDSmCdrehNs8EVCWMltc8R2k",//$NON-NLS-1$
		"http://bobbinwork.googlegroups.com/web/braid2.xml?gda=UQQDQzsAAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURDRVk4fsgKMsk2Mbh43_kAi9",//$NON-NLS-1$
		"http://bobbinwork.googlegroups.com/web/braid1.xml?gda=2VRhyDsAAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURDRD4DT6Fua1gH-xvy-DVSKm"};//$NON-NLS-1$
    
    private String[] choices = new String[SAMPLE_URLS.length];
	private BWVApplet parent;

    public SampleListener(BWVApplet parent) {
    	super();
    	this.parent = parent;
    	for (int i=0 ; i < SAMPLE_URLS.length ; i++){
    		choices[i] = SAMPLE_URLS[i].replaceAll("\\?.*", "").replaceAll(".*/", "");
    	}
    	// TODO enhancements to prepare in the background
    	// replace with actual links from http://groups.google.com/group/bobbinwork/files
    	// preload files 
    	// preload preview images for the drop down list
    }
    
    public void actionPerformed(ActionEvent e) {
    	// TODO turn list into ComboBox
		String s = (String)JOptionPane.showInputDialog(
                parent,
                getString("MenuFile_LoadSample_Prompt"), //$NON-NLS-1$
                getString("MenuFile_LoadSample_Caption"), //$NON-NLS-1$
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[2]);
		if ( s == null ) return;
		
    	for (int i=0 ; i < SAMPLE_URLS.length ; i++){
    		if ( SAMPLE_URLS[i].matches(".*"+s+".*")) {//$NON-NLS-1$ //$NON-NLS-2$
    			s = SAMPLE_URLS[i];
    			try {
   					parent.loadFromStream(s,(new URL(s)).openStream());
    			} catch (MalformedURLException e1) {
    				showError(s, e1, getString("MenuFile_LoadSample_invalid_URL"));
    			} catch (IOException e1) {
    				showError(s, e1, getString("MenuFile_LoadSample_IO_error"));
    			}
    		}
    	}
	}
    
	private void showError(String fileName, Exception exception, String action) {
	    JOptionPane.showMessageDialog(parent, //
	            fileName + "\n" + exception.getLocalizedMessage(),//
	            action, //
	            JOptionPane.ERROR_MESSAGE);
	}

}
