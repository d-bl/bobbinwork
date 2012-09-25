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
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.bwlib.gui.BWFrame;
import nl.BobbinWork.bwlib.gui.ExceptionHandler;
import nl.BobbinWork.bwlib.gui.HeapStatusWidget;
import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.Localizer;
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
      final ExceptionHandler exceptionHandler = new ExceptionHandler(frame);
      final JButton pipette = createThreadStyleButton( "pipette" );
      final JButton pinsel = createThreadStyleButton( "pinsel" );

      final DiagramPanel canvas = new DiagramPanel();
      final DiagramTree tree = new DiagramTree();
      new SelectionListener( tree, canvas, pipette, pinsel );

      final EditForm editForm =
          new EditForm( canvas, createChangeListener( tree, canvas ) );
      tree.addTreeSelectionListener( editForm );

      final JComponent left = createBorderPanel();
      left.add( editForm, NORTH );
      left.add( new JScrollPane( tree ), CENTER );

      final JComponent right = createBorderPanel();
      right.add( createDiagramTools( canvas, pipette, pinsel ), NORTH );
      right.add( new JScrollPane( canvas ), CENTER );

      final DiagramLoader diagramLoader = new DiagramLoader( tree, canvas, exceptionHandler );
      final JMenuBar mainMenu = new MainMenuHelper(diagramLoader, exceptionHandler).get();
      mainMenu.add( new ExportMenu( canvas , exceptionHandler) );
      mainMenu.add( new HelpMenu( frame, "2009", "diagrams" ) );
      mainMenu.add( new HeapStatusWidget() );

      final JPanel mainPanel = createBorderPanel();
      mainPanel.add( mainMenu, NORTH );
      mainPanel.add( new JSplitPane( HORIZONTAL_SPLIT, left, right ), CENTER );

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
        canvas.getSelectedThread().getStyle()
            .apply( threadStyleToolBar.getCoreStyle() );
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
