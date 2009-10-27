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

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static nl.BobbinWork.bwlib.gui.Localizer.*;
import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagramModel;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.bwlib.gui.*;
import nl.BobbinWork.diagram.gui.*;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.viewer.guiUtils.*;

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
  private static final String DIAGRAM =
      "<copy of='ctctc' pairs='1-2'><move x='10' y='10'/></copy>";

  private static final String BUNDLE = "nl/BobbinWork/viewer/gui/labels";

  public static void main(
      final String[] args)
  {
    if (args.length > 0) setBundle( BUNDLE, new Locale( args[0] ) );
    try {
      // must be first for the system look and feel
      final BWFrame frame = new BWFrame( BUNDLE );

      final Diagram model = createDiagramModel( DIAGRAM );

      final DiagramPanel canvas = new DiagramPanel( model );
      final DiagramTree tree = new DiagramTree( model );
      final TreeToolBar treeToolBar = new TreeToolBar();

      tree.addTreeSelectionListener( treeToolBar );
      new DiagramTreeLink( tree, canvas );

      final int dividerposition = frame.getBounds().width / 3;
      final CPanel left = new CPanel( new JScrollPane( tree ), treeToolBar );
      final CPanel right =
          new CPanel( new JScrollPane( canvas ), createDiagramTools( canvas ) );
      final JComponent splitPane =
          new CSplitPane( dividerposition, HORIZONTAL_SPLIT, left, right );

      frame.getContentPane().add( new CPanel( splitPane, createMenu() ) );
      frame.setVisible( true );

    } catch (Exception exception) {
      exception.printStackTrace();
    }
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
    // TODO listeners and hints, same looks as colour choosers
    diagramTools.add( new JButton( createIcon( "pipette.gif" ) ) );
    diagramTools.add( new JButton( createIcon( "pinsel.gif" ) ) );
    return diagramTools;
  }

  private static JComponent createMenu()
  {
    // TODO listeners
    final JMenu fileMenu = new JMenu();
    applyStrings( fileMenu, "MenuFile_file" );
    fileMenu.add( new LocaleMenuItem( "MenuFile_open" ) );
    fileMenu.add( new LocaleMenuItem( "MenuFile_Download" ) );

    final JComponent menuBar = new JMenuBar();
    menuBar.add( fileMenu );
    menuBar.add( new HelpMenu( null, "2009", "diagrams" ) );
    menuBar.add( new HeapStatusWidget() );
    return menuBar;
  }

  private static Icon createIcon(
      String imageFileName)
  {
    URL url = ThreadStyleToolBar.class.getResource( imageFileName );
    return new ImageIcon( url );
  }
}
