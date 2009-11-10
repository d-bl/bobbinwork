/* DiagramRebuilder.java Copyright 2009 by J. Pol
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
 */package nl.BobbinWork.diagram.xml;

import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.model.*;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.w3c.dom.*;

public class DiagramRebuilder
{
  public static boolean canReplace(
      Partition p)
  {
    if (p.getSourceObject() == null) return false;
    final String id = ((Element) p.getSourceObject()).getAttribute( "of" );
    return (id != null && !id.equals( "" ));
  }

  public static boolean canCopy(
      Partition p)
  {
    if (p.getSourceObject() == null) return false;
    final String id = ((Element) p.getSourceObject()).getAttribute( "id" );
    return (id != null && !id.equals( "" ));
  }
  
  public static Diagram delete(
      final Partition selected)
  {
    final Element oldP = (Element) selected.getSourceObject();
    if (oldP == null) return null;
    final Document doc = oldP.getOwnerDocument();
    if (oldP.getParentNode() != null) {
      oldP.getParentNode().removeChild( oldP );
    } else {
      final Element original = (Element) oldP.getUserData( TreeExpander.ORPHAN_TO_CLONE );
      if (original==null)return null;
      original.getParentNode().removeChild( original );
    }
    return rebuild( doc );
  }

  public static Diagram replace(
      final Partition selected,
      final Partition copied)
  {
    final Element oldP = (Element) selected.getSourceObject();
    final Element newP = (Element) copied.getSourceObject();
    if (oldP == null || newP == null) return null;
    final String id = newP.getAttribute( "id" );
    final String of = oldP.getAttribute( "of" );
    if (id == null || of == null) return null;

    oldP.setAttribute( "of", id );

    return rebuild( oldP.getOwnerDocument() );
  }

  private static Diagram rebuild(
      final Document doc)
  {
    if (doc == null ) return null;
    try {
      undoTransformations( doc );
      TreeExpander.replaceCopyElements( doc.getDocumentElement() );
    } catch (XPathExpressionException exception) {
      exception.printStackTrace();
      return null;
    }
    return DiagramBuilder.createDiagram( doc.getDocumentElement() );
  }

  private static void undoTransformations(
      Document document) throws XPathExpressionException
  {
    NodeList nodes = XmlResources.evaluate( "//*", document );
    for (int i = 0; i < nodes.getLength(); i++) {
      Node clone = nodes.item( i );
      Node orphan = (Node) clone.getUserData( TreeExpander.CLONE_TO_ORPHAN );
      if (orphan != null) {
        clone.getParentNode().replaceChild( orphan, clone );
        orphan.setUserData( TreeExpander.ORPHAN_TO_CLONE, null, null );
        clone.setUserData( TreeExpander.CLONE_TO_ORPHAN, null, null );
      }
      if (clone.getUserData( TreeExpander.DOM_TO_VIEW )!=null)
        clone.setUserData( TreeExpander.DOM_TO_VIEW, null, null );
    }
  }
}
