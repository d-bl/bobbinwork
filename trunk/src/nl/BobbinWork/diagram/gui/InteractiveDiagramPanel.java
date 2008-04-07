package nl.BobbinWork.diagram.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
import static nl.BobbinWork.bwlib.gui.Localizer.getBundle;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import nl.BobbinWork.bwlib.gui.CMenuBar;
import nl.BobbinWork.viewer.gui.BWViewer;

/**
 * A diagram panel together with a tool bar to interact with the diagram.
 *
 */
@SuppressWarnings("serial")
public class InteractiveDiagramPanel extends JPanel {

	private DiagramPanel diagramPanel;
	
    public InteractiveDiagramPanel(
    		final DiagramPanel diagramPanel, 
    		final BWViewer viewer) {
		
		super(new BorderLayout());
		
		this.diagramPanel = diagramPanel;
		
        diagramPanel.setDiagramType(true, false);
        diagramPanel.addMouseMotionListener(mouseMotionListener);

		final ThreadStyleToolBar threadStyleToolBar = new ThreadStyleToolBar(getBundle());
		
        diagramPanel.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (isLeftMouseButton(e)) {
                    diagramPanel.setThreadStyleAt(//
                            threadStyleToolBar.getStyleOfFrontThread(), //
                            e.getX(), //
                            e.getY());
                } else if (isRightMouseButton(e)) {
                    threadStyleToolBar.setStyleOfFrontThread//
                            (diagramPanel.getThreadStyleAt(e.getX(), e.getY()));
                }
            }
        });

        JMenu menuList[] = new JMenu[] { 
        		new PrintMenu(diagramPanel), 
        		new ViewMenu(diagramPanel, viewer, threadStyleToolBar, mouseMotionListener) };
        
        add (new CToolBar( threadStyleToolBar, menuList ), PAGE_START);
        add(new JScrollPane(diagramPanel), CENTER);

	}
	
    private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

        public void mouseDragged(MouseEvent arg0) {
            // no dragging
        }

        public void mouseMoved(MouseEvent e) {
            diagramPanel.highlightThreadAt(e.getX(), e.getY());
        }

    };

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
