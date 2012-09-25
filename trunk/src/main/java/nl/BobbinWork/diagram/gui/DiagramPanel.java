/* DiagramPanel.java Copyright 2006-2007 by J. Pol
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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;

import javax.swing.JPanel;

import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.model.MultipleThreadsPartition;
import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.diagram.model.ThreadSegment;
import nl.BobbinWork.diagram.model.ThreadStyle;

/**
 * Bobbin lace Working diagram, a thread and/or pair diagram.
 * 
 * @author J. Pol
 */
@SuppressWarnings("serial")
public class DiagramPanel
    extends JPanel
{

  private Partition lastHigLight;

  private ThreadSegment highlightedThread;

  private Color //
      areaHighlight = new Color( 0xFF, 0xFF, 0x33, 0x77 ),//
      threadHighlight = new Color( 0x00, 0xFF, 0x00, 0x77 );

  private boolean //
      showThreads = true, //
      showPairs = false;

  private double screenScale = 1;

  /** the model for the diagram drawn on the panel */
  private Diagram diagram = null;

  /** Creates a new instance of DiagramPanel. */
  public DiagramPanel()
  {
    setBackground( Color.white );
    getAccessibleContext().setAccessibleName(
        getString( "Diagram_Accessible_Name" ) );
  }

  public DiagramPanel(Diagram model)
  {
    this();
    if (model == null) return;
    setPattern( model );
  }

  /** Registers whether threads and/or pairs are drawn. */
  public void setDiagramType(
      boolean showThreads,
      boolean showPairs)
  {
    this.showThreads = showThreads;
    this.showPairs = showPairs;
    repaint( getBounds() );
  }

  /** Sets the XML definition and redraws the diagram. */
  public void setPattern(
      Diagram diagram)
  {
    this.diagram = diagram;
    repaint();
  }

  /** Transforms the XML defintion into a drawing on the panel. */
  public void paintComponent(
      Graphics g)
  {

    super.paintComponent( g );

    if (diagram != null) {
      // determine space available on paper (default A4 minus 25.4 mm margin)
      // int pw = (int) (pageFormat.getImageableWidth() / PRINT_SCALE);
      // int ph = (int) (pageFormat.getImageableHeight() / PRINT_SCALE);
      // setPreferredSize( new Dimension( (int) (pw * getScreenScale()),
      // (int) (ph * getScreenScale()) ) );

      paintPartitions( (Graphics2D) g, getScreenScale() );
      revalidate();
    }
  }

  private void paintPartitions(
      Graphics2D g2,
      double scale)
  {
    g2.scale( scale, scale );
    g2.setRenderingHint( KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR );
    g2.setRenderingHint( KEY_ANTIALIASING, VALUE_ANTIALIAS_ON );
    if (showThreads) DiagramPainter.paint( g2, diagram.getThreads() );
    if (showPairs) DiagramPainter.paint( g2, diagram.getPairs() );
  }

  /** Highlight a section of the diagram corresponding with a node of the tree. */
  public void highlight(
      Partition partition)
  {

    lastHigLight = partition; // remember for higlightThreadAt()

    // clear previous highlights
    paintImmediately( getBounds() );
    if (partition != null) {
      Shape shape = partition.getBounds();
      if (shape != null) {
        // set new highlight
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.scale( getScreenScale(), getScreenScale() );
        g2.setPaint( areaHighlight );
        g2.fill( shape );
      }
    }
  }

  void highlightSwitchAt(
      int x,
      int y)
  {
    MultipleThreadsPartition part = diagram.getSwitchAt( x, y );
    highlight( part );
  }

  public void highlight(
      ThreadSegment threadSegment)
  {
    highlightedThread = threadSegment;
    paintImmediately( getBounds() );
    Graphics2D g2 = (Graphics2D) getGraphics();
    g2.scale( getScreenScale(), getScreenScale() );

    if (lastHigLight != null) {
      g2.setPaint( areaHighlight );
      g2.fill( lastHigLight.getBounds() );
    }
    if (threadSegment != null) {
      g2.setPaint( threadHighlight );
      g2.setStroke( new BasicStroke( //
          threadSegment.getStyle().getShadow().getWidth() * 1.7f, //
          BasicStroke.CAP_BUTT, //
          BasicStroke.JOIN_MITER ) );
      for (CubicCurve2D curve : threadSegment.getThread()) {
        g2.draw( curve );
      }
    }
  }

  /**
   * Applies the style to the thread at the specified position of the diagram
   * 
   * @param threadStyle
   *          the style to apply
   * @param x
   *          horizontal offset from the left margin
   * @param y
   *          vertical offset fromthe top margin
   */
  public void setThreadStyleAt(
      ThreadStyle threadStyle,
      int x,
      int y)
  {

    if (showThreads) {
      x /= getScreenScale();
      y /= getScreenScale();
      ThreadSegment threadSegment = diagram.getThreadAt( x, y );
      if (threadSegment != null) {
        threadSegment.getStyle().apply( threadStyle );
        repaint();
      }
    }
  }

  /**
   * Gets the style of the thread at the specified position of the diagram
   * 
   * @param x
   *          horizontal offset from the left margin
   * @param y
   *          vertical offset from the top margin
   * @return null if the position is not at a thread in the diagram
   */
  public ThreadStyle getThreadStyleAt(
      int x,
      int y)
  {

    if (showThreads) {
      x /= getScreenScale();
      y /= getScreenScale();
      ThreadSegment threadSegment = diagram.getThreadAt( x, y );
      if (threadSegment != null) {
        return threadSegment.getStyle();
      }
    }
    return null;
  }

  /**
   * @param screenScale
   *          the screenScale to set
   */
  void setScreenScale(
      double screenScale)
  {
    this.screenScale = screenScale;
    repaint();
  }

  /**
   * @return the screenScale
   */
  double getScreenScale()
  {
    return screenScale;
  }

  public MultipleThreadsPartition getPartitionAt(
      MouseEvent event)
  {

    final int x = (int) (event.getX() / getScreenScale());
    final int y = (int) (event.getY() / getScreenScale());

    if (getDiagram() == null) return null;
    return getDiagram().getPartitionAt( x, y );
  }

  Color getAreaHighlight()
  {
    return areaHighlight;
  }

  void setAreaHighlight(
      Color color)
  {
    this.areaHighlight = new Color( //
        color.getRed(), //
        color.getGreen(), //
        color.getBlue(), //
        areaHighlight.getAlpha() );
  }

  Color getThreadHighlight()
  {
    return threadHighlight;
  }

  void setThreadHighlight(
      Color color)
  {
    this.threadHighlight = new Color( //
        color.getRed(), //
        color.getGreen(), //
        color.getBlue(), //
        threadHighlight.getAlpha() );
  }

  public Diagram getDiagram()
  {
    return diagram;
  }

  public ThreadSegment getSelectedThread()
  {
    return highlightedThread;
  }

  public boolean isThreadsVisible()
  {
    return showThreads;
  }

}