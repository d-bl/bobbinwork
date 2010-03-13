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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public enum Ground {
  
  vierge      (4, 4, 0,  80,  80, 800L, 600L), //
  sGravenmoers(4, 4, 0,  80,  80, 650L, 500L), //
  spider      (4, 6, 0,  80, 140, 850L, 600L), //
  flanders    (4, 4, 0,  55,  55, 500L, 450L), //
  snowflake   (4, 6, 2, 136, 100, 850L, 600L);

  private static final String TEMPLATE =
      "" + XmlResources.ROOT + "<title><value lang='en'>%s</value></title>"
          + XmlResources.INCLUDE + "<group pairs='1-%d'>%s</group></diagram>";
  private final int dX;
  private final int dY;
  private final int pairs;
  private final int pairShift;
  private final int rows;
  private final int skippedPairs;
  final long diamondLapse;
  final long squareLapse;

  Ground(
      final int rows,
      final int pairs,
      final int skippedPairs,
      final int x,
      final int y,
      final long diamondLapse,// elapse time
      final long squareLapse)// elapse time
  {
    this.skippedPairs = skippedPairs;
    this.dX = x;
    this.dY = y;
    this.pairs = pairs;
    this.diamondLapse = diamondLapse;
    this.squareLapse = squareLapse;
    this.pairShift = (pairs + skippedPairs) / 2;
    this.rows = rows;
  }
  
  public InputStream squareInputStream() { 
    return new ByteArrayInputStream( square().getBytes() );
  }
  
  public InputStream diamondInputStream() { 
    return new ByteArrayInputStream( diamond().getBytes() );
  }
  
  public String square()
  {
    String s = //
        createDiagonal( 1, (pairs + skippedPairs) * 1, dX * 0, 0 ) + //
        createDiagonal( 3, (pairs + skippedPairs) * 2, dX * 2, 0 ) + //
        createDiagonal( 5, (pairs + skippedPairs) * 3, dX * 4, 0 ) + //
        createDiagonal( 7, (pairs + skippedPairs) * 4, dX * 6, 0 ) + //
        createDiagonal( 5, (pairs + skippedPairs) * 4, dX * 6, dY ) + //
        createDiagonal( 3, (pairs + skippedPairs) * 4, dX * 6, dY*2 ) + //
        createDiagonal( 1, (pairs + skippedPairs) * 4, dX * 6, dY*3 ) + //
        "";
    return String.format( TEMPLATE,"ground", (pairs + skippedPairs) * 4, s );
  }

  public String createDiagonal(
      final int count,
      final int pEnd,
      final int xStart, int yStart)
  {
    String stitches = "";//$NON-NLS-1$
    int p = pEnd - pairs+1;
    int xx = xStart;
    int yy = 2*yStart;
    for (int i = count; i > 0 && p > 0; i--) {
      stitches += newCopyTag( p, xx, yy );
      p -= 0*skippedPairs + (int)((pairs+skippedPairs)/2);
      xx -= dX;
      yy += dY;
    }
    final String string = String.format( "<group pairs='%d-%d'>%s</group>", //$NON-NLS-1$
        1, pEnd, stitches );
    return string;
  }

  public String xmlString()
  {
    return diamond();
  }

  public String diamond()
  {

    int leftPair = (rows - 1) * pairShift * 2 + pairs + 1;
    String copies = "";// newCopyTag(p) + "</copy>";
    for (int i = 0; i < rows; i++) {
      int xx = (rows - 1) * dX + i * dX;
      int yy = i * dY;
      leftPair = pairShift * (rows + i - 1) + 1;
      for (int j = 0; j < rows && leftPair > 0; j++) {
        copies += newCopyTag( leftPair, xx, yy );
        xx -= dX;
        yy += dY;
        leftPair -= pairShift;
      }
    }
    final int nrOfPairs = pairShift * 2 * rows + pairs - pairShift;
    return String.format( TEMPLATE, "ground", nrOfPairs, copies );
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
