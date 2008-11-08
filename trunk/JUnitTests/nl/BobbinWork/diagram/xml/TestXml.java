package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TestXml {

  private static final String ROOT = "<?xml version='1.0' encoding='UTF-8'?>" + 
  "<diagram" + XmlHandler.ROOT_ATTRIBUTES + ">";

  private XmlHandler xmlHandler;
  
  public TestXml() throws ParserConfigurationException, SAXException {
    xmlHandler = new XmlHandler();
  }

  @Test
  public void twist() throws SAXException, IOException {
    xmlHandler.validate(xmlHandler.getTwist(12));
  }

  @Test
  public void include() throws SAXException, IOException, TransformerException {
    String xmlContent = ROOT + "<xi:include href='basicStitches.xml'/></diagram>";
    showIncludeResult(xmlHandler.parse(xmlContent));
    xmlHandler.validate(xmlContent);
  }

  @Test
  public void newDiagram() throws SAXException, IOException {
    File file = new File("src/nl/BobbinWork/diagram/xml/newDiagram.xml");
    xmlHandler.validate(file);
  }

  @Test
  public void emptyDiagram() throws SAXException, IOException {
    String content = ROOT + "</diagram>";
    xmlHandler.parse(content);
  }
  
  @Test
  public void duplicateTitle() throws SAXException, IOException {
    assertException(ROOT + "<title/><title/></diagram>", ".*'title'.*:group.*");
  }
  
  @Test
  public void noGroup() throws SAXException, IOException {
    assertException(ROOT + "<something/></diagram>", ".*'something'.*:group.*");
  }
  
  @Test
  public void duplicateGroup() throws SAXException, IOException {
    String s = "<group pairs='1-1'/>";
    String content = ROOT + s + s + "</diagram>";
    xmlHandler.validate(content);
  }

  private void showIncludeResult(Document doc) 
  throws TransformerException {

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    StreamResult result = new StreamResult(new StringWriter());
    transformer.transform(new DOMSource(doc), result);
    String xmlString = result.getWriter().toString();
    System.out.println(xmlString);
  }
  
  private void assertException( String xmlContent, String regexp )
  throws IOException, SAXException {

    xmlHandler.parse( xmlContent );
    try {
      xmlHandler.validate( xmlContent );
    } catch (SAXParseException e) { 
      String s = e.getMessage();
      assertTrue( xmlHandler.getLocation(e) + " expected [" + regexp + "] but got " + s, s.matches(regexp) );
    }
  }
}
