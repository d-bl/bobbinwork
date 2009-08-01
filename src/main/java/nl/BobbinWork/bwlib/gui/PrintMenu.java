/* PrintMenu.java Copyright 2006-2007 by J. Pol
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

package nl.BobbinWork.bwlib.gui;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_P;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


/**
 * A menu to print the diagram
 *
 */
@SuppressWarnings("serial")
public
class PrintMenu extends JMenu {
	
    private PrinterJob printJob = null;
    private PageFormat pageFormat = null;
    private PrintablePreviewer printablePreviewer;
    
    public interface PrintablePreviewer extends Printable {
    	public void setPageFormat(PageFormat pageFormat);
    }

	private boolean initPrint() {
		
		if (printJob != null && pageFormat != null) return true;
		try {
			printJob = PrinterJob.getPrinterJob();
    		pageFormat = printJob.defaultPage();
    		return true;
    	} catch (Exception e) {
    		printJob = null;
    		pageFormat = null;
    		return false;
    	}
	}
	
    /** Print the diagram with a chance to adjust options or cancel. */
    private void adjustablePrint() {
    	if ( ! initPrint() )  return;
        printJob.setPrintable(printablePreviewer, pageFormat);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /** Changes the page format with a dialog. */
    private void updatePageFormat() {
    	
    	if ( initPrint() ) {
    		pageFormat = printJob.pageDialog(pageFormat);
    		printablePreviewer.setPageFormat(pageFormat);
    	}
    }

    /** Creates a fully dressed JMenu, to print the diagram */
    public PrintMenu(final PrintablePreviewer printablePreviewer) {

    	this.printablePreviewer = printablePreviewer;
    	
        applyStrings(this, "MenuPrint"); //$NON-NLS-1$

        JMenuItem jMenuItem ;
        
        jMenuItem = new LocaleMenuItem("MenuPrint_PageSetup"); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                updatePageFormat();
            }
        });
        add(jMenuItem);

        jMenuItem = new LocaleMenuItem( "MenuPrint_print", VK_P, CTRL_DOWN_MASK); //$NON-NLS-1$
        jMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                adjustablePrint();
            }
        });
        add(jMenuItem);
    }
}
