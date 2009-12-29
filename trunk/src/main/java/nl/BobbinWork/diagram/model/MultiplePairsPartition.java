/* MultiplePairsPartition.java Copyright 2006-2007 by J. Pol
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

package nl.BobbinWork.diagram.model;

import java.util.*;

/**
 * Diagram section containing multiple pair segments.
 * 
 * @author J. Pol
 * 
 */
public abstract class MultiplePairsPartition
    extends MultipleThreadsPartition
{

  private final List<Partition> partitions = new java.util.Vector<Partition>();

  public List<Partition> getPartitions()
  {
    return partitions;
  }

  public Bounds<ThreadSegment> getBounds()
  {
    return super.getBounds();
  }

  /**
   * Searches for a <code>Cross</code> or <code>Twist</code> with the
   * coordinates within its bounds. Zooms in on diagram sections as long as the
   * specified position is within the bounds of the section.
   * 
   * 
   * @param x
   *          position along the x-axis, zero is left, minus is out of sight
   * @param y
   *          position along the y-axis, zero is top, minus is out of sight
   * 
   * @return null or a <code>Cross</code> or <code>Twist</code> with the
   *         coordinates within its bounds.
   * 
   * @see nl.BobbinWork.diagram.model.Connectors#getBounds
   */
  public MultipleThreadsPartition getSwitchAt(
      final int x,
      final int y)
  {
    final MultipleThreadsPartition partitionAt = getPartitionAt( x, y );
    if (partitionAt instanceof Switch) return partitionAt;
    return null;
  }

  public MultipleThreadsPartition getPartitionAt(
      final int x,
      final int y)
  {
    if (getBounds() != null && !getBounds().contains( x, y )) return null;
    for (final Partition partition : getPartitions()) {
      if (partition.isVisible()) {
        if (partition.getBounds().contains( x, y )) {
          if (partition instanceof Switch) return (Switch) partition;
          return ((MultiplePairsPartition) partition).getPartitionAt( x, y );
        }
      }
    }
    return this;
  }

  public ThreadSegment getThreadAt(
      final int x,
      final int y)
  {
    final Switch s = (Switch) getSwitchAt( x, y );
    if (s != null) {
      return s.getFronts()[0];
    } else {
      return null;
    }

  }

  /** Range of pairs of the parent used in this Group/Stitch. */
  protected Range pairRange = null;

  /** Pairs going into and coming out of a Group/Stitch. */
  private Connectors<PairSegment> pairConnectors = null;

  /** a title chosen by the designer of the diagram or include */
  private final String title;
  
  MultiplePairsPartition(final String title)
  {
    this.title = title;
  }
  
  Range getPairRange()
  {
    return pairRange;
  }

  void setPairRange(
      final Range pairs)
  {
    this.pairRange = pairs;
  }

  Connectors<PairSegment> getPairConnectors()
  {
    return pairConnectors;
  }

  void setPairConnectors(
      final Connectors<PairSegment> pairEnds)
  {
    this.pairConnectors = pairEnds;
  }

  Iterator<Drawable> threadIterator()
  {
    if (!isVisible()) return new Vector<Drawable>().iterator();
    return new It( partitions.iterator() )
    {
      void nextSibling()
      {
        current = siblings.next().threadIterator();
      }
    };
  }

  Iterator<Drawable> pairIterator()
  {
    if (!isVisible()) return new Vector<Drawable>().iterator();
    return new It( partitions.iterator() )
    {
      void nextSibling()
      {
        current = siblings.next().pairIterator();
      }
    };
  }

  Iterator<Drawable> pinIterator()
  {
    if (!isVisible()) return new Vector<Drawable>().iterator();
    return new It( partitions.iterator() )
    {
      void nextSibling()
      {
        current = siblings.next().pinIterator();
      }
    };
  }

  private abstract static class It
      implements Iterator<Drawable>
  {

    Iterator<Partition> siblings;
    Iterator<Drawable> current = null;

    public It(final Iterator<Partition> siblings)
    {
      this.siblings = siblings;
      if (siblings.hasNext()) {
        nextSibling();
      } else {
        current = new Vector<Drawable>().iterator();
      }
    }

    abstract void nextSibling();

    public boolean hasNext()
    {
      if (!current.hasNext()) {
        if (siblings.hasNext()) {
          nextSibling();
        }
      }
      return current.hasNext();
    }

    public Drawable next()
    {
      return current.next();
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  public String getCaption()
  {
    if (title != null) {
      if (pairRange.getFirst()>0)
        return pairRange.toString() + ": " + title;
      else
        return title;
    } else {
      return pairRange.toString();
    }
  }
}
