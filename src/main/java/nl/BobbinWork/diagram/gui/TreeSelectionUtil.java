package nl.BobbinWork.diagram.gui;

import javax.swing.tree.*;

import nl.BobbinWork.diagram.model.Partition;

public class TreeSelectionUtil
{

  private static DefaultMutableTreeNode getSelectedNode(
      final TreePath path)
  {
    return (DefaultMutableTreeNode) path
        .getPathComponent( path.getPathCount() - 1 );
  }

  static Partition getSelectedPartition(
      final TreePath path)
  {
    return (Partition) getSelectedNode( path ).getUserObject();
  }

  private static DefaultMutableTreeNode getSubRootNode(
      final TreePath path)
  {
    return (DefaultMutableTreeNode) path.getPathComponent( 1 );
  }

  private static Partition getSubRootPartition(
      final TreePath path)
  {
    return (Partition) getSubRootNode( path ).getUserObject();
  }

  static boolean isVisible(
      final TreePath path)
  {
    if (path.getPathCount() < 2) return false;
    return getSubRootPartition( path ).isVisible();
  }

}
