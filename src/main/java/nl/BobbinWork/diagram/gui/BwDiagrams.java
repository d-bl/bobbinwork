/* BWViewer.java Copyright 2009 by J. Pol
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

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;
import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagramModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.bwlib.gui.BWFrame;
import nl.BobbinWork.bwlib.gui.HeapStatusWidget;
import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.gui.Localizer;
import nl.BobbinWork.bwlib.gui.PrintMenu;
import nl.BobbinWork.diagram.gui.EditForm.DiagramReplacedListener;
import nl.BobbinWork.diagram.model.Diagram;

import org.xml.sax.SAXException;

/**
 * @author J. Pol
 * 
 */
public class BwDiagrams
{
  static final String NEW_LINE = System.getProperty( "line.separator" );
  private static final String BUNDLE = "nl/BobbinWork/diagram/gui/labels";

  public static void main(
      final String[] args)
  {
    try {
      if (args.length > 0) {
        setBundle( BUNDLE, new Locale( args[0] ) );
      } else {
        setBundle( BUNDLE );
      }
      // must come before any swing operation for the system look and feel
      final BWFrame frame = new BWFrame();

      final JButton pipette = createThreadStyleButton( "pipette" );
      final JButton pinsel = createThreadStyleButton( "pinsel" );

      final DiagramPanel canvas = new DiagramPanel();
      final DiagramTree tree = new DiagramTree();
      new SelectionListener( tree, canvas, pipette, pinsel );

      final ActionListener loadListener = createLoadListener( tree, canvas );
      final ActionListener streamListener = createStreamListener( tree, canvas );
      final DiagramReplacedListener changeListener =
          createChangeListener( tree, canvas );
      final EditForm editForm = new EditForm( canvas, changeListener );
      tree.addTreeSelectionListener( editForm );

      final JComponent left = createBorderPanel();
      left.add( editForm, NORTH );
      left.add( new JScrollPane( tree ), CENTER );

      final JComponent right = createBorderPanel();
      right.add( createDiagramTools( canvas, pipette, pinsel ), NORTH );
      right.add( new JScrollPane( canvas ), CENTER );

      final JComponent splitPane =
          new JSplitPane( HORIZONTAL_SPLIT, left, right );

      final JPanel mainPanel = createBorderPanel();
      final JComponent mainMenu =
          createMainMenu( frame, streamListener, loadListener, new ExportMenu(canvas) );
      mainPanel.add( mainMenu, NORTH );
      mainPanel.add( splitPane, CENTER );

      frame.getContentPane().add( mainPanel );
      frame.setVisible( true );

    } catch (final Exception exception) {
      exception.printStackTrace();
    }
  }

  private static DiagramReplacedListener createChangeListener(
      final DiagramTree tree,
      final DiagramPanel canvas)
  {
    return new EditForm.DiagramReplacedListener()
    {
      public void rebuild(
          final Diagram newDiagram)
      {
        canvas.setPattern( newDiagram );
        tree.setDiagramModel( newDiagram );
      }
    };
  }

  private static JComponent createDiagramTools(
      final DiagramPanel canvas,
      final JButton pipette,
      final JButton pinsel)
      throws SAXException, IOException, ParserConfigurationException
  {
    final ThreadStyleToolBar threadStyleToolBar = new ThreadStyleToolBar();
    pipette.addActionListener( createPipetteListener( canvas,
        threadStyleToolBar ) );
    pinsel
        .addActionListener( createPinselListener( canvas, threadStyleToolBar ) );

    threadStyleToolBar.add( Box.createRigidArea( new Dimension( 5, 0 ) ), 0 );
    threadStyleToolBar.add( pinsel, 0 );
    threadStyleToolBar.add( pipette, 0 );

    final JComponent diagramTools = new JMenuBar();
    diagramTools.add( new PrintMenu( canvas ) );
    diagramTools.add( new ViewMenu( canvas, null, null ) );
    diagramTools.add( threadStyleToolBar );
    return diagramTools;
  }

