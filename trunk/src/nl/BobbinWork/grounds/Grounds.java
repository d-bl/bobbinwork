package nl.BobbinWork.grounds;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nl.BobbinWork.bwlib.gui.BWApplet;
import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.SplitPane;
import nl.BobbinWork.diagram.gui.DiagramPanel;
import nl.BobbinWork.diagram.gui.InteractiveDiagramPanel;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.xml.TreeBuilder;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class Grounds extends BWApplet {

	private static final String YEARS = "2006-2008";  //$NON-NLS-1$  
	private static String caption = "Grounds"; // gets extended by the help menu  //$NON-NLS-1$  

    private static final String BUNDLE_NAME = "nl/BobbinWork/viewer/gui/labels"; //$NON-NLS-1$

    public Grounds () {
    	super (BUNDLE_NAME);
	}
    
	public static void main(String[] args) {

        if (args.length > 0) setBundle(BUNDLE_NAME, new Locale(args[0]));
        wrapInApplicationFrame(new Grounds(),caption);
    }

	public void init() {

		final DiagramPanel diagramPanel = new DiagramPanel();
			
		getContentPane().add(new SplitPane(//
				150, // dividerPosition
                HORIZONTAL_SPLIT, // orientation
                new Customizer(diagramPanel), // left component of spiltPane
                new InteractiveDiagramPanel(diagramPanel, this)));	// right component
    }
	
	private class Customizer extends JPanel {
		
		Customizer (DiagramPanel mainDiagramPanel) {

			super(new BorderLayout());
			
			final JPanel stitchChooser = new JPanel();
			stitchChooser.add(new javax.swing.JLabel("<html><br>select a ground<br>from the menu<br><br>this space is <br>reserved to change<br> individual stitches<br> but that is not yet<br> implemented</html>"));//$NON-NLS-1$

			JMenuBar jMenuBar = new JMenuBar();
			jMenuBar.add( createChooser(mainDiagramPanel, stitchChooser));
	        jMenuBar.add( createHelpMenu() );
	        
	        add( jMenuBar, PAGE_START );
	        add( stitchChooser, CENTER );
		}
	}

	private BaseGroundChooser createChooser(
			final DiagramPanel mainDiagramPanel,
			final JPanel stitchChooser) {

		return new BaseGroundChooser( new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				mainDiagramPanel.setPattern(loadDiagram(e.getActionCommand()));
				// TODO something like 
				// partition(..).draw(stitchChoosooser.getGraphics(), true, false);
			}});
	}

	private HelpMenu createHelpMenu() {
		
		HelpMenu helpMenu = new HelpMenu(this, YEARS,caption);
		caption = helpMenu.getVersionedCaption();
		return helpMenu;
	}
	
	private Diagram loadDiagram(String choice) {
		
		try {
			Element root = (new TreeBuilder(false)).build(choice);
			root = root.getOwnerDocument().getDocumentElement();
			System.out.println(""+new Date());
			TreeExpander.parse(root);
			System.out.println(""+new Date());
			return new Diagram(root);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					this, 
					e.getLocalizedMessage()+"\n", 
			        "loading", //$NON-NLS-1$
			        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}
}
