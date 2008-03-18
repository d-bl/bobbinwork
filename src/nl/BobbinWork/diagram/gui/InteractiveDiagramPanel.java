package nl.BobbinWork.diagram.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;

import java.awt.BorderLayout;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import nl.BobbinWork.bwlib.gui.CMenuBar;
import nl.BobbinWork.diagram.gui.ViewMenu;

/**
 * A diagram panel together with a tool bar to interact with the diagram.
 *
 */
@SuppressWarnings("serial")
public class InteractiveDiagramPanel extends JPanel {

    public InteractiveDiagramPanel(
    		final DiagramPanel diagramPanel, 
    		final JApplet parent,
    		final ThreadStyleToolBar threadStyleToolBar,
    		final MouseMotionListener mouseMotionListener) {
		
		super(new BorderLayout());
		
        JMenu menuList[] = new JMenu[] { 
        		new PrintMenu(diagramPanel), 
        		new ViewMenu(diagramPanel, 
        	    		parent,
        	    		threadStyleToolBar,
        	    		mouseMotionListener) };
        
        add (new CToolBar( threadStyleToolBar, menuList ), PAGE_START);
        add(new JScrollPane(diagramPanel), CENTER);

	}
	
    private class CToolBar extends JToolBar {
        
    	public CToolBar(JToolBar toolBar, JMenu[] menu) {
            setFloatable(false);
            setRollover(true);
            setBorder(null);

            add(new CMenuBar(menu));
            add(Box.createHorizontalGlue());
            add(toolBar);
        }
    	
    }
}
