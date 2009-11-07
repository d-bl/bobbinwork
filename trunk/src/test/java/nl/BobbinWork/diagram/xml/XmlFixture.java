/* XmlFixture.java Copyright 2006-2007 by J. Pol
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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import junit.framework.Assert;


import org.junit.BeforeClass;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlFixture {

  static final String PATH = "/nl/BobbinWork/diagram/xml/";
  protected static final String SRC = "src/main/resources";
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
      String s = e.getMessage().replaceAll( "^\\s*", "" ).replaceAll( "\\s*$", "" );
      assertTrue( "line:"+e.getLineNumber()+" col:"+e.getColumnNumber() + 
          " expected [" + regExp + "] but got\n" + s, s.matches(regExp) );
    } catch (SAXException e) { 
      String s = e.getMessage().replaceAll( "^\\s*", "" ).replaceAll( "\\s*$", "" );
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
    Assert.assertEquals(message,transformedXml,string);
  }

}
