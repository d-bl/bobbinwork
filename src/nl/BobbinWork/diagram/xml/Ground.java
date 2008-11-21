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
    String s = newCopyTag(p) + "</copy>";
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
