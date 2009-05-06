package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestExpand extends XmlFixture {

  private static final String XML_CONTENT = XmlResources.ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
  private static final File EXPANDED_FILE = new File ("JUnitTests"+PATH+"expanded.xml");

  @Test
  public void emptyDiagram() throws Exception {
    check (EXPANDED_FILE,XML_CONTENT);
  }
  
  @Test(timeout=25)
  public void performance1() throws Exception {
    xmlResources.parse(XML_CONTENT);
  }
  
  @Test(timeout=800)
  public void performance2() throws Exception {
    Document document = xmlResources.parse(XML_CONTENT);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
  }
  
  @Test //(timeout=60)
  public void basics() throws Exception {
    
    int w = 5;
    float w2 = w / 2F;
    String s = 
      XmlResources.ROOT + "\n" +
      " <twist id='t' bobbins='1-2' " + ">\n" + 
      "  <back start='0," + w2 + "' end='" + w + "," + w2 + "'/>\n" + 
      "  <front start='" + w2 + ",0' end='" + w2 + "," + w + "' />\n" + 
      " </twist>\n" +
      " <copy of='t' bobbins='3-4'><move x='10' y='10'/></copy>\n" +
      " <copy of='t' pairs='5-6'><rotate centre='"+w+","+w+"' angle='45'/></copy>\n" +
      "</diagram>\n";
    System.out.println(s);
    
    Document doc = xmlResources.parse(s);
    TreeExpander.replaceCopyElements( doc.getDocumentElement() );
    System.out.println( XmlResources.toXmlString(doc) );
  }
  
  @Test(timeout=31000)
  public void grounds() throws Exception {
    for ( Ground g : Ground.values() ) {
      check(g.xmlString());
    }
  }
  
  @Test
  public void newDiagram() throws Exception  {
    File file = new File("src" + PATH + "newDiagram.xml");
    Document document = xmlResources.parse(file);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
    String xmlString = XmlResources.toXmlString(document);
    XmlResources.validate(xmlResources.parse(xmlString));
  }

  private void check(File expected, String xmlContent) throws Exception {

    String resultingXml = check(xmlContent);
    
    PrintStream x = new PrintStream("tmp2.xml");
    x.println(resultingXml);
    x.close();
    
    if ( expected != null ) {
      assertEquals(
          "transformed xml not as predicted", 
          expected, 
          resultingXml
      );
    }
  }

  private String check(String xmlContent) throws Exception {
    
    Document document = xmlResources.parse(xmlContent);
    String originalXml = XmlResources.toXmlString(document); // eliminates white space variations
    TreeExpander.replaceCopyElements(document.getDocumentElement());
    String resultingXml = XmlResources.toXmlString(document);
    undoTransformations(document);
    String restoredXml = XmlResources.toXmlString(document);

    /* an external tool could reveal the nature of a failure*/  
    OutputStream os = new FileOutputStream("tmp.xml");
    os.write(resultingXml.getBytes());
    os.close();
    /**/
    
    assertTrue("restored xml not equal to original",restoredXml.equals(originalXml));
    xmlResources.validate(XML_CONTENT);
    xmlResources.validate(resultingXml);
    return resultingXml;
  }

  protected void undoTransformations(Document document)
  throws XPathExpressionException {
    NodeList nodes = XmlResources.evaluate("//*",document);
    for (int i=0 ; i < nodes.getLength() ; i++ ) {
      Node clone = nodes.item(i);
      Node orphan = (Node) clone.getUserData(TreeExpander.CLONE_TO_ORPHAN);
      if ( orphan != null ) {
        clone.getParentNode().replaceChild(orphan, clone);
        orphan.setUserData(TreeExpander.ORPHAN_TO_CLONE, null, null);
        clone.setUserData(TreeExpander.CLONE_TO_ORPHAN, null, null);
        clone.setUserData(TreeExpander.DOM_TO_VIEW, null, null);
      }
    }
  }
}
