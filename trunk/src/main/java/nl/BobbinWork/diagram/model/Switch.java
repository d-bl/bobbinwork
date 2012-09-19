/* Switch.java Copyright 2006-2007 by J. Pol
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

import java.util.Iterator;
import java.util.Vector;

import nl.BobbinWork.bwlib.gui.Localizer;

/**
 * Two bobbins next to one another change positions.
 * Occasionally multiple bobbins are treated as a single one.
 * 
 * @author J. Pol
 */
public abstract class Switch
    extends MultipleThreadsPartition
{

  /** Two (groups of) bobbins exchanging their positions. */
  private ThreadSegment frontThreads[] = null, backThreads[] = null;

  /** The range of threads/bobbins involved in the cross/twist. */
  private Range threadRange = null;

  /**
   * Creates a new instance of a <code>Cross</code> or <code>Twist</code>. The
   * difference between the two is made by the abstract method
   * <code>setThreadConnectors</code> determining whether the
   * <code>frontThreads</code> starts as left bobbin, or the
   * <code>backThreads</code>.
   */
  Switch(
      final Range range,
      final ThreadSegment[] fronts,
      final ThreadSegment[] backs)
  {
    if (range.getCount() != fronts.length + backs.length) {
      String format = "range [%s] =/= front threads [%d] + back threads[%d]";
      String message = String.format( format, range.toString(),fronts.length, backs.length );
      throw new IllegalArgumentException (message);
    }
    threadRange = range;
    frontThreads = fronts;
    backThreads = backs;
    setThreadConnectors();
  }

  /**
   * Adds the <code>backThread</code> and <code>frontThread</code>
   * <code>ThreadSegment</code>s in the appropriate order to the
   * <code>ThreadConnectors</code>.
   * 
   * For a <code>Cross</code> the <code>frontThread</code> segment starts as the
   * left bobbin, for a <code>Twist</code> the <code>backThread</code>.
   * 
   * For each <code>Cross</code> and <code>Twist</code> two
   * <code>ThreadSegment</code>s are drawn. The end points of one
   * <code>Cross</code> and <code>Twist</code> are the start points of the next.
   * While constructing a pattern cross by twist in the same order as a lace
   * maker performs these actions, these segments are connected, eliminating
   * gaps and giving the whole thread a single style (color/width).
   */
  abstract void setThreadConnectors();

  /**
   * Gets the numbers of the thread/bobbin positions involved in the
   * cross/twist.
   * 
   * @return range of positions.
   */
  Range getThreadRange()
  {
    return threadRange;
  }

  /**
   * Gets the segment of the thread(s) lying behind the other thread(s).
   * 
   * @return the thread segment(s) in the background.
   */
  public ThreadSegment[] getBacks()
  {
    return backThreads;
  }

  /**
   * Gets the segment(s) of the thread(s) lying on top of the other thread.
   * 
   * @return the thread segment(s) in front of the other one(s).
   */
  public ThreadSegment[] getFronts()
  {
    return frontThreads;
  }

  @Override
  final Iterator<Drawable> threadIterator()
  {
    final DrawableList drawables = new DrawableList();
    drawables.addThreadSegments( backThreads );
    drawables.addThreadSegments( frontThreads );
    return drawables.iterator();
  }

  @Override
  final Iterator<Drawable> pairIterator()
  {
    return new Vector<Drawable>().iterator();
  }

  @Override
  final Iterator<Drawable> pinIterator()
  {
    return new Vector<Drawable>().iterator();
  }

  public String getCaption()
  {
    final String key = "Node_" + getClass().getSimpleName();
    return threadRange.toString() + ": " + Localizer.getString( key );
  }
}
