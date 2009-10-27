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
import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagram;

import java.awt.*;
import java.net.URL;
import java.util.Locale;

import javax.swing.*;

import nl.BobbinWork.bwlib.gui.*;
import nl.BobbinWork.diagram.gui.*;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.viewer.guiUtils.*;

import org.junit.Ignore;

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

      final Diagram model = createDiagram( DIAGRAM );
      final DiagramPanel canvas = new DiagramPanel( model );
      final DiagramTree tree = new DiagramTree( model );
      final JComponent menuBar = new CMenuBar( //
          // TODO file menu listeners
          new LocaleMenu( true, "MenuFile_file",//
              new LocaleMenuItem( "MenuFile_open" ), // 
              new LocaleMenuItem( "MenuFile_Download" ) ), //
          new HelpMenu( null, "2009", "BobbinWork diagrams" ),//
          new HeapStatusWidget() //
          );
      final JComponent diagramToolbar = new CMenuBar( //
          new PrintMenu( canvas ), //
          new ViewMenu( canvas, null, null ), //
          new ThreadStyleToolBar(), //
          new PlaceHolder( 6, 1 ), //
          // TODO listeners and hints, same looks as colour choosers
          new JButton( createIcon( "pipette.gif" ) ), // 
          new JButton( createIcon( "pinsel.gif" ) ) // 
          );
      final JComponent treeToolbar = new CMenuBar( //
          // TODO listener(s), arrange buttons below one another
          new LocaleButton( false, "TreeToolBar_copy" ), // 
          new LocaleButton( false, "TreeToolBar_paste" ), // 
          new LocaleButton( false, "TreeToolBar_delete" ), // 
          new LocaleButton( false, "TreeToolBar_showHide" ), //
          new ClipBoard( 80, 80 ) //
          );
      final JComponent splitPane = new CSplitPane( //
          frame.getBounds().width / 3, // dividerPosition
          HORIZONTAL_SPLIT, // orientation
          new CPanel( new JScrollPane( tree ), treeToolbar ), // left
          new CPanel( new JScrollPane( canvas ), diagramToolbar ) // right
          );
      new DiagramTreeLink( tree, canvas );
      frame.getContentPane().add( new CPanel( splitPane, menuBar ) );
      frame.setVisible( true );
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static class PlaceHolder
      extends JComponent
  {
    PlaceHolder(int width, int height)
    {
      setMaximumSize( new Dimension( width, height ) );
    }
  }

  private static class ClipBoard
      extends JPanel
  {

    ClipBoard(int width, int height)
    {
      Dimension dim = new Dimension( width, height );
      setPreferredSize( dim );
      setMaximumSize( dim );
      // TODO hint
      applyStrings( this, "Clipboard" );
      setBackground( new Color( 0xFFFFFF ) );
      setBorder( BorderFactory.createLoweredBevelBorder() );
    }

    public void paintComponent(
        Graphics g)
    {

      super.paintComponent( g );
      Graphics2D g2 = (Graphics2D) g;
      // TODO listen to copy button, then show tree.selected.userObject
      // DiagramPanel.paintPartitions (g2,twist.getThreads());
    }
  }

  private static Icon createIcon(
      String imageFileName)
  {
    URL url = ThreadStyleToolBar.class.getResource( imageFileName );
    return new ImageIcon( url );
  }
}
