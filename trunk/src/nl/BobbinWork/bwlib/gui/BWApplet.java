package nl.BobbinWork.bwlib.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class BWApplet extends JApplet {

	public BWApplet (String bundle) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(790, 500));
        setBundle(bundle);
	}
	
    /** @return true if the applet is framed in an application */
	public boolean wrappedInApplicationFrame() {
    	
    	try { 
    		getDocumentBase(); 
    		return false; 
    	} catch (NullPointerException e) {}
    	return true;
    }

	public static void wrapInApplicationFrame(BWApplet applet, String caption, String icon) {
    	
        URL iocnURL = applet.getClass().getClassLoader().getResource(icon);
		JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(caption);
		frame.setIconImage(applet.getToolkit().getImage(iocnURL));
        frame.add(applet);
        frame.setVisible(true);
	}
}
