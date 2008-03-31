package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import nl.BobbinWork.bwlib.io.InputStreamCreator;


@SuppressWarnings("serial")
public class GroundChooser extends JMenu {
	
	public GroundChooser (final ActionListener externalActionListener){
		
		super();
		applyStrings(this, "MenuGround_Choose"); //$NON-NLS-1$

		JMenuItem[] data = {
				new GroundMenuItem("flanders",55,55, 4, 2),//$NON-NLS-1$
				new GroundMenuItem("spider",80,140, 6, 3),//$NON-NLS-1$
				//new GroundMenuItem("spider",136,160, 6, 2),//$NON-NLS-1$
				new GroundMenuItem("vierge",80,80, 4, 2),//$NON-NLS-1$
				new GroundMenuItem("sGravenmoers",80,80,4, 2)};//$NON-NLS-1$
		
		for (final JMenuItem item : data) {
			item.addActionListener(new ActionListener () {

				public void actionPerformed(ActionEvent e) {
					e.setSource(new StreamCreator("ground.xml",item.getActionCommand()));
					externalActionListener.actionPerformed(e);					
				}
				});
			add(item);
		}
	}
	
	private class StreamCreator implements InputStreamCreator {
		
		String name;
		String content;
		
		StreamCreator (String name, String stream) {
			this.name = name;
			this.content = stream;
		}
		
		public InputStream getInputStream() {
			return new ByteArrayInputStream(content.getBytes());
		}

		public String getInputStreamName() {
			return name;
		}
		
	}
	
	private class GroundMenuItem extends JMenuItem {
		
		GroundMenuItem (String groundID, int x, int y, int pairs, int pairShift) {
			
			String s = "";
			for (int i=0 ; i<4 ; i++){
				int xx = i*2*x;
				int yy = 0;
				int p = pairs*i+1; 
				for (int j=0 ; j<=i*2 ; j+=2){
					s = s + "<copy of='"+groundID+"' pairs='"+p+"-"+(p+pairs-1)+"'>"
					+"<move x='"+(xx)+"' y='"+(yy)+"'/></copy>\n";
					xx -= x;
					yy += y;
					p -= pairShift;
				}			
			}
			//System.out.println(s);
			s = "<group pairs='1-" + (pairs*4) + "'>\n" + s + "</group>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			s = "<?xml version='1.0' encoding='UTF-8'?>\n" //$NON-NLS-1$
				+"<diagram\n  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" //$NON-NLS-1$
				+"\n  xsi:noNamespaceSchemaLocation='http://www.xs4all.nl/~falkink/lace/BobbinWork/bw.xsd'\n>\n" //$NON-NLS-1$
				+ s + "</diagram>"; //$NON-NLS-1$
			setActionCommand( s ); 
	        applyStrings(this, "MenuGround_"+groundID); //$NON-NLS-1$
		}
	}
}
