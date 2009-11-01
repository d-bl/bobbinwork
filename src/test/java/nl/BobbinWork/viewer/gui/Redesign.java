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
package nl.BobbinWork.viewer.gui;

import static java.awt.BorderLayout.*;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;
import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagramModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.bwlib.gui.*;
import nl.BobbinWork.bwlib.io.NamedInputStream;
import nl.BobbinWork.diagram.gui.*;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.viewer.guiUtils.FileMenu;

import org.junit.Ignore;
import org.xml.sax.SAXException;

/**
 * Prototype for the new version of the viewer.
 * 
 * @author Joke Pol
 * 
 */
@Ignore("this is java application, not a JUnit test")
public class Redesign
{
  private static final String BUNDLE = "nl/BobbinWork/viewer/gui/labels";

  public static void main(
      final String[] args)
  {
    if (args.length > 0) setBundle( BUNDLE, new Locale( args[0] ) );
    try {
      // must be first for the system look and feel
      final BWFrame frame = new BWFrame( BUNDLE );

      final DiagramPanel canvas = new DiagramPanel();
      final DiagramTree tree = new DiagramTree();
      new DiagramTreeLink( tree, canvas );

      final JPanel mainPanel = createBorderPanel();
      mainPanel.add(
          createMainMenu( frame, createFileListener( tree, canvas ) ), NORTH );
      mainPanel.add( createSplitPane( canvas, tree ), CENTER );

      frame.getContentPane().add( mainPanel );
      frame.setVisible( true );

    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static JComponent createSplitPane(
      final DiagramPanel canvas,
      final DiagramTree tree)
      throws SAXException, IOException, ParserConfigurationException
  {
    final TreeToolBar treeToolBar = new TreeToolBar( canvas );
    tree.addTreeSelectionListener( treeToolBar );

    final JPanel left = createBorderPanel();
    left.add( treeToolBar, NORTH );
    left.add( new JScrollPane( tree ), CENTER );

    final JPanel right = createBorderPanel();
    right.add( createDiagramTools( canvas ), NORTH );
    right.add( new JScrollPane( canvas ), CENTER );

    return new JSplitPane( HORIZONTAL_SPLIT, left, right );
  }

  private static JComponent createDiagramTools(
      final DiagramPanel canvas)
      throws SAXException, IOException, ParserConfigurationException
  {
    final JComponent diagramTools = new JMenuBar();
    diagramTools.add( new PrintMenu( canvas ) );
    diagramTools.add( new ViewMenu( canvas, null, null ) );
    diagramTools.add( new ThreadStyleToolBar() );
    diagramTools.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
    diagramTools.add( createIconButton( "pipette.gif" ) );
    diagramTools.add( createIconButton( "pinsel.gif" ) );
    return diagramTools;
  }

  private static JComponent createMainMenu(
      final BWFrame frame,
      final ActionListener inputStreamListener)
  {
    final JComponent menuBar = new JMenuBar();
    menuBar.add( new FileMenu( inputStreamListener, null, null ) );
    menuBar.add( new SampleDiagramChooser( menuBar, inputStreamListener ) );
    menuBar.add( new GroundChooser( inputStreamListener ) );
    menuBar.add( new HelpMenu( frame, "2009", "diagrams" ) );
    menuBar.add( new HeapStatusWidget() );
    return menuBar;
  }

  private static ActionListener createFileListener(
      final DiagramTree tree,
      final DiagramPanel canvas)
  {
    return new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        try {
          final NamedInputStream source = (NamedInputStream) event.getSource();
          final Diagram model = createDiagramModel( source.getStream() );
          tree.setDiagramModel( model );
          canvas.setPattern( model );
        } catch (SAXException exception) {
          // TODO Auto-generated catch block
          exception.printStackTrace(); // error in xml content
        } catch (IOException exception) {
          // TODO Auto-generated catch block
          exception.printStackTrace(); // environment/user error
        } catch (XPathExpressionException exception) {
          exception.printStackTrace(); // programming error
        } catch (ParserConfigurationException exception) {
          exception.printStackTrace(); // programming error
        }
      }
    };
  }

  private static JPanel createBorderPanel()
  {
    return new JPanel( new BorderLayout() );
  }

  private static JButton createIconButton(
      final String imageFileName)
  {
    final URL url = ThreadStyleToolBar.class.getResource( imageFileName );
    return new JButton( new ImageIcon( url ) );
  }
}
