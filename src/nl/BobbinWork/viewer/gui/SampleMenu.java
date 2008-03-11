package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

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

@SuppressWarnings("serial")
public class SampleMenu extends JMenu {

	private static final String BASE_URL = "http://bobbinwork.googlegroups.com/web/";
	private static final String S = "AAAAYixsiBs_Jr-a7N6OS_3XDppDSmRoLLcG9UYL7B9ILBWG1qiJ7UbTIup-M2XPURD";
    private static final String[] SAMPLE_URLS = new String[] {//
         "torchon.xml?gda=M4C-rDw" + S + "SHAoqq6MVKgGExQWFmpupE",//$NON-NLS-1$
    	 "spiders.xml?gda=4zTd7Tw" + S + "ScIXlog2Wd3wWxjd0fqC3o",//$NON-NLS-1$
    	"flanders.xml?gda=4z7qcT0" + S + "SmCdrehNs8EVCWMltc8R2k",//$NON-NLS-1$
    	  "braid2.xml?gda=UQQDQzs" + S + "RVk4fsgKMsk2Mbh43_kAi9",//$NON-NLS-1$
    	  "braid1.xml?gda=2VRhyDs" + S + "RD4DT6Fua1gH-xvy-DVSKm"};//$NON-NLS-1$
    
    private BWVApplet parent;
    private SampleMenu self = this;
    
	private void loadSample(String s) {
		InputStream openStream;
		try {
				openStream = (new URL(s)).openStream();
		} catch (MalformedURLException e1) {
		    JOptionPane.showMessageDialog(parent, //
		            s + "\n" + e1.getLocalizedMessage(),//
		            getString("MenuFile_LoadSample_invalid_URL"), //
		            JOptionPane.ERROR_MESSAGE);
		    return;
		} catch (IOException e1) {
			JOptionPane.showInputDialog(
					parent,
	                getString("ExcuseUs") + "\n\n" + //$NON-NLS-1$
	                getString("MenuFile_PasteSample_Prompt"), //$NON-NLS-1$
	                s+"\n");
			return;
		}
		parent.loadFromStream(s,openStream); //TODO eliminate coupling
	}
    
	public SampleMenu (BWVApplet parent) {
    	super();
		this.parent = parent;
        applyStrings(this, "MenuFile_LoadSample"); //$NON-NLS-1$

    	JMenuItem //
        jMenuItem = new JMenuItem(getString("MenuFile_ChooseSample"));
    	jMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				loadSample((String)JOptionPane.showInputDialog(
		                self.parent,
		                getString("MenuFile_ChooseSample_Prompt"), //$NON-NLS-1$
		                "http://"));  //$NON-NLS-1$
			}
    	});
        add(jMenuItem);
        
        add(new JSeparator());

		ActionListener al = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadSample(e.getActionCommand());
        	}
        };

        for (int i=0 ; i < SAMPLE_URLS.length ; i++){
            jMenuItem = new JMenuItem(SAMPLE_URLS[i].replaceAll("\\?.*", "")); //$NON-NLS-1$ //$NON-NLS-2$
            jMenuItem.setActionCommand(BASE_URL + SAMPLE_URLS[i]);
            jMenuItem.addActionListener( al );
            add(jMenuItem);
    	}
    	// TODO enhancements to prepare in the background
    	// replace with actual links from http://groups.google.com/group/bobbinwork/files
    	// cache files 
    	// cache preview images for the drop down list
	}
}
