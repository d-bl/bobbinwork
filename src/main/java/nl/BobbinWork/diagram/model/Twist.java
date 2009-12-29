/* Twist.java Copyright 2006-2007 by J. Pol
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

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * 
 * A bobbin going over the previous bobbin.
 * 
 * Having two bobbins in each hand usually the right bobbins in both hands go
 * over left bobbin in the same hand. A <code>Twist</code> describes what
 * happens in one hand. Note that each pair has it own instances: Though lace
 * makers shorthand describes a cloth stitch as CTC, we will have a left twist
 * and a right twist. Note that a bobbin may be read as a group of bobbins.
 * 
 * @author J. Pol
 */
public class Twist
    extends Switch
{

  /**
   * @param range
   *          select which threads/bobbins of the parent should be used counting
   *          form left to right. Usually the left two bobbins or right two
   *          bobbins of a stitch.
   * @param frontSegments
   *          represents the thread(s) going from right to left over the other.
   * @param backSegments
   *          represents the thread(s) going from left to right behind the
   *          other.
   */
  public Twist(
      final Range range,
      final ThreadSegment[] frontSegments,
      final ThreadSegment[] backSegments)
  {
    super( range, frontSegments, backSegments );
  }

  void setThreadConnectors()
  {
    final List<ThreadSegment> ins = new Vector<ThreadSegment>( 2 );
    ins.addAll( Arrays.asList( getBacks() ) );
    ins.addAll( Arrays.asList( getFronts() ) );
    super.setThreadConnectors( new Connectors<ThreadSegment>( ins ) );
  }
}
