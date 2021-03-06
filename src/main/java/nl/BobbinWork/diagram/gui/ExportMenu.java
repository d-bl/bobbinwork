package nl.BobbinWork.diagram.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.bwlib.gui.*;
import nl.BobbinWork.diagram.conversion.ToImages;
import nl.BobbinWork.diagram.xml.DiagramRebuilder;

public class ExportMenu
    extends JMenu
{
    private static final long serialVersionUID = 1L;
private final ActionListener toImagesListener;
private final ExceptionHandler exceptionHandler;

  public ExportMenu(final DiagramPanel canvas, ExceptionHandler exceptionHandler)
  {
    this.exceptionHandler = exceptionHandler;
    applyStrings( this, "Export_menu" );

    final JMenuItem toClipbaord = new LocaleMenuItem( "To_clipboard" );
    toClipbaord.addActionListener( createToClipboardListener( canvas ) );
    add( toClipbaord );

    final List<String> formats = ToImages.formats();
    toImagesListener = newToImagesListener( canvas );

    add( createImageMenu( formats, "pairSnapShot" ) );
    add( createImageMenu( formats, "pairSlides" ) );
    add( createImageMenu( formats, "threadSnapShot" ) );
    add( createImageMenu( formats, "threadSlides" ) );
  }

  private JMenuItem createImageMenu(
      final List<String> formats,
      final String keyBase)
  {
    final JMenuItem slides = new JMenu();
    final String toolTip = "<html><body>"+Localizer.getString("To_" + keyBase + "_hint")+"</body></html>";
    applyStrings( slides, "To_" + keyBase );
    for (final String format : formats) {
      final String file = keyBase + "." + format;
      final JMenuItem item = new JMenuItem( file );
      item.addActionListener( toImagesListener );
      item.setToolTipText( toolTip );
      slides.add( item );
    }
    return slides;
  }

  private ActionListener newToImagesListener(
      final DiagramPanel canvas)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        final String fileName =
            ((JMenuItem) event.getSource()).getActionCommand();
        final ToImages toImages = newToImages( canvas, fileName );
        final Dimension size =
            toImages.getAnimatedPartition().getBounds().getBounds().getSize();
        try {
          createImages( fileName, toImages, size );
        } catch (final IllegalArgumentException e) {
          exceptionHandler.show( e, "could not write image"  );
        } catch (final IOException e) {
          exceptionHandler.show( e, "could not write image" );
        }
      }
    };
  }

  private static void createImages(
      final String fileName,
      final ToImages toImages,
      final Dimension size) throws IOException
  {
    if (fileName.toLowerCase().contains( "slides" )) {
      toImages.createSlides( size, fileName );
    } else {
      final RenderedImage image = toImages.newImage( size );
      final String extension = fileName.replaceAll( ".*\\.", "" );
      ImageIO.write( image, extension, new File( fileName ) );
    }
  }

  private static ToImages newToImages(
      final DiagramPanel canvas,
      final String fileName)
  {
    ToImages toImages;
    if (fileName.toLowerCase().contains( "thread" )) {
      toImages = ToImages.threads( canvas.getDiagram() );
    } else {
      toImages = ToImages.pairs( canvas.getDiagram() );
    }
    return toImages;
  }

  private ActionListener createToClipboardListener(
      final DiagramPanel canvas)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        String s;
        try {
          s = DiagramRebuilder.toString( canvas.getDiagram() );
        } catch (final TransformerException exception) {
          s = exceptionHandler.toString( exception );
        } catch (final XPathExpressionException exception) {
          s = exceptionHandler.toString( exception );
        }
        final StringSelection ss = new StringSelection( s );
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents( ss, null );
      }
    };
  }
}
