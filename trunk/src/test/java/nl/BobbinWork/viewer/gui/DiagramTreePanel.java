/* BWViewer.java Copyright 2006-2008 by J. Pol
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

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;
import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagramModel;

import java.net.URI;
import java.util.Locale;

import javax.swing.JScrollPane;

import nl.BobbinWork.bwlib.gui.BWFrame;

import org.junit.Ignore;

/**
 * Show a tree in a frame for a visual test based on the diagram model.
 * 
 * @author Joke Pol
 * 
 */
@Ignore("this is java application, not a JUnit test")
public class DiagramTreePanel
{
  private static final String BUNDLE = "nl/BobbinWork/viewer/gui/labels";
  
  public static void main(
      String[] args)
  {
    if (args.length > 0)
      setBundle( BUNDLE, new Locale( args[0] ) );
    try {
      BWFrame frame = new BWFrame( BUNDLE );
      URI diagram = DiagramTree.class.getClassLoader().getResource( "http://bobbinwork.googlecode.com/svn/wiki/diagrams/flanders.xml" ).toURI();
      DiagramTree tree = new DiagramTree();
      tree.setDiagramModel( createDiagramModel(diagram) );
      frame.getContentPane().add( new JScrollPane( tree ) );
      frame.setVisible( true );
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
