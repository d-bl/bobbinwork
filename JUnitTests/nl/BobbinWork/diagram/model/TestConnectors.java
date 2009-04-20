package nl.BobbinWork.diagram.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import nl.BobbinWork.diagram.xml.DiagramBuilder;
import nl.BobbinWork.diagram.xml.XmlResources;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

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

	@BeforeClass
	public static void setUp () throws Exception {
		xr = new XmlResources();
	}

	@Test
	public void ctctc () throws Exception {
		// TODO turn into a parameterized test
		assertSame(cp(CTCTC), 8, 3, new Bounds(new int[][]{{6,0},{10,0},{11,5},{5,5}}));
	}

	private void assertSame(MultiplePairsPartition partition, int x, int y, Bounds expected)
			throws IOException, SAXException {
		String exp = expected.toString();
		String actual = ((Bounds) partition.getSwitchAt(x, y).getHull()).toString();
		assertTrue("\nexpected: "+exp+"\ngot: "+actual,exp.equals(actual));
	}

	private static MultiplePairsPartition cp(String ctctc2)
			throws IOException, SAXException {
		return DiagramBuilder.createStitch(xr.parse(ctctc2).getDocumentElement());
	}
}
