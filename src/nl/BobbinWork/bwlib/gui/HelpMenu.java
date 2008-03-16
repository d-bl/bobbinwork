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
     * @param caption
     */
    public HelpMenu(final Component parent, final String years, final String caption) {

    	this.caption = "BobbinWork - " + caption + " " //$NON-NLS-1$ //$NON-NLS-2$
    		+ this.getClass().getPackage().getImplementationVersion();

    	final String description = "<html> "
    		+ Localizer.getString("MenuHelp_release") + " "
    		+ caption + "<br>Copyright © " 
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
                JOptionPane.showMessageDialog(parent, description, caption,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(jMenuItem);
    }

	private String caption;
	public String getCaption() {
		return caption;
	}

}
