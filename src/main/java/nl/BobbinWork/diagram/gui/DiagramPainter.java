package nl.BobbinWork.diagram.gui;

import static java.awt.BasicStroke.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.InsetsUIResource;

import nl.BobbinWork.diagram.model.*;

public class DiagramPainter
{
  /**
   * Prepares a renderer to show the full drawing. If the height of the drawing
   * is too big to fit between the borders of the panel, the drawing is scaled
   * down to fit. If the drawing is smaller than the container, it is centred
   * supposing the shape is located in the top level corner of the canvas.
   * 
   * @param graphics
   *          the original renderer
   * @param shape
   *          a container of the expected drawing, typically
   *          {@link Partition#getBounds()}
   * @param panel
   *          the panel going to contain the drawing
   * @return a copy of graphics object with additional transformations (note
   *         that transformations are permanent changes)
   */
  public static Graphics2D fit(
      final Graphics graphics,
      final Shape shape,
      final JPanel panel)
  {
    final int avaiableHeight;
    final Rectangle bounds = shape.getBounds();
    final Insets insets;
    if (panel.getBorder() == null) {
      avaiableHeight = panel.getSize().height;
      insets = new InsetsUIResource( 0, 0, 0, 0 );
    } else {
      insets = panel.getBorder().getBorderInsets( panel );
      avaiableHeight = panel.getSize().height - insets.bottom - insets.top;
    }
    final double scale = (((double) avaiableHeight) / ((double) bounds.height));
    double dx = -bounds.x + insets.left;
    double dy = -bounds.y + insets.top;

    final Graphics2D g2 = (Graphics2D) graphics.create();
    if (scale >= 1) {// put in the center
      dy += (avaiableHeight - bounds.height) / 2D;
      dx += (avaiableHeight - bounds.width) / 2D;
    } else {
      g2.scale( scale, scale );
    }
    g2.translate( dx, dy );
    return g2;
  }

  /**
   * Draws a diagram or a section of it. Pins are drawn after the other types of
   * objects so they appear on top of the threads or pairs.
   * 
   * @param graphics2D
   *          a renderer for 2D shapes
   * @param drawables
   *          all the objects of the diagram to draw. In case of threads, all
   *          shadows and cores must come in the proper order to create the
   *          visual under/over effect.
   */
  public static void paint(
      final Graphics2D graphics2D,
      final Iterable<Drawable> drawables)
  {
    final List<Drawable> pins = new Vector<Drawable>();
    for (final Drawable drawable : drawables) {
      final int width = drawable.getStyle().getWidth() * 1;
      if (width > 0) {
        graphics2D.setPaint( drawable.getStyle().getColor() );
        graphics2D.setStroke( new BasicStroke( width, CAP_BUTT, JOIN_MITER ) );
        graphics2D.draw( drawable.getShape() );
      } else {
        pins.add( drawable );
      }
    }
    for (Drawable drawable : pins) {
      graphics2D.setPaint( drawable.getStyle().getColor() );
      graphics2D.fill( drawable.getShape() );
    }
  }

}
