package nl.BobbinWork.diagram.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.w3c.dom.Document;
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
  public void include() throws SAXException, IOException, ParserConfigurationException {

    // create a parser to transform an XML document into a DOM tree
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    docFactory.setXIncludeAware(true);
    DocumentBuilder parser = docFactory.newDocumentBuilder();
    
    // create a SchemaFactory capable of understanding WXS schemas
    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    // load a WXS schema, represented by a Schema instance
    Source schemaFile = new StreamSource(new File(DIR + "bw.xsd"));
    Schema schema = factory.newSchema(schemaFile);

    // create a Validator instance, which can be used to validate an instance document
    Validator validator = schema.newValidator();

    // validate the xml instance
    validator.validate(new DOMSource(parser.parse(new File(DIR + "newDiagram.xml"))));
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
