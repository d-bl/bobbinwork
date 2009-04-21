package nl.BobbinWork.diagram.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.diagram.xml.DiagramBuilder;
import nl.BobbinWork.diagram.xml.XmlResources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class TestConnectors {
	
	private static XmlResources xr = null;
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

	
	/*
	TODO get the right example of http://code.google.com/p/bobbinwork/wiki/MathChallenges#Highlighting_(groups_of)_stitches  
	
	    expand the 3 copy elements below into a stitch:
	 
    	Element tmp = xr.parse("...").getDocumentElement();
		TreeExpander.replaceCopyElements(tmp);
        System.out.println(tmp -> String);
        
        manually remove the first 3 stitches and
        merge the results of the copies into one stitch
	
    <stitch id="l" pairs="1-1" display="none">
        <pair start="-2.7,-2.7" end="3,3" mark="" />
        <twist bobbins="1-2">
            <back start="-6,0.6" c1="0,6.6" c2="0.6,-6" end="6.6,0" />
            <front start="0.6,-6" c1="6.6,0" c2="-6,0.6" end="0,6.6" />
        </twist>
    </stitch>
    <stitch id="r" pairs="2-2" display="none" >
        <pair start="19.3,-2.7" end="13.3,3.3" mark="" />
        <twist bobbins="1-2">
            <back start="16,-6" c1="10,0" c2="22.6,0.6" end="16.6,6.6" />
            <front start="22.6,0.6" c1="16.6,6.6" c2="16,-6" end="10,0" />
        </twist>
    </stitch>
    <stitch id="ctc" pairs="9-10">
        <pair start="3,3" end="13.5,13.5" />
        <pair start="13.5,3" end="3,13.3" />
        <cross bobbins="2-3">
            <back start="10,0" end="5,5" />
            <front start="6.6,0" end="11.6,5" />
        </cross>
        <twist bobbins="1-2">
            <back start="0,6.6" end="5,11.6" />
            <front start="5,5" end="0,10" />
        </twist>
        <twist bobbins="3-4">
            <back start="11.6,5" end="16.6,10" />
            <front start="16.6,6.6" end="11.6,11.6" />
        </twist>
        <cross bobbins="2-3">
            <back start="11.6,11.6" end="6.6,16.6" />
            <front start="5,11.6" end="10,16.3" />
        </cross>
    </stitch>
    <copy of="l"    pairs="1-1"><rotate centre="15,15" angle="-45"/><move x="188" y="73"/></copy>
    <copy of="r"    pairs="2-2"><rotate centre="15,15" angle="75"/><move x="215" y="92"/></copy>
    <copy of="ctc"  pairs="1-2"><rotate centre="10,10" angle="25"/><move x="195" y="85"/></copy>
	*/
	

	String a,b;
	public TestConnectors(String a,String b){
		this.a = a;
		this.b = b;
	}
	
	@Test
	public void ctctc () throws Exception {
		assertTrue (a+" =/= "+b,a.equals(b));
	}
	
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        MultiplePairsPartition ctctc = createPart(CTCTC);
        
        final Object[][] testCaseParameters = new Object[][] {
        		{getSwitchAt(ctctc,8,3),getSwitchAt(ctctc,7,3)},
        		{getSwitchAt(ctctc,8,3),getSwitchAt(ctctc,9,3)},
        /**/};
        return Arrays.asList(testCaseParameters);
    }

	private static String getSwitchAt(MultiplePairsPartition partition, int x, int y) {
		return ((Bounds) partition.getSwitchAt(x, y).getHull()).toString();
	}

	private static MultiplePairsPartition createPart(String ctctc2)
			throws IOException, SAXException, ParserConfigurationException {
		if ( xr == null ) xr = new XmlResources();
		return DiagramBuilder.createStitch(xr.parse(ctctc2).getDocumentElement());
	}
}
