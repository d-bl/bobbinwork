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
import javax.swing.JSplitPane;

import nl.BobbinWork.diagram.gui.DiagramPanel;

public class Visualizer {
    private static final int TEST_CASE_NR = 0;
	private JFrame frame = new JFrame("visualizer"); 

	static class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		MultiplePairsPartition partition = null;
		int x,y;
		MyPanel(MultiplePairsPartition p, int x, int y) throws Exception {
        	partition = p;
        	this.x = x;
        	this.y = y;
        	setBackground(new Color(0xFFFFFF));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(4, 4);
            g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            Iterable<Drawable> shapes = partition.getThreads();
			DiagramPanel.paintPartitions (g2,shapes);
			// TODO draw (x,y) + highLight (see DiagramPanel.highlightThreadAt())
			//Shape highLight = partition.getSwitchAt(x,y).getBounds();
        }
	}

    public static void main (String[] args){
    	try {
    		Object[] testCase = (Object[]) PointsInSwitches.data().toArray()[TEST_CASE_NR];
			MultiplePairsPartition partition = (MultiplePairsPartition) testCase[0];
			Visualizer visualizer = new Visualizer();
			MyPanel panelA = new MyPanel (partition, (Integer) testCase[1], (Integer) testCase[2]);
			MyPanel panelB = new MyPanel (partition, (Integer) testCase[3], (Integer) testCase[3]);
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelA,panelB);
			splitPane.setDividerLocation(400);
			visualizer.frame.getContentPane().add(splitPane);
			visualizer.frame.setSize(new Dimension(800, 300));
			visualizer.frame.setVisible(true);
			System.out.println("xxx");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
