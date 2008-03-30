package nl.BobbinWork.grounds;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;

@SuppressWarnings("serial")
public class BaseGroundChooser extends JMenu {
	
    private static final String ERROR_CAPTION = "loading"; //$NON-NLS-1$
	private static final String BASIC_STITCHES = "nl/BobbinWork/diagram/xml/basicStitches.xml";
	private static final String PROCESSING_INSTRUCTION = "<?.*?>"; //$NON-NLS-1$
    private static final String END_TAG = "</diagram>"; //$NON-NLS-1$
	private final String source = loadFile();

	private static final int COUNT = 4;
	private static final String PREFIX = "MenuGround_"; //$NON-NLS-1$

	public BaseGroundChooser (final ActionListener externalActionListener){
    	super();
        applyStrings(this, "MenuGround_Choose"); //$NON-NLS-1$

		JMenuItem jMenuItem;
		Object [][] data = {
				{"flanders",50,50, 4},//$NON-NLS-1$
				{"spider",80,140, 6},//$NON-NLS-1$
				{"vierge",80,80, 4},//$NON-NLS-1$
				{"sGravenmoers",80,80,4}};//$NON-NLS-1$
		for (Object[] item : data) {
	        jMenuItem = new LocaleMenuItem(PREFIX+item[0]); 
	        jMenuItem.setActionCommand(assemble((String)item[0],(Integer)item[1],(Integer)item[2], (Integer)item[3]));
	        jMenuItem.addActionListener(externalActionListener);
	        add(jMenuItem);
		}
	}
	
	private String assemble(String groundID, int x, int y, int pairs) {
		String s = "";
		for (int i=0 ; i<COUNT ; i++){
			int xx = i*2*x;
			int yy = 0;
			int p = pairs*i+1; 
			for (int j=0 ; j<=i*2 ; j+=2){
				s = s + "<copy of='"+groundID+"' pairs='"+p+"-"+(p+pairs-1)+"'>"
				+"<move x='"+(xx)+"' y='"+(yy)+"'/></copy>\n";
				xx -= x;
				yy += y;
				p -= pairs/2;
			}			
		}
		System.out.println(s);
		return source+"<group pairs='1-16'>"+s+"</group>"+END_TAG; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String loadFile () {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(BASIC_STITCHES);
        char[] cbuf = new char[65*1024];
        try {
			(new InputStreamReader(stream)).read(cbuf);
			return new String(cbuf)
			.replaceFirst(END_TAG+".*", "") //$NON-NLS-1$ //$NON-NLS-2$
			.replaceFirst(PROCESSING_INSTRUCTION, ""); //$NON-NLS-1$
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					this, 
					e.getLocalizedMessage()+"\n", //$NON-NLS-1$
			        ERROR_CAPTION, 
			        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}
}
