package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;

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

  @Test(timeout=4700)
  public void restore() throws Exception {
    check (XML_CONTENT,EXPANDED_FILE);
  }
  
  public void check(String xmlContent, File expected) throws Exception {
    
    Document document = xmlResources.parse(xmlContent);
    String originalXml = XmlResources.toXmlString(document);
    TreeExpander.applyTransformations(document.getDocumentElement());

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
