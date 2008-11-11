package nl.BobbinWork.diagram.xml;


import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class TestXml extends XmlFixture {

  @Test
  public void twist() throws SAXException, IOException {
    xmlHandler.validate(xmlHandler.getTwist(12));
  }

  @Test
  public void include() throws SAXException, IOException, TransformerException {
    
    String xmlContent = ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
    String xmlString = xmlHandler.toXmlString(xmlHandler.parse(xmlContent));
    //new FileOutputStream("JUnitTests" + PATH + "expanded.xml").write(xmlString.getBytes());
    xmlHandler.validate(xmlContent);
    xmlHandler.validate(xmlString);
  }

  @Test
  public void newDiagram() throws SAXException, IOException, TransformerException {
    File file = new File("src" + PATH + "newDiagram.xml");
    xmlHandler.validate(file);
  }

  @Test
  public void emptyDiagram() throws SAXException, IOException {
    String content = ROOT + "</diagram>";
    xmlHandler.parse(content);
  }
  
  @Test
  public void duplicateTitle() throws SAXException, IOException, TransformerException {
    assertException(ROOT + "<title/><title/></diagram>", ".*'title'.*:group.*");
  }
  
  @Test
  public void noGroup() throws SAXException, IOException, TransformerException {
    assertException(ROOT + "<something/></diagram>", ".*'something'.*:group.*");
  }
  
  @Test
  public void duplicateGroup() throws SAXException, IOException, TransformerException {
    String s = "<group pairs='1-1'/>";
    String content = ROOT + s + s + "</diagram>";
    xmlHandler.validate(content);
  }
}
