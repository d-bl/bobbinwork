package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestExpand extends XmlFixture {

  private static final String XML_CONTENT = ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
  private static final File EXPANDED_FILE = new File ("JUnitTests"+PATH+"expanded.xml");

  @Test
  public void restore() throws SAXException, IOException, TransformerException, XPathExpressionException {
    
    Document document = xmlHandler.parse(XML_CONTENT);
    String originalXml = xmlHandler.toXmlString(document);
    TreeExpander.applyTransformations(document.getDocumentElement());

    assertEquals("transformed xml not as predicted", EXPANDED_FILE, xmlHandler.toXmlString(document));
    undoTransformations(document);
    assertTrue("restored xml not equal to original",xmlHandler.toXmlString(document).equals(originalXml));
    
    xmlHandler.validate(xmlHandler.toXmlString(document));
    xmlHandler.validate(XML_CONTENT);
  }

  private void undoTransformations(Document document)
      throws XPathExpressionException {
    NodeList nodes = xmlHandler.evaluate("//*[@id]",document);
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
