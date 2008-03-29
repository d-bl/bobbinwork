package nl.BobbinWork.grounds;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Locale;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

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
    private static final String DIAGRAM = "nl/BobbinWork/grounds/grounds.xml"; //$NON-NLS-1$
    private static final String BASIC_STITCHES = "nl/BobbinWork/diagram/xml/basicStitches.xml"; //$NON-NLS-1$
    private static final String ICON = "nl/BobbinWork/viewer/gui/bobbin.gif"; //$NON-NLS-1$

    private static final String END_TAG = "</diagram>";
	private final String source = 
		loadFile(BASIC_STITCHES).replaceFirst(END_TAG,"")//$NON-NLS-1$
		+ loadFile(DIAGRAM).replaceFirst("<diagram[^>]*>","");//$NON-NLS-1$//$NON-NLS-2$

    public Grounds () {
    	super (BUNDLE_NAME);
	}
    
	public static void main(String[] args) {

        BWApplet applet = new BWApplet(BUNDLE_NAME);
        if (args.length > 0) setBundle(BUNDLE_NAME, new Locale(args[0]));
        applet.init();
        wrapInApplicationFrame(applet,caption, ICON);
    }

	public void init() {

		final DiagramPanel diagramPanel = new DiagramPanel();
			
		JMenuBar jMenuBar = new JMenuBar();
		jMenuBar.add( createChooser(diagramPanel));
        jMenuBar.add( createHelpMenu() );
        setJMenuBar(jMenuBar);
		
		getContentPane().add(new SplitPane(//
				50, // dividerPosition
                HORIZONTAL_SPLIT, // orientation
                jMenuBar, // left component of spiltPane
                new InteractiveDiagramPanel(diagramPanel, this)));	// right component
    }

	private BaseGroundChooser createChooser(final DiagramPanel diagramPanel) {
		return new BaseGroundChooser( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				diagramPanel.setPattern(loadDiagram(e.getActionCommand()));
			}});
	}

	private HelpMenu createHelpMenu() {
		HelpMenu helpMenu = new HelpMenu(this, YEARS,caption);
		caption = helpMenu.getVersionedCaption();
		return helpMenu;
	}
	
	private Diagram loadDiagram(String choice) {
		
		try {
			String s = source.replaceFirst(END_TAG, choice+END_TAG);
			System.out.println(s);
			Element root = (new TreeBuilder(false)).build(s);
			root = root.getOwnerDocument().getDocumentElement();
			System.out.println(""+new Date());
			TreeExpander.parse(root);
			System.out.println(""+new Date());
			return new Diagram(root);
		} catch (Exception e) {
	        showError(e);
		}
		return null;
	}

	private String loadFile (String file) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(file);
        char[] cbuf = new char[(int)new File(file).length()];
        try {
			(new InputStreamReader(stream)).read(cbuf);
			return new String(cbuf).replaceFirst("<?.*?>", "");//$NON-NLS-1$//$NON-NLS-2$
		} catch (IOException e) {
	        showError(e);
		}
		return null;
	}

	private void showError(Exception e) {
		
		JOptionPane.showMessageDialog(
				this, 
				e.getLocalizedMessage()+"\n", 
		        "loading", //$NON-NLS-1$
		        JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
}
