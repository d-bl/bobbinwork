package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestExpand extends XmlFixture {

  private static final String XML_CONTENT = XmlResources.ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
  private static final File EXPANDED_FILE = new File ("JUnitTests"+PATH+"expanded.xml");

  @Test(timeout=31000)
  public void grounds() throws Exception {
    for ( Ground g : Ground.values() ) {
      check(g.xmlString(), null);
    }
  }

  @Test
  public void basics() throws Exception {

    int w = 5;
    float w2 = w / 2F;
    String s = 
      XmlResources.ROOT + 
      "<twist id='t' bobbins='1-2' " + ">" + //$NON-NLS-1$ $NON-NLS-2$
      "<back start='0," + w2 + "' end='" + w + "," + w2 + "'/>" + //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
      "<front start='" + w2 + ",0' end='" + w2 + "," + w + "' />" + //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
      "</twist>\n" + //$NON-NLS-1$
      "<copy of='t' bobbins='3-4'><move x='10' y='10'/></copy>\n" +
      "<copy of='t' bobbins='5-6'><rotate centre='"+w+","+w+"' angle='45'/></copy>\n" +
      "</diagram>\n";
    System.out.println(s);

    Document doc = xmlResources.parse(s);
    TreeExpander.replaceCopyElements( doc.getDocumentElement() );
    System.out.println( XmlResources.toXmlString(doc) );
  }

  @Test(timeout=800)
  public void performanceNew() throws Exception {
    Document document = xmlResources.parse(XML_CONTENT);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
  }

  @Test
  public void newDiagram() throws Exception  {
    File file = new File("src" + PATH + "newDiagram.xml");
    Document document = xmlResources.parse(file);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
    String xmlString = XmlResources.toXmlString(document);
    XmlResources.validate(xmlResources.parse(xmlString));
  }

  @Test//(timeout=1200)
  public void newStyle() throws Exception {
    check (XML_CONTENT,EXPANDED_FILE);
  }

  public void check(String xmlContent, File expected) throws Exception {

    Document document = xmlResources.parse(xmlContent);
    String originalXml = XmlResources.toXmlString(document);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
    
    OutputStream os = new FileOutputStream("tmp.xml");
    os.write(XmlResources.toXmlString(document).getBytes());
    os.close();

    if ( expected != null )
      assertEquals(
          "transformed xml not as predicted", 
          expected, 
          XmlResources.toXmlString(document)
      );
    undoTransformations(document);
    assertTrue("restored xml not equal to original",XmlResources.toXmlString(document).equals(originalXml));

    xmlResources.validate(XmlResources.toXmlString(document));
    xmlResources.validate(XML_CONTENT);
  }

  protected void undoTransformations(Document document)
  throws XPathExpressionException {
    NodeList nodes = XmlResources.evaluate("//*[@id]",document);
    for (int i=0 ; i < nodes.getLength() ; i++ ) {
      Node clone = nodes.item(i);
      Node orphan = (Node) clone.getUserData(TreeExpander.CLONE_TO_ORPHAN);
      if ( orphan != null ) {
        clone.getParentNode().replaceChild(orphan, clone);
        orphan.setUserData(TreeExpander.ORPHANE_TO_CLONE, null, null);
        orphan.setUserData(TreeExpander.DOM_TO_VIEW, null, null);
        clone.setUserData(TreeExpander.CLONE_TO_ORPHAN, null, null);
        clone.setUserData(TreeExpander.DOM_TO_VIEW, null, null);
      }
    }
  }
}
