package nl.BobbinWork.diagram.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlResources {

  private static final String PATH = "nl/BobbinWork/diagram/xml/"; //$NON-NLS-1$
  private static final String SCHEMA = PATH + "bw.xsd"; //$NON-NLS-1$
  private static final URL SCHEMA_RESOURCE = XmlResources.class.getClassLoader().getResource(SCHEMA);
  
  /** base's for URI's to try until parsing does not fail with an include problem */
  private static final String [] INCLUDE_BASES = {
    SCHEMA_RESOURCE.toString(),
    null,
    "http://bobbinwork.googlecode.com/svn/trunk/src/" + SCHEMA, //$NON-NLS-1$
    "http://bobbinwork.googlegroups.com/web/"}; //$NON-NLS-1$
  
  /**  attributes for the root element required for includes and validation against the schema */
  public  static final String ROOT_ATTRIBUTES = 
    " xmlns='http://BobbinWork.googlecode.com/bw.xsd'" + //$NON-NLS-1$
    " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" + //$NON-NLS-1$
    " xsi:schemaLocation='http://BobbinWork.googlecode.com bw.xsd'" + //$NON-NLS-1$
    " xmlns:xi='http://www.w3.org/2001/XInclude'" ; //$NON-NLS-1$

  static final String ROOT = "<?xml version='1.0' encoding='UTF-8'?>" + 
  "<diagram" + XmlResources.ROOT_ATTRIBUTES + ">";
  
  static private Validator validator = newValidator();
  static private XPath xPath = XPathFactory.newInstance().newXPath();
  static private DocumentBuilder parser;
  
  public XmlResources( ) throws ParserConfigurationException {
    if (parser == null ) parser = newParser();
  }
  
  static public NodeList evaluate (String xPathExp, Document document) throws XPathExpressionException {
    return (NodeList) xPath.evaluate(xPathExp,document, XPathConstants.NODESET);
  }
  
  /** Gets the XML definition of a twist. 
   * The right thread goes over the left thread.
   * 
   * @param w width and height of the bounding box 
   * @return
   * @throws IOException 
   * @throws SAXException 
   */
  public Document getTwist (int w) throws SAXException, IOException {
    double w2 = (w/2D);
    String s = "<?xml version='1.0'?>" + //$NON-NLS-1$
    "<twist bobbins='1-2' " + ROOT_ATTRIBUTES + ">" + //$NON-NLS-1$ $NON-NLS-2$
    "<back start='0," + w2 + "' end='" + w + "," + w2 + "'/>" + //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
    "<front start='" + w2 + ",0' end='" + w2 + "," + w + "' />" + //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
    "</twist>"; //$NON-NLS-1$
    return parse(s);
  }
  
  public Document parse(String xmlContent) 
  throws IOException, SAXException {

    String messages = "";
    for (String base : INCLUDE_BASES ) {
      try {

        byte[] bytes = xmlContent.getBytes();
        InputStream is = new ByteArrayInputStream (bytes);
        return parser.parse( is, base );

      } catch (SAXException exception) {
        
        boolean includeProblem = exception.getMessage().matches(".*nclude.*"); //$NON-NLS-1$
        if ( ! includeProblem ) throw exception;
        if ( ! messages.equals("") ) throw new SAXException("include failed whith base: "+messages+"\n"+base);
        messages += " \n"+base;
      }
    }
    throw new SAXException(messages);
  }
  
  public void validate(File xmlFile) 
  throws IOException, SAXException {
    
    if ( validator == null ) return;
    validator.validate(new DOMSource( parser.parse(xmlFile) ));
  }
  
  public static void validate(Document source) 
  throws IOException, SAXException {
    
    if ( validator == null ) return;
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
    
    newValidator().validate( new DOMSource( parse(xmlContent)));
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
