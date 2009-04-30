/* PointsInSwitches.java Copyright 2009 by J. Falkink-Pol
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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import nl.BobbinWork.diagram.xml.DiagramBuilder;
import nl.BobbinWork.diagram.xml.XmlResources;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Positive tests that check the shape of bounding polygons. */
@RunWith(Parameterized.class)
public class PointsInSwitches {
	
	private static XmlResources xr = null;
	
	private static final String DIAGRAM_ROOT = 
	"<diagram" +
	" xmlns='http://BobbinWork.googlecode.com/bw.xsd'" +
	" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
	" xsi:schemaLocation='http://BobbinWork.googlecode.com bw.xsd'" +
	" xmlns:xi='http://www.w3.org/2001/XInclude'" +
	"><xi:include href='basicStitches.xml'/><group pairs='1-1'>";

	private static final String DIAGRAM_ROOT_END = "</group></diagram>";

	private static final String CTCTC= ""+
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
	private static final String TCTC_BASED_ON_BASIC_STITCHES = ""+
	"<group pairs='1-8'>\n"+
	"<copy of='l'   pairs='1-1'><rotate centre='15,15' angle='-45'/><move x='188' y='73'/></copy>"+
    "<copy of='r'   pairs='2-2'><rotate centre='15,15' angle='75' /><move x='215' y='92'/></copy>"+
    "<copy of='ctc' pairs='1-2'><rotate centre='10,10' angle='25' /><move x='195' y='85'/></copy>"+
    "</group>";
	
	/** same drawing but differently grouped. TODO should get same bounds for whole thing */
	private static final String TCTC_BUILDING_OWN_STITCHES= ""+
	"<stitch pairs='1-2'>"+
	"<pair end='206.69291333853582,99.65124117072072' start='201.61417332292834,85.69751765855855'/>"+
	"<pair end='197.26120522699915,95.03248786503605' start='211.13040508681317,90.1350094068359'/>"+
	"<twist bobbins='1-2'>"+
	"<back c1='186.45370132023479,92.66690475583121' c2='177.96841994599623,83.33309524416879' end='186.45370132023479,83.33309524416879' start='177.96841994599623,92.66690475583121'/>"+
	"<front c1='186.45370132023479,83.33309524416879' c2='177.96841994599623,92.66690475583121' end='186.45370132023479,92.66690475583121' start='177.96841994599623,83.33309524416879'/>"+
	"</twist>"+
	"<twist bobbins='3-4'>"+
	"<back c1='243.19479216882343,98.28808519201685' c2='245.87635664134174,110.61404203032062' end='238.52788741299221,106.37140134320134' start='250.54326139717296,102.53072587913613'/>"+
	"<front c1='238.52788741299221,106.37140134320134' c2='250.54326139717296,102.53072587913613' end='243.19479216882343,98.28808519201685' start='245.87635664134174,110.61404203032062'/>"+
	"</twist>"+
	"<cross bobbins='2-3'>"+
	"<back end='202.58155237352025,88.35536975611325' start='209.226182617407,85.9369221296335'/>"+
	"<front end='208.56318376796213,91.14465028360186' start='206.14473614148238,84.50002003971512'/>"+
	"</cross>"+
	"<twist bobbins='1-2'>"+
	"<back end='199.79227184603164,94.33700115055514' start='197.3738242195519,87.6923709066684'/>"+
	"<front end='195.9369221296335,90.77381738259301' start='202.58155237352025,88.35536975611325'/>"+
	"</twist>"+
	"<twist bobbins='3-4'>"+
	"<back end='210.98163139444188,97.78928052748861' start='208.56318376796213,91.14465028360186'/>"+
	"<front end='205.77390324047352,97.12628167804375' start='212.41853348436027,94.707834051564'/>"+
	"</twist>"+
	"<cross bobbins='2-3'>"+
	"<back end='199.12927299658676,99.54472930452351' start='205.77390324047352,97.12628167804375'/>"+
	"<front end='202.3375049510336,100.7097390583309' start='199.79227184603164,94.33700115055514'/>"+
	"</cross>"+
	"</stitch>";

	/*  To extract more examples from a diagram:
	  
	   	System.out.println( XmlResources.toXmlString( parseEmbedded( s )));
	*/
	
    @Parameters
    public static Collection<Object[]> data() throws Exception {
    	xr = new XmlResources();
    	MultiplePairsPartition tctcBS = extractPart(TCTC_BASED_ON_BASIC_STITCHES);
    	MultiplePairsPartition tctcOS = createPart(TCTC_BUILDING_OWN_STITCHES);
		MultiplePairsPartition ctctcBS = extractPart("<copy of='ctctc' pairs='1-2'/>");
		MultiplePairsPartition ctctcOS = createPart(CTCTC);
		System.out.println( XmlResources.toXmlString( parseEmbedded( TCTC_BASED_ON_BASIC_STITCHES )));

		
		final Object[][] testCaseParameters = new Object[][] {
        		
        		{ctctcOS,ctctcBS,8,3,7,3}, 
        		
        		// just above end of front end in cross
        		{tctcOS,tctcBS,202,98,202,98},
        		
        		// start of front thread in cross
        		{tctcOS,tctcBS,200,94,200,94}, // FIXME r269 introduced the problem

        		// end of back of left twist; checks merging polygons 
        		{tctcOS,tctcOS,186,83,186,83}, // TODO merge not yet implemented
        /**/};
        return Arrays.asList(testCaseParameters);
    }

    MultiplePairsPartition partP, partQ;
    int xa,xb,ya,yb;
	
	/** Creates a test object for a test case in data(). */
	public PointsInSwitches(MultiplePairsPartition variantP, MultiplePairsPartition variantQ, int xa, int ya, int xb, int yb){
		this.partP = variantP;
		this.partQ = variantQ;
		this.xa = xa;
		this.xb = xb;
		this.ya = ya;
		this.yb = yb;
	}
	
	/** Executes tests for a case in data(). */
	@Test
	public void variantP () throws Exception {
		assertEqualSwitchBounds(partP);
	}

	/** Executes tests for a case in data(). */
	@Test
	public void variantQ () throws Exception {
		assertEqualSwitchBounds(partQ);
	}
	
	/** Executes a test for a case in data(). */
	@Test
	public void equalBounds () throws Exception {
		String p = getBounds(partP);
		String q = getBounds(partQ);
		assertTrue (p+" =/= "+q,p.equals(q));
	}

	/** Executes a test for a case in data(). */
	@Test
	public void equalDrawables () throws Exception {
		Iterator<Drawable> iteratorP = partP.threadIterator();
		Iterator<Drawable> iteratorQ = partQ.threadIterator();
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
		String boundsA = getSwitchAt(part,xa,ya);
		String boundsB = getSwitchAt(part,xb,yb);
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
	private static MultiplePairsPartition createPart(String partition)
			throws Exception {
		return DiagramBuilder.createStitch(parse(partition).getDocumentElement());
	}
	
	/** Creates an object model using the basic stitches. */
	private static MultiplePairsPartition extractPart(String partition)
	throws Exception {
		Element parsed = parseEmbedded(partition).getDocumentElement();
		Diagram diagram = DiagramBuilder.createDiagram(parsed);
		return (MultiplePairsPartition)diagram.getPartitions().get(1);
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
