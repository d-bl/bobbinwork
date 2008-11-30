package nl.BobbinWork.diagram.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.junit.BeforeClass;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlFixture {

  static final String PATH = "/nl/BobbinWork/diagram/xml/";
  static XmlResources xmlResources;

  @BeforeClass
  public static void setup() throws ParserConfigurationException, SAXException {
    xmlResources = new XmlResources();
  }

  protected void assertSAXException(String xmlContent, String regExp)
      throws IOException, SAXException, TransformerException {
      
    try {
      xmlResources.parse( xmlContent );
      xmlResources.validate( xmlContent );
    } catch (SAXParseException e) { 
      String s = e.getMessage();
      assertTrue( "line:"+e.getLineNumber()+" col:"+e.getColumnNumber() + 
          " expected [" + regExp + "] but got\n" + s, s.matches(regExp) );
    } catch (SAXException e) { 
      String s = e.getMessage();
      assertTrue( " expected [" + regExp + "] but got \n" + s, s.matches(regExp) );
    }
  }

  protected void assertEquals(String message, File file, String transformedXml)
  throws IOException, FileNotFoundException {

    if ( ((int) file.length()) != file.length() ) throw new IOException ("file too long");

    char[] b = new char[ (int) file.length() ];
    FileReader reader = new FileReader(file);
    try { if ( file.length() != reader.read(b)) throw new IOException ("read wrong length"); } 
    finally { reader.close(); }
    String string = new String(b);
    assertTrue(message,transformedXml.equals(string));
  }

}
