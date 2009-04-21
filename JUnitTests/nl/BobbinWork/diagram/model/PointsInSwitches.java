package nl.BobbinWork.diagram.model;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import nl.BobbinWork.diagram.xml.DiagramBuilder;
import nl.BobbinWork.diagram.xml.XmlResources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/** Positive tests that check the shape of bounding polygons. */
@RunWith(Parameterized.class)
public class PointsInSwitches {
	
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

	/** figure (b) on http://code.google.com/p/bobbinwork/wiki/MathChallenges#Highlighting_(groups_of)_stitches */
	private static final String TCTC= ""+
	"<stitch pairs='1-2'>"+
	"<pair end='206.69291333853582,99.65124117072072' start='201.61417332292834,85.69751765855855'/>"+
	"<pair end='197.26120522699915,95.03248786503605' start='211.13040508681317,90.1350094068359'/>"+
	"<twist bobbins='1-2'>"+
	"<back c1='186.45370132023479,92.66690475583121' c2='177.96841994599623,83.33309524416879' end='186.45370132023479,83.33309524416879' start='177.96841994599623,92.66690475583121'/>"+
	"<front c1='186.45370132023479,83.33309524416879' c2='177.96841994599623,92.66690475583121' end='186.45370132023479,92.66690475583121' start='177.96841994599623,83.33309524416879'/>"+
	"</twist>"+
	"<twist bobbins='1-2'>"+
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
	
	/*  TODO visualize with a variant of ThreadStyleToolBar.Preview

	    To extract more examples from a diagram:
	  
	   	Document tmp = xr.parse(s);
		TreeExpander.replaceCopyElements(tmp.getDocumentElement());
		System.out.println( XmlResources.toXmlString(tmp));
	*/
	
    @Parameters
    public static Collection<Object[]> data() throws Exception {
    	xr = new XmlResources();
        MultiplePairsPartition ctctc = createPart(CTCTC);
        MultiplePairsPartition tctc = createPart(TCTC);
        final Object[][] testCaseParameters = new Object[][] {
        		
        		{ctctc,8,3,7,3},
        		
        		// just above end of front end in cross
        		{tctc,202,98,202,98},
        		
        		// start of front thread in cross
        		{tctc,200,94,200,94},

        		// end of back of left twist; checks merging polygons 
        		{tctc,186,83,186,83},
        /**/};
        return Arrays.asList(testCaseParameters);
    }

    MultiplePairsPartition partition;
    int xa,xb,ya,yb;
	
	/** Creates a test object for a test case in data(). */
	public PointsInSwitches(MultiplePairsPartition p, int xa, int ya, int xb, int yb){
		this.partition = p;
		this.xa = xa;
		this.xb = xb;
		this.ya = ya;
		this.yb = yb;
	}
	
	/** Executes the test for a case in data(). */
	@Test
	public void sameSwitches () throws Exception {
		String sa = getSwitchAt(partition,xa,ya);
		String sb = getSwitchAt(partition,xb,yb);
		assertTrue (sa+" =/= "+sb,sa.equals(sb));
	}
	
	/** Searches for a cross or twist in the partition at position (x,y).
	 * @return the bounding polygon in string format.
	 */
	private static String getSwitchAt(MultiplePairsPartition partition, int x, int y) throws Exception {
		MultipleThreadsPartition switchAtXy = partition.getSwitchAt(x, y);
		if (switchAtXy == null) throw new Exception("no cross or twist at ("+x+","+y+")\n" +
				"bounding polygon for the whole thing: "+((Bounds)partition.getBounds()));
		return ((Bounds) switchAtXy.getBounds()).toString();
	}

	/** Creates a diagram object model from an XML string. */
	private static MultiplePairsPartition createPart(String partition)
			throws Exception {
		return DiagramBuilder.createStitch(xr.parse(partition).getDocumentElement());
	}
}
