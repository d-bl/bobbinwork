/* Visualizer.java Copyright 2009 by J. Pol
 *
 * This file is part of BobbinWork.
 *
 * BobbinWork is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BobbinWork is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BobbinWork.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.BobbinWork.diagram.model;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.BobbinWork.diagram.gui.DiagramPainter;

import org.junit.Ignore;

@Ignore("this is java application, not a JUnit")
public class SwitchesSelectedByPoint {
	
	private JFrame frame = new JFrame("visualizer"); 

	static class MyPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private final Iterable<Drawable> shapes;
		private final int scale;
		
		MyPanel(MultiplePairsPartition p, int scale) throws Exception {
        	this.scale = scale;
			shapes = p.getThreads();
        	setBackground(new Color(0xFFFFFF));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(scale, scale);
            g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            DiagramPainter.paint (g2,shapes);
        }
	}

    public SwitchesSelectedByPoint(MultiplePairsPartition part, int scale) throws Exception {
    	MyPanel myPanel = new MyPanel( part, scale );
    	frame.setSize(new Dimension(800, 300));
		frame.getContentPane().add( myPanel);
	}
    
    private static MultiplePairsPartition getPart(int caseNr, int variantNr)
    throws Exception {
    	Object[] testcases = PointsInSwitchesTest.data().toArray();
    	Object[] testCase = (Object[]) testcases[caseNr];
    	return (MultiplePairsPartition) testCase[variantNr];
    }

	public static void main (String[] args){
    	try {
    		int caseNr = 0, variantNr = 0, scale = 2;
			new SwitchesSelectedByPoint(getPart(caseNr, variantNr), scale).frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
