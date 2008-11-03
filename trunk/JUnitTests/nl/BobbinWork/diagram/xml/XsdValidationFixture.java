package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidationFixture {
  protected static final String DIR = "src/nl/BobbinWork/diagram/xml/";
  private Validator validator;

  public XsdValidationFixture() throws ParserConfigurationException,
      SAXException {
    validator = newValidator();
  }

  private static Validator newValidator() throws SAXException {

    String uri = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    SchemaFactory factory = SchemaFactory.newInstance(uri);
    Schema newSchema = factory.newSchema(new File(DIR + "bw.xsd"));
    return newSchema.newValidator();
  }

  protected void validate(File xmlFile) throws IOException, SAXException {
    validate(new StreamSource(xmlFile), xmlFile.getName());
  }

  protected void validate(String xmlContent) throws SAXException, IOException {
    InputStream inputStream = new ByteArrayInputStream (xmlContent.getBytes());
    validate(new StreamSource(inputStream), "");
  }
  
  private void validate(StreamSource streamSource, String prefix)
  throws SAXException, IOException {
    try {
      validator.validate(streamSource);
    } catch (SAXParseException e) {
      String msg = prefix+" " + "line:"+e.getLineNumber()+" col:"+e.getColumnNumber()+" "+e.getMessage();
      throw new SAXException(msg);
    }
  }
  
  protected void assertParseException(String xmlContent, String regexp) throws IOException, SAXException {
    try {
      InputStream inputStream = new ByteArrayInputStream (xmlContent.getBytes());
      validator.validate(new StreamSource(inputStream));
    } catch (SAXParseException e) { 
      String s = e.getMessage();
      assertTrue( e.getLineNumber()+","+e.getColumnNumber()+" expected [" + regexp + "] but got " + s, s.matches(regexp) );
    }
  }
}