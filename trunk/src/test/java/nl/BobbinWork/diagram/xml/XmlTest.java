/* TestXml.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.diagram.xml;


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

public class XmlTest extends XmlFixture {

  @Test
  public void twist() throws SAXException, IOException {
    XmlResources.validate(xmlResources.getTwist(5));
  }

  @Test
  public void includeError() throws SAXException, IOException, TransformerException {
    
    String xmlContent = XmlResources.ROOT + "<xi:include href='not-existing.xml'/></diagram>";
    assertSAXException(xmlContent, ".*");
  }
  
  @Test
  public void include() throws SAXException, IOException, TransformerException {
    
    String xmlContent = XmlResources.ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
    String xmlString = XmlResources.toXmlString(xmlResources.parse(xmlContent));
    //new FileOutputStream("JUnitTests" + PATH + "expanded.xml").write(xmlString.getBytes());
    xmlResources.validate(xmlContent);
    xmlResources.validate(xmlString);
  }

  @Test
  public void newDiagram() throws SAXException, IOException, TransformerException {
    File file = new File(SRC + PATH + "newDiagram.xml");
    xmlResources.validate(file);
  }

  @Test
  public void emptyDiagram() throws SAXException, IOException {
    String content = XmlResources.ROOT + "</diagram>";
    xmlResources.parse(content);
  }
  
  @Test
  public void duplicateTitle() throws SAXException, IOException, TransformerException {
    assertSAXException(XmlResources.ROOT + "<title/><title/></diagram>", ".*'title'.*:group.*");
  }
  
  @Test
  public void noGroup() throws SAXException, IOException, TransformerException {
    assertSAXException(XmlResources.ROOT + "<something/></diagram>", ".*'something'.*:group.*");
  }
  
  @Test
  public void duplicateGroup() throws SAXException, IOException, TransformerException {
    String s = "<group pairs='1-1'/>";
    String content = XmlResources.ROOT + s + s + "</diagram>";
    xmlResources.validate(content);
  }

  protected void undoTransformations(Document document)
      throws XPathExpressionException {
        NodeList nodes = XmlResources.evaluate("//*[@id]",document);
        for (int i=0 ; i < nodes.getLength() ; i++ ) {
          Node clone = nodes.item(i);
          Node orphan = (Node) clone.getUserData(TreeExpander.CLONE_TO_ORPHAN);
          if ( orphan != null ) {
            clone.getParentNode().replaceChild(orphan, clone);
            orphan.setUserData(TreeExpander.ORPHAN_TO_CLONE, null, null);
            orphan.setUserData(TreeExpander.DOM_TO_VIEW, null, null);
            clone.setUserData(TreeExpander.CLONE_TO_ORPHAN, null, null);
            clone.setUserData(TreeExpander.DOM_TO_VIEW, null, null);
          }
        }
      }
}
