package nl.BobbinWork.grounds;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;

@SuppressWarnings("serial")
public class BaseGroundChooser extends JMenu {
	
	private static final int COUNT = 4;
	private static final String PREFIX = "MenuGround_";

	public BaseGroundChooser (final ActionListener externalActionListener){
    	super();
        applyStrings(this, "MenuGround_Choose"); //$NON-NLS-1$

		JMenuItem jMenuItem;
		Object [][] data = {
				{"flanders",60,60, 4},//$NON-NLS-1$
				{"spider",80,140, 6},//$NON-NLS-1$
				{"vierge",80,80, 4},//$NON-NLS-1$
				{"sGravenmoers",80,80,4}};//$NON-NLS-1$
		for (Object[] item : data) {
	        jMenuItem = new LocaleMenuItem(PREFIX+item[0]); //$NON-NLS-1$
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
		return "<group pairs='1-16'>"+s+"</group>";
	}
}
