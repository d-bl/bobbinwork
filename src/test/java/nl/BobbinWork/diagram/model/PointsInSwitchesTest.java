/* PointsInSwitches.java Copyright 2009 by J. Pol
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

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import nl.BobbinWork.diagram.gui.BwDiagrams;
import nl.BobbinWork.diagram.xml.DiagramBuilder;
import nl.BobbinWork.diagram.xml.XmlResources;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Positive tests that check the shape of bounding polygons. */
@RunWith(Parameterized.class)
public class PointsInSwitchesTest {
	
	static XmlResources xr = null;
	
	private static final String DIAGRAM_ROOT = 
	"<diagram" +
	" xmlns='http://BobbinWork.googlecode.com/bw.xsd'" +
	" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
	" xsi:schemaLocation='http://BobbinWork.googlecode.com bw.xsd'" +
	" xmlns:xi='http://www.w3.org/2001/XInclude'" +
	"><xi:include href='basicStitches.xml'/><group pairs='1-1'>";

	private static final String DIAGRAM_ROOT_END = "</group></diagram>";

	protected static final String CTCTC= ""+
	"<stitch id='ctctc' pairs='1-2'>"+
	"<pair start='0,6.6' end='16.6,23.3'/>"+
	"<pair start='16.6,6.6' end='0,23.3'/>"+
	"<cross bobbins='2-3'><back start='10,0' end='5,5'/><front start='6.6,0' end='11.6,5'/></cross>"+
	"<twist bobbins='1-2'><back start='0,6.6' end='5,11.6'/><front start='5,5' c1='0,10' c2='0,10' end='0,15'/></twist>"+
	"<twist bobbins='3-4'><back start='11.6,5' c1='16.6,10' c2='16.6,10' end='16.6,15'/><front start='16.6,6.6' end='11.6,11.6'/></twist>"+
	"<cross bobbins='2-3'><back start='11.6,11.6' end='5,18.3'/><front start='5,11.6' end='11.6,18.3'/></cross>"+
	"<twist bobbins='1-2'><back start='0,15' c1='0,20' c2='0,20' end='5,25'/><front start='5,18.3' end='0,23.3'/></twist>"+
	"<twist bobbins='3-4'><back start='11.6,18.3' end='16.6,23.3'/><front start='16.3,15' c1='16.3,20' c2='16.3,20' end='11.6,25'/></twist>"+
	"<cross bobbins='2-3'><back start='11.6,25' end='5,31.6'/><front start='5,25' end='11.6,31.6'/></cross>"+
	"</stitch>";
	
	/** figure (b) on http://code.google.com/p/bobbinwork/wiki/MathChallenges#Highlighting_(groups_of)_stitches */
	static final String TCTC_BASED_ON_BASIC_STITCHES = ""+
  "<stitch pairs='1-2'>\n"+
  "<pair end='206.692,99.651' start='201.614,85.697'/>"+
  "<pair end='197.261,95.032' start='211.130,90.13'/>"+
  "<copy of='C'><rotate centre='15,15' angle='-45'/><move x='188' y='73'/></copy>"+
  "<copy of='F'><rotate centre='15,15' angle='75' /><move x='215' y='92'/></copy>"+
  "<copy of='G'><rotate centre='10,10' angle='25' /><move x='195' y='85'/></copy>"+
  "<copy of='N'><rotate centre='10,10' angle='25' /><move x='195' y='85'/></copy>"+
  "<copy of='O'><rotate centre='10,10' angle='25' /><move x='195' y='85'/></copy>"+
  "<copy of='P'><rotate centre='10,10' angle='25' /><move x='195' y='85'/></copy>"+
  "</stitch>";
	
