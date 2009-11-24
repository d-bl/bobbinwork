/* Connectors.java Copyright 2006-2009 by J. Pol
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

import java.util.List;
import java.util.Vector;

/**
 * Manages line segments like connectors on pieces of a jig saw puzzle.
 * Connected pieces result into a bigger piece with less connectors than the sum
 * of connectors of the individual pieces.
 * 
 * The <em>point</em>s at the border of a partition are
 * <code>getIns(i).getStart()</code> and <code>getOut(i).getEnd()</code>.
 * 
 * @author J. Pol
 */
class Connectors<T extends Segment>
{

  /** segments along the top edge of the jig saw piece (left to right) */
  private final List<T> ins;

  /** segments along the bottom edge of the jig saw piece (left to right) */
  private final List<T> outs;

  /**
   * Cashed value. Connections are typically made during (re)construction of a
   * diagram. If this connector is not the last in the diagram it gets connected
   * to in turn. That changes the outs and the bounds should need adjustment.
   * Rather be sure the bounds are not set at all when connecting. The value is
   * typically requested when clicking around on ad diagram and the system is
   * looking what to highlight. During that phase cashing improves performance
   * without the penalty of gaps between individual highlights
   */
  private Bounds<T> bounds = null;

  final List<Connectors<T>> children = new Vector<Connectors<T>>();

  /**
   * Creates a populated instance for threads of a switch or pairs of a stitch.
   * 
   * <pre>
   * ins:   a   b
   *         \ /
   *          X
   *         / \
   * outs:  b   a
   * </pre>
   * 
   * @param segments
   *          ordered (a,b) as shown in the figure.
   */
  Connectors(final List<T> segments)
  {
    ins = segments;
    outs = reverse( segments );
    bounds = new Bounds<T>( ins );
  }

  /**
   * Creates a copy of the list with the elements in reversed order.
   * 
   * @param segments
   * @return
   */
  private List<T> reverse(
      final List<T> segments)
  {
    final List<T> list = new Vector<T>( segments.size() );
    for (int i = segments.size(); --i >= 0;) {
      list.add( segments.get( i ) );
    }
    return list;
  }

  /**
   * Creates a new instance to be populated by <code>connect()</code>.
   * 
   * @param count
   *          the number of chained thread/pair segments.
   */
  Connectors(final int count)
  {
    ins = new Vector<T>( count );
    outs = new Vector<T>( count );
    for (int i = 0; i < count; i++) {
      ins.add( null );
      outs.add( null );
    }
  }

  /**
   * Connects the starting segments of a diagram partition to the end segments
   * of preceding diagram partitions.
   * 
   * @param child
   *          Connectors of a sub partition of the diagram
   * @param offset
   *          first bobbin/pair used by the child
   */
  void connect(
      final Connectors<T> child,
      final int offset)
  {
    // visualisation of a diagram partition (stitches/switches):
    // _____
    // |x x|
    // | x |
    // |x x|
    // |_x_|
    final int end = Math.min( child.getIns().size(), ins.size() - offset );
    for (int i = 0; i < end; i++) {
      if (ins.get( offset + i ) == null) {
        ins.set( offset + i, child.getIns().get( i ) );
      } else if (outs.get( offset + i ) != null) {
        outs.get( offset + i ).setNext( child.getIns().get( i ) );
      }
      if (child.outs.get( i ) != null) {
        outs.set( offset + i, child.outs.get( i ) );
      }
    }
    children.add( child );

    bounds = null;
  }

  /**
   * @return the thread/pair segments at the start of the diagram partition from
   *         left to right
   */
  List<T> getIns()
  {
    return ins;
  }

  /** @return @see Partition#getBounds() */
  Bounds<T> getBounds()
  {

    if (bounds == null || bounds.npoints == 0) {
      bounds = new Bounds<T>( ins, outs );
    }
    return bounds;
  }
}
