package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.BeforeClass;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlFixture {

  protected static final String PATH = "/nl/BobbinWork/diagram/xml/";
  protected static final String ROOT = "<?xml version='1.0' encoding='UTF-8'?>" + 
    "<diagram" + XmlHandler.ROOT_ATTRIBUTES + ">";
  protected static XmlHandler xmlHandler;

  @BeforeClass
  public static void setup() throws ParserConfigurationException, SAXException {
    xmlHandler = new XmlHandler();
  }

  protected String toXmlString(Document doc) throws TransformerException {
  
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    StreamResult result = new StreamResult(new StringWriter());
    transformer.transform(new DOMSource(doc), result);
    return result.getWriter().toString();
  }

  protected void assertException(String xmlContent, String regexp)
      throws IOException, SAXException {
      
        xmlHandler.parse( xmlContent );
        try {
          xmlHandler.validate( xmlContent );
        } catch (SAXParseException e) { 
          String s = e.getMessage();
          assertTrue( xmlHandler.getLocation(e) + " expected [" + regexp + "] but got " + s, s.matches(regexp) );
        }
      }

  protected void assertEquals(String message, File file, String transformedXml)
      throws IOException, FileNotFoundException {
        
        char[] b = new char[ (int) file.length() ];
        new FileReader(file).read(b);
        String string = new String(b);
        assertTrue(message,transformedXml.equals(string));
      }

}