	/** same drawing but differently grouped. */
	protected static final String TCTC_BUILDING_OWN_STITCHES= ""+
	"<stitch pairs='1-2'>"+
	"<pair end='206.692,99.651' start='201.614,85.697'/>"+
	"<pair end='197.261,95.032' start='211.130,90.13'/>"+
	"<twist bobbins='1-2'>"+
	"<back c1='186.453,92.666' c2='177.968,83.333' end='186.453,83.333' start='177.968,92.666'/>"+
	"<front c1='186.453,83.333' c2='177.968,92.666' end='186.453,92.666' start='177.968,83.333'/>"+
	"</twist>"+
	"<twist bobbins='3-4'>"+
	"<back c1='243.194,98.288' c2='245.876,110.614' end='238.527,106.371' start='250.543,102.530'/>"+
	"<front c1='238.527,106.371' c2='250.543,102.530' end='243.194,98.288' start='245.876,110.614'/>"+
	"</twist>"+
	"<cross bobbins='2-3'>"+
	"<back end='202.581,88.355' start='209.2,85.93'/>"+
	"<front end='208.563,91.144' start='206.144,84.500'/>"+
	"</cross>"+
	"<twist bobbins='1-2'>"+
	"<back end='199.792,94.337' start='197.37,87.69'/>"+
	"<front end='195.93,90.773' start='202.581,88.355'/>"+
	"</twist>"+
	"<twist bobbins='3-4'>"+
	"<back end='210.981,97.789' start='208.563,91.144'/>"+
	"<front end='205.773,97.126' start='212.418,94.7'/>"+
	"</twist>"+
	"<cross bobbins='2-3'>"+
	"<back end='199.129,99.544' start='205.773,97.126'/>"+
	"<front end='202.33,100.70' start='199.792,94.337'/>"+
	"</cross>"+
	"</stitch>";

	/*  To extract more examples from a diagram:
	  
	   	System.out.println( XmlResources.toXmlString( parseEmbedded( s )));
	*/
	
    @Parameters
    public static Collection<Object[]> data() throws Exception {
      setBundle( "nl/BobbinWork/diagram/gui/labels", new Locale( "en" ) );
    	xr = new XmlResources();
      final MultiplePairsPartition ctctcBS = extractPart("<copy of='ctctc' pairs='1-2'/>");
      final MultiplePairsPartition ctctcOS = createPart(CTCTC);
    	final MultiplePairsPartition tctcBS = extractPart(TCTC_BASED_ON_BASIC_STITCHES);
    	final MultiplePairsPartition tctcOS = createPart(TCTC_BUILDING_OWN_STITCHES);
      //System.out.println( XmlResources.toXmlString( parseEmbedded( TCTC_BASED_ON_BASIC_STITCHES )));
		
      final Object[][] testCaseParameters = new P[][] {
        		
            {new P(ctctcBS,ctctcOS,8,3,7,3)}, 
            
            // just above end of front end in cross
            {new P(tctcBS,tctcOS,202,98,202,98)},

            // start of front thread in second cross
        		{new P(tctcBS,tctcOS,200,95,200,95)}, 

        		// TODO merge not yet implemented
        		// TODO perhaps see http://blog.schauderhaft.de/2009/10/04/junit-rules/  
        /**/};
        return Arrays.asList(testCaseParameters);
    }

    P params;
	
	static class P {
		MultiplePairsPartition partP, partQ;
		int xa,xb,ya,yb;
		P (MultiplePairsPartition variantP, MultiplePairsPartition variantQ, int xa, int ya, int xb, int yb){
			this.partP = variantP;
			this.partQ = variantQ;
			this.xa = xa;
			this.xb = xb;
			this.ya = ya;
			this.yb = yb;
		}
	}
	
	/** Creates a test object for a test case in data(). */
	public PointsInSwitchesTest(P parameters){
		params = parameters;
	}
	
	/** Executes tests for a case in data(). */
	@Test
	public void variantP () throws Exception {
		assertEqualSwitchBounds(params.partP);
	}

	/** Executes tests for a case in data(). */
	@Test
	public void variantQ () throws Exception {
		assertEqualSwitchBounds(params.partQ);
	}
	
