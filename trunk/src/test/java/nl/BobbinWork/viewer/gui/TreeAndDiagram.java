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

import java.util.*;

import javax.swing.JScrollPane;

import nl.BobbinWork.bwlib.gui.BWFrame;
import nl.BobbinWork.diagram.gui.DiagramPanel;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.viewer.guiUtils.SplitPane;

import org.junit.Ignore;

/**
 * Show a tree and diagram in a frame for a visual test based on the diagram model.
 * 
 * @author Joke Pol
 * 
 */
@Ignore("this is java application, not a JUnit test")
public class TreeAndDiagram
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
      final DiagramTree tree = new DiagramTree( model );
      final DiagramPanel canvas = new DiagramPanel( model );
      new DiagramTreeLink( tree, canvas );

      frame.getContentPane().add( new SplitPane( //
          frame.getBounds().width / 3, // dividerPosition
          HORIZONTAL_SPLIT, // orientation
          new JScrollPane( tree ), // left
          new JScrollPane( canvas ) // right
          ) );
      frame.setVisible( true );
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
