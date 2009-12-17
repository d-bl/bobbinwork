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

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import nl.BobbinWork.diagram.model.*;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.junit.*;
import org.w3c.dom.*;
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
  
  @Ignore("see issue 23")
  @Test
  public void savingChangedThreads() throws Exception {
    
    Document parsed = xmlResources.parse( Ground.flanders.diamond() );
    TreeExpander.replaceCopyElements( (Element) parsed.getFirstChild() );
    Diagram diagram =DiagramBuilder.createDiagram( (Element) parsed.getFirstChild() );
    
    ThreadSegment thread = diagram.getThreadAt(203, 232);
    thread.getStyle().setColor( "#FF0000" );
    thread.getStyle().setWidth( 2 );
    
    String s = DiagramRebuilder.toString( diagram );
    assertThat(s,containsString("<new_bobbins nrs='13'><style color='#FF0000' width='2'><shadow color='#CC0000' width='10'><style/></new_bobbins>"));
  }
}
