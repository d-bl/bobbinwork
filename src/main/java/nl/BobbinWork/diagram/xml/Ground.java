/* Ground.java Copyright 2006-2007 by J. Pol
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

package nl.BobbinWork.diagram.xml;

public enum Ground {

  vierge      (4, 4, 2, 80, 80), //
  sGravenmoers(4, 4, 2, 80, 80), //
  spider      (4, 6, 3, 80, 140), //
  flanders    (4, 4, 2, 55, 55), //
  snowflake   (4, 6, 4, 136, 100);

  private static final String BASIC_STITCHES = "basicStitches.xml";
  private final int x;
  private final int y;
  private final int pairs;
  private final int pairShift;
  private final int rows;

  Ground(
      final int rows,
      final int pairs,
      final int pairShift,
      final int x,
      final int y)
  {
    this.x = x;
    this.y = y;
    this.pairs = pairs;
    this.pairShift = pairShift;
    this.rows = rows;
  }

  public String xmlString()
  {

    int leftPair = (rows - 1) * pairShift * 2 + pairs + 1;
    String copies = "";// newCopyTag(p) + "</copy>";
    for (int i = 0; i < rows; i++) {
      int xx = (rows - 1) * x + i * x;
      int yy = i * y;
      leftPair = pairShift * (rows + i - 1) + 1;
      for (int j = 0; j < rows && leftPair > 0; j++) {
        copies += newCopyTag( leftPair, xx, yy );
        xx -= x;
        yy += y;
        leftPair -= pairShift;
      }
    }
    int nrOfPairs = pairShift * 2 * rows + pairs - pairShift;
    final String s =
        String.format( "<xi:include href='%s'/><group pairs='1-%d'>%s</group>", //$NON-NLS-1$
            BASIC_STITCHES, nrOfPairs, copies );
    return XmlResources.ROOT + s + "</diagram>"; //$NON-NLS-1$
  }

  private String newCopyTag(
      final int start,
      final int x,
      final int y)
  {
    return String.format( "<copy of='%s' pairs='%d-%d'>", //$NON-NLS-1$
        name(), start, start + pairs - 1 )
        + String.format( "<move x='%d' y='%d'/></copy>", x, y );//$NON-NLS-1$
  }
}