	/** Executes a test for a case in data(). */
	@Test
	public void equalBounds () throws Exception {
		String p = getBounds(params.partP);
		String q = getBounds(params.partQ);
		assertTrue (p+" =/= "+q,p.equals(q));
	}

	/** Executes a test for a case in data(). */
	@Test
	public void equalDrawables () throws Exception {
		Iterator<Drawable> iteratorP = params.partP.threadIterator();
		Iterator<Drawable> iteratorQ = params.partQ.threadIterator();
		int count = 0;
		while ( iteratorP.hasNext() && iteratorQ.hasNext() ) {
			count++;
			Drawable drawableP = iteratorP.next();
			Drawable drawableQ = iteratorQ.next();
			assertEqualShapes( drawableP, drawableQ);
			assertEqualStyles(drawableP, drawableQ);
		}
		assertEqualNrOfDrawables(iteratorP, iteratorQ, count);
	}

	private void assertEqualNrOfDrawables(Iterator<Drawable> iteratorP,
			Iterator<Drawable> iteratorQ, int count) {
		String msg = "common number of elements = " + count + //
		"; p.hasNext=" + iteratorP.hasNext() + //
		"; q.hasNext=" + iteratorQ.hasNext();
		assertTrue(msg,!iteratorP.hasNext() || !iteratorP.hasNext());
	}

	private void assertEqualStyles(Drawable drawableP, Drawable drawableQ) {
		String styleP = drawableP.getStyle().toString();
		String styleQ = drawableQ.getStyle().toString();
		String msg = "styles are not queual: "+styleP +" =/= " + styleQ;
		assertTrue( msg, styleP.equals(styleQ));
	}
	
	private void assertEqualShapes(Drawable drawableP, Drawable drawableQ) {
		// TODO Auto-generated method stub
	}

	private String getBounds(MultiplePairsPartition part) {
		String b = part.getBounds().toString();
		assertTrue ( "no bounds for whole thing: "+b, b.matches(".*\\) \\(.*"));
		return b;
	}
	
	private void assertEqualSwitchBounds(MultiplePairsPartition part) throws Exception {
		String boundsA = getSwitchAt(part,params.xa,params.ya);
		String boundsB = getSwitchAt(part,params.xb,params.yb);
		assertTrue (boundsA+" =/= "+boundsB,boundsA.equals(boundsB));
	}
	
	
	/** Searches for a cross or twist in the partition at position (x,y).
	 * @return the points of the bounding polygon of the found switch.
	 */
	private static String getSwitchAt(MultiplePairsPartition partition, int x, int y) throws Exception {
		MultipleThreadsPartition switchAtXy = partition.getSwitchAt(x, y);
		String msg = "no cross or twist at ("+x+","+y+")\n" +
				"bounding polygon for the whole thing: "+(partition.getBounds());
		assertTrue(msg,switchAtXy != null);
		String bounds = (switchAtXy.getBounds()).toString();
		assertTrue ( "empty bounds for switch", ! bounds.equals(""));
		return bounds;
	}

	/** Creates an object model from an XML string. */
	protected static MultiplePairsPartition createPart(String partition)
			throws Exception {
		return DiagramBuilder.createStitch(parse(partition).getDocumentElement());
	}
	
	/** Creates an object model using the basic stitches. */
	protected static MultiplePairsPartition extractPart(String partition)
	throws Exception {
		Element parsed = parseEmbedded(partition).getDocumentElement();
		List<Partition> parts = DiagramBuilder.createDiagram(parsed).getPartitions();
		MultiplePairsPartition part = (MultiplePairsPartition)parts.get(parts.size()-1);
		return (MultiplePairsPartition) part.getPartitions().get(0);
	}

	private static Document parseEmbedded(String s) throws Exception {
		return parse(DIAGRAM_ROOT + s + DIAGRAM_ROOT_END);
	}
	
	private static Document parse(String s) throws Exception {
		Document parsed = xr.parse(s);
		TreeExpander.replaceCopyElements(parsed.getDocumentElement());
		return parsed;
	}
}
