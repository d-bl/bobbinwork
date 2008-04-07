package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import nl.BobbinWork.bwlib.io.NamedInputStream;


@SuppressWarnings("serial")
public class GroundChooser extends JMenu {
	
	public GroundChooser (final ActionListener externalActionListener){
		
		super();
		applyStrings(this, "MenuGround_Choose"); //$NON-NLS-1$

		JMenuItem[] data = {
				new GroundMenuItem("vierge",80,80, 4, 2),//$NON-NLS-1$
				new GroundMenuItem("sGravenmoers",80,80,4, 2),//$NON-NLS-1$
				new GroundMenuItem("spider",80,140, 6, 3),//$NON-NLS-1$
				new GroundMenuItem("flanders",55,55, 4, 2),//$NON-NLS-1$
				new GroundMenuItem("snowflake",136,100, 6, 4)};//$NON-NLS-1$

		for (final JMenuItem item : data) {
			item.addActionListener(new ActionListener () {

				public void actionPerformed(ActionEvent e) {
					ByteArrayInputStream is = new ByteArrayInputStream(item.getActionCommand().getBytes());
					e.setSource(new NamedInputStream("ground.xml",is));
					externalActionListener.actionPerformed(e);					
				}
			});
			add(item);
		}
	}
	
	private class GroundMenuItem extends JMenuItem {
		
		GroundMenuItem (String groundID, int x, int y, int pairs, int pairShift) {
			
			final int diagonalRows = 4;
			int p = (diagonalRows - 1) * pairShift * 2 + pairs + 1;
			String s = "<copy of='"+groundID+"' pairs='"+p+"-"+(p+pairs-1)+"'/>\n";
			for (int i=0 ; i<diagonalRows ; i++){
				int xx = (diagonalRows-1)*x+i*x;
				int yy = i*y;
				p = pairShift*(diagonalRows+i-1)+1; 
				for (int j=0 ; j<diagonalRows && p>0; j++){
					s = s + "<copy of='"+groundID+"' pairs='"+p+"-"+(p+pairs-1)+"'>"
					+"<move x='"+(xx)+"' y='"+(yy)+"'/></copy>\n";
					xx -= x;
					yy += y;
					p -= pairShift;
				}			
			}
			//System.out.println(s);
			s = "<?xml version='1.0' encoding='UTF-8'?>\n" //$NON-NLS-1$ 
				+ "<diagram>\n<group pairs='1-" //$NON-NLS-1$
				+ (pairShift*2*diagonalRows+pairs-pairShift) + "'>\n" //$NON-NLS-1$
				+ "<title/>\n" // magically it makes the name of the stitch appear in the treeView $NON-NLS-1$
				+ s
				+ "</group>\n</diagram>"; //$NON-NLS-1$ 
			setActionCommand( s );
	        applyStrings(this, "MenuGround_"+groundID); //$NON-NLS-1$
		}
	}
}
