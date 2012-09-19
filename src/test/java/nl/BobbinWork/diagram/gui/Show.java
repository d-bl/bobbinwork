package nl.BobbinWork.diagram.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.BobbinWork.bwlib.gui.BWFrame;
import nl.BobbinWork.diagram.model.Drawable;
import nl.BobbinWork.diagram.model.Knot;
import nl.BobbinWork.diagram.model.Partition;

public class Show
{
  private static final String BUNDLE = "nl/BobbinWork/diagram/gui/labels";
  private static Iterable<Drawable> drawables;

  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    Panel()
    {
      setBackground( Color.white );
    }
    
    public void paint(
        final Graphics g)
    {
      g.clearRect( 0, 0, getWidth(), getHeight() );
      if (drawables == null) return;
      DiagramPainter.paint( (Graphics2D) g, drawables );
    }
  }

  public static void main(
      final String[] args)
  {
    setBundle( BUNDLE );
    try {
      final JFrame frame = new BWFrame();
      final JPanel panel = new Panel();
      frame.add( panel );
      frame.setVisible( true );

      show( panel, Knot.rightKnot() );
//      for (Ground d : Ground.values()) {
//        show( panel, DiagramBuilder.createDiagramModel( d.diamondInputStream() ) );
//        show( panel, DiagramBuilder.createDiagramModel( d.squareInputStream() ) );
//      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private static void show(
      final JPanel panel,
      Partition diagram) throws InterruptedException
  {
    drawables = diagram.getPairs();
    panel.repaint();
    Thread.sleep( 2000 );
    drawables = diagram.getThreads();
    panel.repaint();
    Thread.sleep( 2000 );
  }
}
