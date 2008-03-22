package nl.BobbinWork.bwlib.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * A menu with at least an about item.
 */
@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	
    /**
     * @param parent
     * @param details
     * @param simpleCaption
     */
    public HelpMenu(final Component parent, final String years, final String simpleCaption) {

    	this.versionedCaption = "BobbinWork - " + simpleCaption + " " //$NON-NLS-1$ //$NON-NLS-2$
    		+ this.getClass().getPackage().getImplementationVersion();

    	final String description = "<html> "
    		+ Localizer.getString("MenuHelp_release") + " "
    		+ simpleCaption + "<br>Copyright © " 
    		+ years + " " 
    		+ "J. Falkink-Pol"  
    		+ "<hr>" 
    		+ Localizer.getString("MenuHelp_About_License").replaceAll("\\n", "<br>")
    		+ "<hr>" 
    		+ Localizer.getString("MenuHelp_About_URLs").replaceAll("\\n", "<br>")
    		+ "</html>" 
    		;

    	applyStrings(this, "MenuHelp_help"); //$NON-NLS-1$

        JMenuItem//

        jMenuItem = new LocaleMenuItem("MenuHelp_About"); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(parent, description, simpleCaption,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(jMenuItem);
    }

	private String versionedCaption;
	public String getVersionedCaption() {
		return versionedCaption;
	}

}
