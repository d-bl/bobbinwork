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
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;
import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagram;

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
      final BWFrame frame = new BWFrame( BUNDLE );

      final Diagram model = createDiagram( DIAGRAM );
      final DiagramPanel canvas = new DiagramPanel( model );
      final DiagramTree tree = new DiagramTree( model );
      new DiagramTreeLink( tree, canvas );
      final SplitPane splitPane = new SplitPane( //
          frame.getBounds().width / 3, // dividerPosition
          HORIZONTAL_SPLIT, // orientation
          new CPanel( new JScrollPane( tree ), createTreeTools( tree ) ), // left
          new CPanel( new JScrollPane( canvas ), createDiagramTools( canvas ) ) // right
          );
      // TODO top border; status bar menu usage
      frame.getContentPane().add(
          new CPanel( splitPane, createTools( tree, canvas ) ) );
      frame.setVisible( true );
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static JComponent[] createDiagramTools(
      final DiagramPanel canvas)
      throws SAXException, IOException, ParserConfigurationException
  {
    return new JComponent[] {
        new CMenuBar( new JMenu[] {
            new PrintMenu( canvas ), //
            new ViewMenu( canvas, null, null )
        } ), //
        new ThreadStyleToolBar(), //
        new JButton( createIcon( "pipette.gif" ) ), // TODO listener/hint
        new JButton( createIcon( "pinsel.gif" ) ), // TODO listener/hint
    // listeners should somehow connect to DiagramTreeLink
    };
  }

  private static JComponent[] createTreeTools(
      DiagramTree tree)
  { // TODO on top of one another; preview of "copied" node
    return new JComponent[] { // TODO listeners (properties for paste/delete)
        new LocaleButton( false, "TreeToolBar_copy" ), // 
        new LocaleButton( false, "TreeToolBar_paste" ), //
        new LocaleButton( false, "TreeToolBar_delete" ), //
        new LocaleButton( false, "TreeToolBar_showHide" ), //
    };
  }

  private static JComponent[] createTools(
      DiagramTree tree,
      final DiagramPanel canvas)
  {
    return new JComponent[] { // TODO listeners
        new LocaleButton( false, "MenuFile_open" ), // 
        new LocaleButton( false, "MenuFile_New" ), //
        new LocaleButton( false, "MenuFile_ChooseSample" ), //
        // TODO help
    };
  }

  private static Icon createIcon(
      String imageFileName)
  {
    URL url = ThreadStyleToolBar.class.getResource( imageFileName );
    return new ImageIcon( url );
  }
}
