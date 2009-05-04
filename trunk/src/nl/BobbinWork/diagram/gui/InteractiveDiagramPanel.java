package nl.BobbinWork.diagram.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import static nl.BobbinWork.bwlib.gui.Localizer.getBundle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;

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
	
    private class MyButton extends JButton {
    	MyButton (String imageFileName) {
    		URL url = ThreadStyleToolBar.class.getResource(imageFileName);
			setIcon(new ImageIcon(url));
    		String baseFileName = imageFileName.replaceAll("\\..*", "");//$NON-NLS-1$//$NON-NLS-2$
			Localizer.applyStrings(this,"ThreadStyle_"+baseFileName);//$NON-NLS-1$
    	}
    }
    
    private class GetButton extends MyButton {
    	GetButton () {
    		super("pipette.gif");//$NON-NLS-1$
    		addActionListener(new ActionListener(){
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				if ( selected == null ) return;
    				threadStyleToolBar.setStyleOfFrontThread//
    				(diagramPanel.getThreadStyleAt((int)selected.getX(), (int)selected.getY()));
    			}});
    	}
    }
    
    private class SetButton extends MyButton {
    	SetButton () {
    		super("pinsel.gif");//$NON-NLS-1$
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
            add(new GetButton());
            add(new SetButton());
    	}
    }
}
