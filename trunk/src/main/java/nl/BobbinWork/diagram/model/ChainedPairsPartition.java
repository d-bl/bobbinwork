/* Group.java Copyright 2006-2007 by J. Pol
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

/**
 * 
 * @author J. Pol
 */
abstract class ChainedPairsPartition
    extends MultiplePairsPartition
{

  abstract void initConnectors();

  abstract void connectChild(
      MultiplePairsPartition child);

  ChainedPairsPartition(Range range, List<Partition> newParts, String title)
  {
    super(title);
    setPairRange( range );
    initConnectors();
    List<Partition> parts = getPartitions();
    for (Partition newPart : newParts) {
      parts.add( newPart );
      if (!(newPart instanceof Pin))
        connectChild( (MultiplePairsPartition) newPart );
    }
  }
}
