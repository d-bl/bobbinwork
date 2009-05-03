package nl.BobbinWork.diagram.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import static nl.BobbinWork.bwlib.gui.Localizer.getBundle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.bwlib.gui.CMenuBar;
import nl.BobbinWork.bwlib.gui.PrintMenu;
import nl.BobbinWork.diagram.model.Point;

/**
 * A diagram panel together with a tool bar to interact with the diagram.
 *
 */
@SuppressWarnings("serial")
public class InteractiveDiagramPanel extends JPanel {

	private final DiagramPanel diagramPanel;
	private Point selected = null;
	private final ThreadStyleToolBar threadStyleToolBar = new ThreadStyleToolBar(getBundle());
	
    public InteractiveDiagramPanel(
    		final DiagramPanel diagramPanel, 
    		final JFrame viewer) 
    throws ParserConfigurationException {
		
		super(new BorderLayout());
		
		this.diagramPanel = diagramPanel;
		
        diagramPanel.setDiagramType(true, false);
        diagramPanel.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
            	selected = new Point(e.getX(), e.getY());
            	diagramPanel.highlightSwitchAt(e.getX(), e.getY());
            	diagramPanel.highlightThreadAt(e.getX(), e.getY());
            }
        });

        JMenu menuList[] = new JMenu[] { 
        		new PrintMenu(diagramPanel), 
        		new ViewMenu(diagramPanel, viewer, threadStyleToolBar)};
        
        add (new CToolBar( threadStyleToolBar, menuList ), PAGE_START);
        add(new JScrollPane(diagramPanel), CENTER);
	}
	
    private class GetButton extends JButton {
    	GetButton (String name) {
    		super(name);
            addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if ( selected == null ) return;
                    threadStyleToolBar.setStyleOfFrontThread//
                         (diagramPanel.getThreadStyleAt((int)selected.getX(), (int)selected.getY()));
				}});
    	}
    }
    
    private class SetButton extends JButton {
    	SetButton (String name) {
    		super(name);
    		addActionListener(new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				if ( selected == null ) return;
                    diagramPanel.setThreadStyleAt(//
                            threadStyleToolBar.getStyleOfFrontThread(), //
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
            add(Box.createHorizontalGlue());
            add(toolBar);
            add(new GetButton("get"));
            add(new SetButton("apply"));
    	}
    }
}
