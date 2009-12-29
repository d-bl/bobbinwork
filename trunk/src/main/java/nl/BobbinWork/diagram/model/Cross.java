/* Cross.java Copyright 2006-2007 by J. Pol
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
 * A bobbin going over the next bobbin.
 * 
 * Having two bobbins in each hand usually the right bobbin in the left hand
 * goes over the left bobbin in the right hand. In other words, the second
 * bobbin goes over the third. A bobbin may be read as a group of bobbins
 * treated as one.
 * 
 * @author J. Pol
 */
public class Cross
    extends Switch
{

  public Cross(final Range range, final ThreadSegment[] fronts, final ThreadSegment[] backs)
  {
    super( range, fronts, backs );
  }

  void setThreadConnectors()
  {
    final List<ThreadSegment> ins = new Vector<ThreadSegment>( 2 );
    ins.addAll( Arrays.asList( getFronts() ) );
    ins.addAll( Arrays.asList( getBacks() ) );
    super.setThreadConnectors( new Connectors<ThreadSegment>( ins ) );
  }
}
