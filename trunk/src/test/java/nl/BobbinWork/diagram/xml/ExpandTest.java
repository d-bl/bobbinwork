/* TestExpand.java Copyright 2006-2007 by J. Pol
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

import static org.junit.Assert.assertTrue;

import java.io.*;

import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.Test;
import org.w3c.dom.*;

public class ExpandTest extends XmlFixture {

  private static final String XML_CONTENT = XmlResources.ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
  private static final File EXPANDED_FILE = new File ("src/test/java"+PATH+"expanded.xml");

  @Test
  public void emptyDiagram() throws Exception {
    check (EXPANDED_FILE,XML_CONTENT);
  }
  
  @Test(timeout=35)
  public void performance1() throws Exception {
    xmlResources.parse(XML_CONTENT);
  }
  
  @Test(timeout=200)
  public void performance2() throws Exception {
    Document document = xmlResources.parse(XML_CONTENT);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
  }
  
  @Test(timeout=20)
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
    //System.out.println(s);
    
    Document doc = xmlResources.parse(s);
    TreeExpander.replaceCopyElements( doc.getDocumentElement() );
    //System.out.println( XmlResources.toXmlString(doc) );
  }
  
  @Test(timeout=5000)
  public void grounds() throws Exception {
    for ( Ground g : Ground.values() ) {
      check(g.xmlString());
    }
  }
  
  @Test(timeout=450)
  public void newDiagram() throws Exception  {
    File file = new File(SRC + PATH + "newDiagram.xml");
    Document document = xmlResources.parse(file);
    TreeExpander.replaceCopyElements(document.getDocumentElement());
    String xmlString = XmlResources.toXmlString(document);
    XmlResources.validate(xmlResources.parse(xmlString));
  }

  private void check(File expected, String xmlContent) throws Exception {

    String resultingXml = check(xmlContent);
    
    /* useful to synchronize expanded.xml (delete last line) 
    
    PrintStream x = new PrintStream("build/new-expanded.xml");
    x.print(strip( resultingXml ));
    x.close();
    /**/
    
    if ( expected != null ) {
      assertEquals(
          "transformed xml not as predicted", 
          expected, 
          strip(resultingXml)
      );
    }
  }

  private String strip(
      String resultingXml)
  {
    return resultingXml.replaceAll( ">\\s+<", "><" ).replaceAll( "[\\r\\n]+", " " );
  }

  private String check(String xmlContent) throws Exception {
    
    Document document = xmlResources.parse(xmlContent);
    String originalXml = XmlResources.toXmlString(document); // eliminates white space variations
    TreeExpander.replaceCopyElements(document.getDocumentElement());
    String resultingXml = XmlResources.toXmlString(document);
    undoTransformations(document);
    String restoredXml = XmlResources.toXmlString(document);

    /* an external tool could reveal the nature of a failure  
    
    OutputStream os = new FileOutputStream("build/tmp.xml");
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
