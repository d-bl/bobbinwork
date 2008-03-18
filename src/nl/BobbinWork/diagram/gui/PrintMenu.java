package nl.BobbinWork.diagram.gui;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_P;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

/**
 * A menu to print the diagram
 *
 */
@SuppressWarnings("serial")
class PrintMenu extends JMenu {
	
    /** Creates a fully dressed JMenu, to print the diagram */
    public PrintMenu(final DiagramPanel diagramPanel) {

        applyStrings(this, "MenuPrint"); //$NON-NLS-1$

        add(new javax.swing.JSeparator());
        JMenuItem//
        jMenuItem = new LocaleMenuItem("MenuPrint_PageSetup"); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.updatePageFormat();
            }
        });
        add(jMenuItem);

        jMenuItem = new LocaleMenuItem( "MenuPrint_print", VK_P, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagramPanel.adjustablePrint();
            }
        });
        add(jMenuItem);
    }

}
