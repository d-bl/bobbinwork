package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidationFixture {

  protected static final String DIR = "src/nl/BobbinWork/diagram/xml/";
  private Validator validator;
  private DocumentBuilder parser;

  public XsdValidationFixture() 
  throws ParserConfigurationException, SAXException {
    
    SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
    validator = factory.newSchema( new File( DIR + "bw.xsd" ) ).newValidator();
    parser = newParser();
  }

  private Source parse(File xmlFile) 
  throws SAXException, IOException {
    
    return new DOMSource( parser.parse(xmlFile) );
  }
  
  private Source parse(String xmlContent) 
  throws SAXException, IOException {
    
    InputStream inputStream = new ByteArrayInputStream (xmlContent.getBytes());
    return new DOMSource( parser.parse(inputStream) );
  }

  protected void validate(File xmlFile) 
  throws IOException, SAXException {
    
    validate( parse(xmlFile), xmlFile.getPath() );
  }
  
  protected void validate(String xmlContent) 
  throws SAXException, IOException {
    
    validate( parse(xmlContent), "");
  }
  
  private DocumentBuilder newParser() 
  throws ParserConfigurationException {
    
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    docFactory.setXIncludeAware(true); 
    docFactory.setNamespaceAware(true);
    return docFactory.newDocumentBuilder();
  }
  
  private void validate(Source source, String sourceName)
  throws SAXException, IOException {
  
    try {
      validator.validate(source);
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
    
    try {
      validator.validate(parse(xmlContent));
    } catch (SAXParseException e) { 
      String s = e.getMessage();
      assertTrue( getLocation(e) + " expected [" + regexp + "] but got " + s, s.matches(regexp) );
    }
  }
}