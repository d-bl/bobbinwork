/* DiagramPanel.java Copyright 2006-2007 by J. Falkink-Pol
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

package nl.BobbinWork.diagram.gui;

import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.JOIN_MITER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import nl.BobbinWork.bwlib.gui.PrintMenu.PrintablePreviewer;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.model.Drawable;
import nl.BobbinWork.diagram.model.MultipleThreadsPartition;
import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.diagram.model.ThreadSegment;
import nl.BobbinWork.diagram.model.ThreadStyle;

/**
 * Bobbin lace Working diagram, a thread and/or pair diagram.
 * 
 * @author J. Falkink-Pol
 */
@SuppressWarnings("serial")
public class DiagramPanel extends JPanel implements PrintablePreviewer {

    private Color //
            areaHighlight = new Color(0xFF, 0xFF, 0x33, 0x77),//
            threadHighlight = new Color(0x00, 0xFF, 0x00, 0x77);

    private boolean //
            showThreads = true, //
            showPairs = false;

    private double screenScale = 1;

    /** the model for the diagram drawn on the panel */
    private Diagram diagram = null;

    /** Creates a new instance of DiagramPanel. */
    public DiagramPanel() {
    	setBackground(Color.white);
    }

    /** Registers whether threads and/or pairs are drawn. */
    public void setDiagramType(boolean showThreads, boolean showPairs) {
        this.showThreads = showThreads;
        this.showPairs = showPairs;
        repaint(getBounds());
    }

    /*
     * In Java Foundation Classes in a Nutshell, Flanagan tells us,
     * 
     * “When drawing to a screen or an off-screen image, X and Y coordinates are
     * measured in pixels. When drawing to a printer or other high-resolution
     * device, however, X and Y coordinates are measured in points instead of
     * pixels (and there are 72 points in one inch).”
     * 
     * Flanagan goes on to tell us
     * 
     * “By default, when drawing to a screen or image, user space is the same as
     * device space. However, the Graphics2D class defines methods that allow
     * you to trivially modify the default coordinate system... By default, when
     * drawing to the screen, one unit in user space corresponds to one pixel in
     * device space. The scale() method changes this. If you scale the
     * coordinate system by a factor of 10, one unit of user space corresponds
     * to 10 pixels in device space...”
     */
    private static final double PRINT_SCALE = 0.72;

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {

        if ((pi >= 1) || (diagram == null)) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        paintPartitions (g2,PRINT_SCALE);
        
        return Printable.PAGE_EXISTS;
    }

    public static void paintPartitions (Graphics2D g, Iterable<Drawable> drawables){
    	List<Drawable> pins = new Vector<Drawable>();
    	for (Drawable drawable:drawables){
            int width = drawable.getStyle().getWidth() * 1;
			if (width > 0) {
				g.setPaint(drawable.getStyle().getColor());
				g.setStroke(new BasicStroke(width, CAP_BUTT, JOIN_MITER));
				g.draw(drawable.getShape());
			} else {
				pins.add(drawable);
            }
    	}
    	for (Drawable drawable:pins){
    		g.setPaint(drawable.getStyle().getColor());
    		g.fill(drawable.getShape());
    	}
    }
    
    /** Sets the XML definition and redraws the diagram. */
    public void setPattern(Diagram diagram) {
        this.diagram = diagram;
        repaint();
    }

