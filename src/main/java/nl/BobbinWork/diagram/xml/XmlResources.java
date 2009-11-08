/* XmlSource.java Copyright 2006-2007 by J. Pol
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

import java.io.*;
import java.net.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlResources {

  public static String NEWLINE = " ";//FIXME breaks some JUnit tests  
  // System.getProperty("line.separator"); //$NON-NLS-1$
  
  private static final URL SCHEMA_RESOURCE = XmlResources.class.getResource("bw.xsd"); //$NON-NLS-1$
  
  /** base's for URI's to try until parsing does not fail with an include problem */
  private static final String [] INCLUDE_BASES = {
    SCHEMA_RESOURCE.toString(),
    null,
    "http://bobbinwork.googlecode.com/svn/trunk/src/main/java/" + SCHEMA_RESOURCE, //$NON-NLS-1$
    "http://bobbinwork.googlecode.com/svn/wiki/diagrams/", //$NON-NLS-1$
    "http://bobbinwork.googlegroups.com/web/"}; //$NON-NLS-1$
  
  /**  attributes for the root element required for includes and validation against the schema */
  public  static final String ROOT_ATTRIBUTES = 
    " xmlns='http://BobbinWork.googlecode.com/bw.xsd'" + //$NON-NLS-1$
    " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" + //$NON-NLS-1$
    " xsi:schemaLocation='http://BobbinWork.googlecode.com bw.xsd'" + //$NON-NLS-1$
    " xmlns:xi='http://www.w3.org/2001/XInclude'" ; //$NON-NLS-1$

  static final String ROOT = "<?xml version='1.0' encoding='UTF-8'?>" + 
  "<diagram" + XmlResources.ROOT_ATTRIBUTES + ">";
  
  static private Validator validator = null;
  static private XPath xPath = XPathFactory.newInstance().newXPath();
  static private DocumentBuilder parser;

  public static final String INCLUDE = "<xi:include href='basicStitches.xml'/>";
  
  public XmlResources( ) throws ParserConfigurationException {
    if (parser == null ) parser = newParser();
  }
  
  static public NodeList evaluate (String xPathExp, Document document) throws XPathExpressionException {
    return (NodeList) xPath.evaluate(xPathExp,document, XPathConstants.NODESET);
  }
  
  static public NodeList evaluate (String xPathExp, Element document) throws XPathExpressionException {
    return (NodeList) xPath.evaluate(xPathExp,document, XPathConstants.NODESET);
  }
  
  /** Gets the XML definition of a twist. 
   * The right thread goes over the left thread.
   * 
   * @param w width and height of the bounding box 
   * @return a HTML-dom that describes a twist.
   * @throws IOException 
   * @throws SAXException 
   */
  public Document getTwist (int w) throws SAXException, IOException {
    double w2 = (w/2D);
    String s = "<?xml version='1.0'?>" + //$NON-NLS-1$
    "<twist id='t' bobbins='1-2' " + ROOT_ATTRIBUTES + ">" + //$NON-NLS-1$ $NON-NLS-2$
    "<back start='0," + w2 + "' end='" + w + "," + w2 + "'/>" + //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
    "<front start='" + w2 + ",0' end='" + w2 + "," + w + "' />" + //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
    "</twist>"; //$NON-NLS-1$
    return parse(s);
  }
  
  public Document parse(
      String xmlContent,
      String... includes) throws IOException, SAXException
  {
    String src = xmlContent;
    if (includes != null && includes.length > 0) {
      src = ROOT;
      for (String include : includes) {
        src += String.format( "<xi:include href='%s'", include );
      }
      src += xmlContent + "</diagram>";
    }
    return parse( new ByteArrayInputStream( src.getBytes() ) );
  }
  
  public Document parse(File file) 
  throws IOException, SAXException {
    
     return parse( new FileInputStream(file) );
  }

  private static class BufferOutputStream extends OutputStream {
    private StringBuffer buffer = new StringBuffer();

    public void write(int b) {
        buffer.append((char)b);
    }
    
    public PrintStream getPrintStream(){
      return new PrintStream(this){
        public String toString(){
          return buffer.toString();
        }
      }; 
    }
    
    public String toString(){
      return buffer.toString();
    }
  }

  public Document parse(
      InputStream inputStream) throws SAXException, IOException
  {
    String messages = ""; //$NON-NLS-1$
    for (String base : INCLUDE_BASES) {
      
      PrintStream saved = System.out;
      PrintStream buffer = new BufferOutputStream().getPrintStream();
      System.setErr( buffer );

      try {
        return parser.parse( inputStream, base );

      } catch (SAXException exception) {

        if (!exception.getMessage().matches( ".*nclude.*" )) {//$NON-NLS-1$
          messages = buffer.toString() + NEWLINE + messages;
          throw new SAXException(messages,exception);
        }
//        if (!messages.equals( "" )) {//$NON-NLS-1$
//          throw new SAXException( "include failed with base: " + messages + " "//$NON-NLS-1$ //$NON-NLS-2$
//              + base );
//        }
        messages = exception.getMessage() + " [" + base + "] " + messages;
      } finally {
        System.setErr( saved );
      }
    }
    throw new SAXException( messages );
  }

  public Document parse(
      URI uri) throws SAXException, IOException
  {
    return parse( new FileInputStream(new File(uri)) );
  }
  
  public void validate(File xmlFile) 
  throws IOException, SAXException {
    
    if ( validator == null && null == (validator=newValidator()) ) return;
    validator.validate(new DOMSource( parse(xmlFile) ));
  }
  
  public static void validate(Document source) 
  throws IOException, SAXException {
    
    if ( validator == null && null == (validator=newValidator()) ) return;
    validator.validate(new DOMSource(source));
  }
  
  public static String toXmlString(Document doc) throws TransformerException {
    
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    StreamResult result = new StreamResult(new StringWriter());
    transformer.transform(new DOMSource(doc), result);
    return result.getWriter().toString();
  }

  public void validate(String xmlContent) 
  throws SAXException, IOException, TransformerException {
    
  if ( validator == null && null == (validator=newValidator()) ) return;
    validator.validate( new DOMSource( parse(xmlContent)));
  }

  private static DocumentBuilder newParser() 
  throws ParserConfigurationException {
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setXIncludeAware(true); 
    factory.setNamespaceAware(true);
    return factory.newDocumentBuilder();
  }
  
  private static Validator newValidator() {
    
    SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
    try {
      return factory.newSchema( SCHEMA_RESOURCE ).newValidator(); //$NON-NLS-1$
    } catch (SAXException e) {
      // skipping validation is not essential
      // it just gives the user less friendly error messages
      System.out.println("validation attempts will be ignored ["+e.getLocalizedMessage()+"]"); //$NON-NLS-1$ $NON-NLS-2$
      return null;
    }
  }
}
