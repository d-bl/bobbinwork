/* TestXml.java Copyright 2006-2007 by J. Pol
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


import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XmlTest extends XmlFixture {

  @Test
  public void duplicateTitle() throws SAXException, IOException, TransformerException {
    assertSAXException(XmlResources.ROOT + "<title/><title/></diagram>", ".*'title'.*:group.*");
  }
  
  @Test
  public void noGroup() throws SAXException, IOException, TransformerException {
    assertSAXException(XmlResources.ROOT + "<something/></diagram>", ".*'something'.*:group.*");
  }
  
  @Test
  public void duplicateGroup() throws SAXException, IOException, TransformerException {
    String s = "<group pairs='1-1'/>";
    String content = XmlResources.ROOT + s + s + "</diagram>";
    xmlResources.validate(content);
  }
}
