/* FileMenu.java Copyright 2006-2007 by J. Pol
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

import static nl.BobbinWork.diagram.xml.DiagramBuilder.createDiagramModel;

import java.io.InputStream;

import nl.BobbinWork.bwlib.gui.ExceptionHandler;
import nl.BobbinWork.bwlib.io.InputStreamHandler;
import nl.BobbinWork.diagram.model.Diagram;

public class DiagramLoader
    implements InputStreamHandler
{
  private final DiagramTree tree;
  private final DiagramPanel canvas;
  private final ExceptionHandler exceptionHandler;

  public DiagramLoader(
      final DiagramTree tree,
      final DiagramPanel canvas,
      final ExceptionHandler exceptionHandler)
  {
    this.tree = tree;
    this.canvas = canvas;
    this.exceptionHandler = exceptionHandler;
  }

  public void proces(
      final InputStream inputStream)
  {
    try {
      final Diagram model = createDiagramModel( inputStream );
      canvas.setPattern( model );
      tree.setDiagramModel( model );
      tree.setSelectionRow( 0 );
      tree.requestFocus();
    } catch (final Exception exception) {
      exceptionHandler.show( exception, "could not load diagram");
    }
  }
}
