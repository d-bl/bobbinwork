package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidationFixture {
  protected static final String DIR = "src/nl/BobbinWork/diagram/xml/";
  private Validator validator;

  public XsdValidationFixture() 
  throws ParserConfigurationException, SAXException {
    
    SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI);
    validator = factory.newSchema(new File(DIR + "bw.xsd")).newValidator();
  }

  protected void validate(File xmlFile) 
  throws IOException, SAXException {

    validate(new StreamSource(xmlFile), xmlFile.getPath());
  }

  protected void validate(String xmlContent) 
  throws SAXException, IOException {
    
    InputStream inputStream = new ByteArrayInputStream (xmlContent.getBytes());
    validate(new StreamSource(inputStream), "");
  }
  
  private void validate(StreamSource streamSource, String sourceName)
  throws SAXException, IOException {
  
    try {
      validator.validate(streamSource);
    } catch (SAXParseException e) {
      String msg = "\n" + sourceName + "\n" + getLocation(e) +"\n"+e.getMessage();
      throw new SAXException(msg);
    }
  }

  private String getLocation(SAXParseException e) {
    
    return "line:"+e.getLineNumber()+" col:"+e.getColumnNumber();
  }
  
  protected void assertParseException(String xmlContent, String regexp) 
  throws IOException, SAXException {
    
    InputStream inputStream = new ByteArrayInputStream (xmlContent.getBytes());
    StreamSource streamSource = new StreamSource(inputStream);
    try {
      validator.validate(streamSource);
    } catch (SAXParseException e) { 
      String s = e.getMessage();
      assertTrue( getLocation(e) + " expected [" + regexp + "] but got " + s, s.matches(regexp) );
    }
  }
}