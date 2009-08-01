/* InteractiveDiagramPanel.java Copyright 2006-2007 by J. Pol
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

package nl.BobbinWork.diagram.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import nl.BobbinWork.bwlib.gui.CMenuBar;
import nl.BobbinWork.bwlib.gui.Localizer;
import nl.BobbinWork.bwlib.gui.PrintMenu;
import nl.BobbinWork.diagram.model.Point;

/**
 * A diagram panel together with a tool bar to interact with the diagram.
 *
 */
@SuppressWarnings("serial")
public class InteractiveDiagramPanel extends JPanel {

	private final DiagramPanel diagramPanel;
	private final ThreadStyleToolBar threadStyleToolBar;
	private Point selected = null;
	
    public InteractiveDiagramPanel(
    		final DiagramPanel panel, 
    		final JFrame parentForDialogs) 
    throws ParserConfigurationException, SAXException, IOException {
		
		super(new BorderLayout());
		
		threadStyleToolBar = new ThreadStyleToolBar();
		threadStyleToolBar.add(new GetButton());
		threadStyleToolBar.add(new SetButton());
		
		diagramPanel = panel;
        diagramPanel.setDiagramType(true, false);
        diagramPanel.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
            	selected = new Point(e.getX(), e.getY());
            	//diagramPanel.highlightSwitchAt(e.getX(), e.getY()); 
            	// too slow to traverse the tree twice?
            	diagramPanel.highlightThreadAt(e.getX(), e.getY());
            }
        });

        JMenu menuList[] = new JMenu[] { 
        		new PrintMenu(diagramPanel), 
        		new ViewMenu(diagramPanel, parentForDialogs, threadStyleToolBar)};
        
        add (new CToolBar( threadStyleToolBar, menuList ), PAGE_START);
        add(new JScrollPane(diagramPanel), CENTER);
	}
	
    private class MyButton extends JButton {
    	MyButton (String imageFileName) {
    		URL url = ThreadStyleToolBar.class.getResource(imageFileName);
			setIcon(new ImageIcon(url));
    		String baseFileName = imageFileName.replaceAll("\\..*", "");//$NON-NLS-1$//$NON-NLS-2$
			Localizer.applyStrings(this,"ThreadStyle_"+baseFileName);//$NON-NLS-1$
    	}
    }
    
    /** a button allowing the user to get the style of the selected thread */
    private class GetButton extends MyButton {
    	GetButton () {
    		super("pipette.gif");//$NON-NLS-1$
    		addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				if ( selected == null ) return;
    				threadStyleToolBar.setCoreStyle//
    				(diagramPanel.getThreadStyleAt((int)selected.getX(), (int)selected.getY()));
    			}});
    	}
    }
    
    /** a button allowing the user to apply the style to the selected thread */
    private class SetButton extends MyButton {
    	SetButton () {
    		super("pinsel.gif");//$NON-NLS-1$
    		addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				if ( selected == null ) return;
                    diagramPanel.setThreadStyleAt(//
                            threadStyleToolBar.getCoreStyle(), //
                            (int)selected.getX(), //
                            (int)selected.getY());
    			}});
    	}
    }
    
    private class CToolBar extends JToolBar {
        
    	public CToolBar(JToolBar toolBar, JMenu[] menu) {
            setFloatable(false);
            setRollover(true);
            setBorder(null);

            add(new CMenuBar(menu));
            // add(Box.createHorizontalGlue()); // would hide most on ubuntu
            add(toolBar);
    	}
    }
}