  private static ActionListener createPinselListener(
      final DiagramPanel canvas,
      final ThreadStyleToolBar threadStyleToolBar)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        if (canvas.getSelectedThread() == null) return;
        canvas.getSelectedThread().getStyle().apply(
            threadStyleToolBar.getCoreStyle() );
        canvas.repaint();
      }
    };
  }

  private static ActionListener createPipetteListener(
      final DiagramPanel canvas,
      final ThreadStyleToolBar threadStyleToolBar)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        if (canvas.getSelectedThread() == null) return;
        threadStyleToolBar.setCoreStyle( canvas.getSelectedThread().getStyle() );
      }
    };
  }

  private static class MenuBar
      extends JMenuBar
  {
    private static final long serialVersionUID = 1L;

    void addMenuItem(
        final ActionListener clipboardExporter,
        final String subMenuKey,
        final String mainMenuKey)
    {
      final JMenu exportMenu = new JMenu();
      final JMenuItem toClipbaord = new LocaleMenuItem( subMenuKey );
      applyStrings( exportMenu, mainMenuKey );
      exportMenu.add( toClipbaord );
      toClipbaord.addActionListener( clipboardExporter );
      add( exportMenu );
    }
  }

  private static JComponent createMainMenu(
      final BWFrame frame,
      final ActionListener inputStreamListener,
      final ActionListener clipboardImporter,
      final ExportMenu exportMenu)
  {
    final MenuBar menuBar = new MenuBar();
    menuBar.addMenuItem( clipboardImporter, "From_clipboard", "Import_menu" );
    menuBar.add( exportMenu );
    menuBar.add( new SampleMenu( menuBar, inputStreamListener ) );
    menuBar.add( new GroundMenu( inputStreamListener ) );
    menuBar.add( new HelpMenu( frame, "2009", "diagrams" ) );
    menuBar.add( new HeapStatusWidget() );
    return menuBar;
  }

  private static ActionListener createLoadListener(
      final DiagramTree tree,
      final DiagramPanel canvas)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        final byte[] bytes = getClipboardContents().getBytes();
        setDiagramModel( tree, canvas, new ByteArrayInputStream( bytes ) );
      }
    };
  }

  /**
   * Get the String residing on the system clip board.
   * 
   * @return any text found on the Clipboard; if none found, return an empty
   *         String.
   */
  private static String getClipboardContents()
  {
    final Transferable contents =
        Toolkit.getDefaultToolkit().getSystemClipboard().getContents( null );
    final boolean hasTransferableText =
        (contents != null)
            && contents.isDataFlavorSupported( DataFlavor.stringFlavor );
    if (hasTransferableText) {
      try {
        return (String) contents.getTransferData( DataFlavor.stringFlavor );
      } catch (final UnsupportedFlavorException exception) {
        // highly unlikely since we are using a standard DataFlavor
        System.out.println( exception );
        exception.printStackTrace();
      } catch (final IOException ex) {
        System.out.println( ex );
        ex.printStackTrace();
      }
    }
    return "";
  }

  private static ActionListener createStreamListener(
      final DiagramTree tree,
      final DiagramPanel canvas)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        final InputStream inputStream = (InputStream) event.getSource();
        setDiagramModel( tree, canvas, inputStream );
      }

    };
  }

  private static void setDiagramModel(
      final DiagramTree tree,
      final DiagramPanel canvas,
      final InputStream inputStream)
  {
    try {
      final Diagram model = createDiagramModel( inputStream );
      tree.setDiagramModel( model );
      //System.out.println(XmlResources.toXmlString( ((Element)model.getSourceObject()).getOwnerDocument() ));
      canvas.setPattern( model );

      tree.setSelectionRow(0);
      tree.requestFocus();
    } catch (final Exception exception) {
      showError( canvas, exception );
    }
  }

  private static void showError(
      final JComponent parent,
      final Exception exception)
  {
    final String stackTrace = stackTraceToString( exception );

    final String title = Localizer.getString( "Load_error_caption" ); // $NON-NLS-1$
    if (exception instanceof SAXException) {
      final String message =
          exception.getClass().getName() + NEW_LINE
              + exception.getLocalizedMessage();
      JOptionPane.showMessageDialog( parent, message, title,
          JOptionPane.ERROR_MESSAGE );
    } else {
      final String message =
          exception.getLocalizedMessage() + NEW_LINE + stackTrace;
      JOptionPane.showMessageDialog( parent, message, title,
          JOptionPane.ERROR_MESSAGE );
    }
  }

  static String stackTraceToString(
      final Throwable exception)
  {
    final StringWriter stringWriter = new StringWriter();
    final PrintWriter printWriter = new PrintWriter( stringWriter );
    exception.printStackTrace( printWriter );
    printWriter.flush();
    stringWriter.flush();

    final String stackTrace = stringWriter.toString()//
        .replaceAll( "(?m)^\\tat java.awt[ -~]*[\\r\\n]+", "" )//
        .replaceAll( "(?m)^\\tat javax.swing[ -~]*[\\r\\n]+", "" );
    return stackTrace;
  }

  private static JPanel createBorderPanel()
  {
    return new JPanel( new BorderLayout() );
  }

  private static JButton createThreadStyleButton(
      final String name)
  {
    final URL url = ThreadStyleToolBar.class.getResource( name + ".gif" );
    final JButton button = new JButton( new ImageIcon( url ) );
    Localizer.applyStrings( button, "ThreadStyle_" + name );
    button.setEnabled( false );
    button.setRequestFocusEnabled( false );
    return button;
  }
}
