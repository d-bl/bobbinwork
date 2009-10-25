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

import java.awt.event.*;

import javax.swing.event.*;
import javax.swing.tree.*;

import nl.BobbinWork.diagram.gui.DiagramPanel;
import nl.BobbinWork.diagram.model.*;

/**
 * A bidirectional listener that highlights the selected element in both
 * components on an event in one of the components.
 * 
 * @author Joke Pol
 * 
 */
public class DiagramTreeLink
    extends MouseAdapter
    implements TreeSelectionListener
{

  private final DiagramTree tree;
  private final DiagramPanel canvas;

  /**
   * Creates a bidirectional listener that highlights the selected element in
   * both components on an event in one of the components.
   * 
   * @param tree
   *          ads the created object as a selection listener
   * @param canvas
   *          ads the created object as a listener to mouse clicks
   */
  public DiagramTreeLink(final DiagramTree tree, final DiagramPanel canvas)
  {
    this.tree = tree;
    this.canvas = canvas;
    canvas.addMouseListener( this );
    tree.addTreeSelectionListener( this );
  }

  @Override
  public void mouseClicked(
      final MouseEvent event)
  {
    final MultipleThreadsPartition partition = getSwitchAt( event );
    if (partition == null) return;
    canvas.highLight( partition );
    tree.select( partition );
  }

  @Override
  public void valueChanged(
      final TreeSelectionEvent event)
  {
    final TreePath path = event.getPath();
    if (!isVisible( path ))
      canvas.highLight( getDiagram() );
    else
      canvas.highLight( getSelectedPartition( path ) );
  }

  private static boolean isVisible(
      final TreePath path)
  {
    if (path.getPathCount() < 2) return false;
    return getSubRootPartition( path ).isVisible();
  }

  private static Partition getSubRootPartition(
      final TreePath path)
  {
    return (Partition) getSubRootNode( path ).getUserObject();
  }

  private static DefaultMutableTreeNode getSubRootNode(
      final TreePath path)
  {
    return (DefaultMutableTreeNode) path.getPathComponent( 1 );
  }

  private static Partition getSelectedPartition(
      final TreePath path)
  {
    return (Partition) getSelectedNode( path ).getUserObject();
  }

  private static DefaultMutableTreeNode getSelectedNode(
      final TreePath path)
  {
    return (DefaultMutableTreeNode) path
        .getPathComponent( path.getPathCount() - 1 );
  }

  private MultipleThreadsPartition getSwitchAt(
      final MouseEvent event)
  {
    return getDiagram().getSwitchAt( event.getX(), event.getY() );
  }

  private Diagram getDiagram()
  {
    return (Diagram) getRootNode().getUserObject();
  }

  private DefaultMutableTreeNode getRootNode()
  {
    return (DefaultMutableTreeNode) tree.getPathForRow( 0 )
        .getPathComponent( 0 );
  }
}