    /** Transforms the XML defintion into a drawing on the panel. */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (diagram != null) {
            // determine space available on paper (default A4 minus 25.4 mm margin)
        	int pw = (int) (pageFormat.getImageableWidth() / PRINT_SCALE);
        	int ph = (int) (pageFormat.getImageableHeight() / PRINT_SCALE);
            setPreferredSize(new Dimension((int) (pw * getScreenScale()), (int) (ph * getScreenScale())));

            // make the off-page area grey
            g2.setColor(new Color(0xDDDDDD));
            int sw = getSize().width;
            int sh = getSize().height;
            g2.fillRect((int) (pw * getScreenScale()), 0, sw - (int) (pw * getScreenScale()), sh);
            g2.fillRect(0, (int) (ph * getScreenScale()), sw, sh - (int) (ph * getScreenScale()));

            paintPartitions (g2,getScreenScale());
            revalidate();
        }
    }

	private void paintPartitions(Graphics2D g2, double scale) {
        g2.scale(scale, scale);
        if (scale > 1.5) { 
        	// more accurate but slower?
            g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        }
		if (showPairs) paintPartitions (g2,diagram.getPairs());
		if (showThreads) paintPartitions (g2,diagram.getThreads());
	}
	
    private Partition lastHigLight;

    /** Highlight a section of the diagram corresponding with a node of the tree. */
    public void highLight(Partition partition) {
    	
    	lastHigLight = partition; // remember for higlightThreadAt()
    	
        // clear previous highlights
        paintImmediately(getBounds());
        if (partition != null) {
            Shape shape = partition.getBounds();
            if (shape != null) {
                // set new highlight
                Graphics2D g2 = (Graphics2D) getGraphics();
                g2.scale(getScreenScale(), getScreenScale());
                g2.setPaint(areaHighlight);
                g2.fill(shape);
            }
        }
    }

    void highlightSwitchAt(int x, int y) {
    	MultipleThreadsPartition part = diagram.getSwitchAt(x, y);
    	highLight(part);
    }    
    
    /**
     * Highlights the thread segment at the specified position of the diagram
     * 
     * @param x
     *            horizontal offset from the left margin
     * @param y
     *            vertical offset from the top margin
     */
    void highlightThreadAt(int x, int y) {

        x /= getScreenScale();
        y /= getScreenScale();
        ThreadSegment threadSegment = diagram.getThreadAt(x, y);

        paintImmediately(getBounds());
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.scale(getScreenScale(), getScreenScale());

        if (lastHigLight != null) {
            g2.setPaint(areaHighlight);
            g2.fill(lastHigLight.getBounds());
        }
        if (threadSegment != null) {
            g2.setPaint(threadHighlight);
            g2.setStroke(new BasicStroke( //
                    threadSegment.getStyle().getShadow().getWidth() * 1.7f, //
                    BasicStroke.CAP_BUTT, //
                    BasicStroke.JOIN_MITER));
            if (threadSegment.getPrevious() != null) {
                g2.draw(((ThreadSegment) threadSegment.getPrevious()).getCurve());
            }
            g2.draw(threadSegment.getCurve());
            if (threadSegment.getNext() != null) {
                g2.draw(((ThreadSegment) threadSegment.getNext()).getCurve());
            }
        }
    }

    /**
     * Applies the style to the thread at the specified position of the diagram
     * 
     * @param threadStyle
     *            the style to apply
     * @param x
     *            horizontal offset from the left margin
     * @param y
     *            vertical offset fromthe top margin
     */
    public void setThreadStyleAt(ThreadStyle threadStyle, int x, int y) {

        if (showThreads) {
            x /= getScreenScale();
            y /= getScreenScale();
            ThreadSegment threadSegment = diagram.getThreadAt(x, y);
            if (threadSegment != null) {
                threadSegment.getStyle().apply(threadStyle);
                repaint();
            }
        }
    }

    /**
     * Gets the style of the thread at the specified position of the diagram
     * 
     * @param x
     *            horizontal offset from the left margin
     * @param y
     *            vertical offset fromthe top margin
     * @return null if the position is not at a thread in the diagram
     */
    public ThreadStyle getThreadStyleAt(int x, int y) {

        if (showThreads) {
            x /= getScreenScale();
            y /= getScreenScale();
            ThreadSegment threadSegment = diagram.getThreadAt(x, y);
            if (threadSegment != null) {
                return threadSegment.getStyle();
            }
        }
        return null;
    }

    /**
     * @param screenScale
     *            the screenScale to set
     */
    void setScreenScale(double screenScale) {
        this.screenScale = screenScale;
        repaint();
    }

    /**
     * @return the screenScale
     */
    double getScreenScale() {
        return screenScale;
    }

    Color getAreaHighlight() {
        return areaHighlight;
    }

    void setAreaHighlight(Color color) {
        this.areaHighlight = new Color(//
                color.getRed(), //
                color.getGreen(), //
                color.getBlue(), //
                areaHighlight.getAlpha());
    }

    Color getThreadHighlight() {
        return threadHighlight;
    }

    void setThreadHighlight(Color color) {
        this.threadHighlight = new Color(//
                color.getRed(), //
                color.getGreen(), //
                color.getBlue(), //
                threadHighlight.getAlpha());
    }

	private PageFormat pageFormat = new PageFormat();
	
	public void setPageFormat(PageFormat pageFormat) {
		this.pageFormat = pageFormat;
	}

}