package nl.BobbinWork.bwlib.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class BWFrame extends JFrame {

	protected static final String ICON = "nl/BobbinWork/bwlib/gui/bobbin.gif";

	public BWFrame (String bundle) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(790, 500));
        setBundle(bundle);
        URL iocnURL = getClass().getClassLoader().getResource(ICON);
		setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(getToolkit().getImage(iocnURL));
	}
}
