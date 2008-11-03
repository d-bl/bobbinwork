package nl.BobbinWork.diagram.xml;

import java.io.IOException;
import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class TestXml extends XsdValidationFixture {

  
  private static final String ROOT = "<?xml version='1.0' encoding='UTF-8'?>" +
  "<diagram" +
  " xmlns='http://BobbinWork.googlecode.com/bw.xsd'" +
  " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
  " xsi:schemaLocation='http://BobbinWork.googlecode.com bw.xsd'" +
  ">";

  public TestXml() throws ParserConfigurationException, SAXException {
    super();
  }

  @Test
  public void basicStitches() throws SAXException, IOException {
    validate(new File(DIR + "basicStitches.xml"));
  }

  @Test
  public void newDiagram() throws SAXException, IOException {
    validate(new File(DIR + "newDiagram.xml"));
  }

  @Test
  public void emptyDiagram() throws SAXException, IOException {
    validate(ROOT + "</diagram>");
  }
  
  @Test
  public void duplicateTitle() throws SAXException, IOException {
    assertParseException(ROOT + "<title/><title/></diagram>", ".*'title'.*:group.*");
  }
  
  @Test
  public void noGroup() throws SAXException, IOException {
    assertParseException(ROOT + "<something/></diagram>", ".*'something'.*:group.*");
  }
  
  @Test
  public void duplicateGroup() throws SAXException, IOException {
    String s = "<group pairs='1-1'/>";
    validate(ROOT + s + s + "</diagram>");
  }
}
