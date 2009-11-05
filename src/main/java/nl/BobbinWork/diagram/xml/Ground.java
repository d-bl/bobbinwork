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

  vierge(80, 80, 4, 2),
  sGravenmoers(80, 80, 4, 2),
  spider(80, 140, 6, 3),
  flanders(55, 55, 4, 2),
  snowflake(136, 100, 6, 4);
  
  private static final String BASIC_STITCHES = "basicStitches.xml";
  private final static int DIAGONAL_ROWS = 4;
  private int x; 
  private int y; 
  private int pairs; 
  private int pairShift;
  
  Ground (int x, int y, int pairs, int pairShift) {
    this.x = x;
    this.y = y;
    this.pairs = pairs;
    this.pairShift = pairShift;
  }
  
  public String xmlString () {
 
    int p = (DIAGONAL_ROWS - 1) * pairShift * 2 + pairs + 1;
    String s = "";//newCopyTag(p) + "</copy>";
    for (int i=0 ; i<DIAGONAL_ROWS ; i++){
        int xx = (DIAGONAL_ROWS-1)*x+i*x;
        int yy = i*y;
        p = pairShift*(DIAGONAL_ROWS+i-1)+1; 
        for (int j=0 ; j<DIAGONAL_ROWS && p>0; j++){
            s += newCopyTag(p)
            +"<move x='"+(xx)+"' y='"+(yy)+"'/></copy>\n";
            xx -= x;
            yy += y;
            p -= pairShift;
        }           
    }
    int nrOfPairs = pairShift * 2 * DIAGONAL_ROWS + pairs - pairShift;
    
    return XmlResources.ROOT + 
    "\n<xi:include href='" + BASIC_STITCHES + "'/>\n" + //$NON-NLS-1$ //$NON-NLS-2$
    "<group pairs='1-" + nrOfPairs + "'>\n" + //$NON-NLS-1$ //$NON-NLS-2$
    "<title/>\n" + //$NON-NLS-1$ magically it makes the name of the stitch appear in the treeView $NON-NLS-1$
    s + "</group>\n</diagram>"; //$NON-NLS-1$
  }

  private String newCopyTag(int p) {
    return "<copy of='"+name()+"' pairs='"+p+"-"+(p+pairs-1)+"'>\n";
  }
}
