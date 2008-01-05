/* TreeBuilder.java Copyright 2006-2007 by J. Falkink-Pol
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author J. Falkink-Pol
 */
public class TreeBuilder {
    
    private javax.xml.parsers.DocumentBuilder documentBuiler = null;
    
    /** Creates a new instance of TreeBuilder 
     * @param validating */
    public TreeBuilder(boolean validating) {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //dbf.setIgnoringComments(true);
        //dbf.setValidating(validating);
        try {
            documentBuiler = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException parserConfigurationException) {
            System.out.println(parserConfigurationException.getMessage());
            System.exit(1);
        }
    }
    
    public Element build( String s ) throws UnsupportedEncodingException, SAXException, IOException {
        return documentBuiler.parse( new ByteArrayInputStream(s.getBytes("UTF-8")) ).getDocumentElement();
    }    
}
