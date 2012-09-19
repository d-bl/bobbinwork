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

import java.util.List;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.model.MultiplePairsPartition;
import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.diagram.model.Style;
import nl.BobbinWork.diagram.model.Switch;
import nl.BobbinWork.diagram.model.ThreadStyle;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DiagramRebuilder
{
  public static boolean canCopy(
      final Partition p)
  {
    if (p.getSourceObject() == null) return false;
    final String id = ((Element) p.getSourceObject()).getAttribute( "id" );
    return (id != null && !id.equals( "" ));
  }

  public static boolean canDelete(
      final Partition partition)
  {
    if (partition instanceof Switch) {
      // the basic stitch in the library would be destroyed
      return false;
    }

    final Element domElement = (Element) partition.getSourceObject();
    if (domElement == null) return false;
    if (domElement.getParentNode() != null
        && domElement.getParentNode()
            .getUserData( TreeExpander.CLONE_TO_ORPHAN ) != null) return false;
    if (domElement.getParentNode() != null
        && domElement.getParentNode().getUserData(
            TreeExpander.INDIRECT_CLONE_TO_ORPHAN ) != null) return false;
    return true;
  }

  public static Diagram delete(
      final Partition selected)
  {
    final Element domElement = (Element) selected.getSourceObject();
    if (domElement == null) return null;
    final Element original =
        (Element) domElement.getUserData( TreeExpander.ORPHAN_TO_CLONE );
    if (original != null) {
      original.getParentNode().removeChild( original );
    } else {
      if (domElement.getParentNode() != null) {
        domElement.getParentNode().removeChild( domElement );
      } else {
        return null;
      }
    }
    return rebuild( domElement.getOwnerDocument() );
  }

  /**
   * Checks if the selected element eventually can be replaced
   * 
   * @param selected
   *          a diagram partition
   * @return true if the partition is created from a clone of a &lt;copy>
   *         element
   */
  public static boolean canReplace(
      final Partition selected)
  {
    if (selected == null) return false;
    final Element oldP = (Element) selected.getSourceObject();
    if (oldP == null) return false;
    final String of = oldP.getAttribute( "of" );
    if (of == null || !of.equals( "" )) return false;
    return true;
  }

  /**
   * Checks if the selected element can be replaced
   * 
   * @param selected
   *          a diagram partition that eventually will be replaced
   * @param copied
   *          the partition that eventually will be copied over the selected
   *          partition
   * @return true if the partition is created from a clone of a &lt;copy>
   *         element
   */
  public static boolean canReplace(
      final Partition selected,
      final Partition copied)
  {
    if (copied == null || selected == null || copied == selected) return false;
    if (copied.getNrOfPairs() != selected.getNrOfPairs()) return false;
    final Element oldP = (Element) selected.getSourceObject();
    final Element newP = (Element) copied.getSourceObject();
    if (oldP == null || newP == null) return false;
    final String id = newP.getAttribute( "id" );
    final String of = oldP.getAttribute( "of" );
    if (id == null || id.equals( "" ) || of == null || of.equals( "" ))
      return false;
    return true;
  }

  /**
   * Replaces the selected partition with the copied one. This is done by
   * replacing the attribute vale of the original &lt;copy of="..">. To make the
   * change effective, the orphaned nodes are restored and the diagram
   * regenerated.
   * 
   * @param selected
   *          a diagram partition that will be replaced
   * @param copied
   *          the partition that will be copied over the selected partition
   */
  public static Diagram replace(
      final Partition selected,
      final Partition copied)
  {
    final Element oldP = (Element) selected.getSourceObject();
    final Element newP = (Element) copied.getSourceObject();
    if (oldP == null || newP == null) return null;
    final String id = newP.getAttribute( "id" );
    final String of = oldP.getAttribute( "of" );
    if (id == null || id.equals( "" ) || of == null || of.equals( "" ))
      return null;

    oldP.setAttribute( "of", id );

    return rebuild( oldP.getOwnerDocument() );
  }

  private static Diagram rebuild(
      final Document doc)
  {
    if (doc == null) return null;

    final Document parsed;
    try {
      undoTransformations( doc );
      // parse from scratch to work around the memory loss
      final String s = XmlResources.toXmlString( doc );
      parsed = new XmlResources().parse( s );
      TreeExpander.replaceCopyElements( parsed.getDocumentElement() );
    } catch (final Exception exception) {
      exception.printStackTrace();
      return null;
    }
    final Element element = parsed.getDocumentElement();
    return DiagramBuilder.createDiagram( element );
  }

  public static String toString(
      final Diagram diagram)
      throws TransformerException, XPathExpressionException
  {
    if (diagram == null) return "";
    final Element sourceObject = (Element) diagram.getSourceObject();
    if (sourceObject == null) return "";
    final Document doc = sourceObject.getOwnerDocument();
    if (doc == null) return "";
    undoTransformations( doc );
    final Partition visiblePartition = getVisiblePartition( diagram);
    if (visiblePartition!=null){
      final Element visible = (Element) visiblePartition.getSourceObject();
      removerColors( sourceObject );
      int i = 0;
      for (ThreadStyle style:visiblePartition.getThreadStyles()) {
        if (style !=null)
          visible.appendChild( newBobbin( doc, style, ++i+"" ));
      }
    }
    return XmlResources.toXmlString( doc );
  }

  private static Element newBobbin(
      final Document doc,
      final ThreadStyle x,
      final String nrs)
  {
    final Element result = doc.createElement( ElementType.new_bobbins.name() );
    final Element style = createStyle(doc,"style",x);
    style.appendChild( createStyle(doc,"shadow",x.getShadow()) );
    result.appendChild( style );
    result.setAttribute( "nrs", nrs );
    return result;
  }
  
  private static Element createStyle(
      final Document doc,
      final String name,
      final Style style)
  {
    final Element result = doc.createElement( name );
    result.setAttribute( "color", "#"+ Integer.toHexString(style.getColor().getRGB()&0xFFFFFF) );
    result.setAttribute( "width", "" + style.getWidth() );
    return result;
  }

  private static void removerColors(
      Element diagram)
  {
    NodeList elements;
    try {
      elements = XmlResources.evaluate( "//*[@nrs]", diagram.getOwnerDocument() );
    } catch (XPathExpressionException exception) {
      exception.printStackTrace();
      return;
    }
    for (int i = 0; i < elements.getLength(); i++) {
      Node node = elements.item( i );
        node.getParentNode().removeChild( node );
    }
  }

  private static Partition getVisiblePartition(
      final Diagram diagram)
  {
    Partition partition = diagram;
    while (null == partition.getBounds()) {
      if (!(partition instanceof MultiplePairsPartition)) {
        return null;
      }
      final List<Partition> list =
          ((MultiplePairsPartition) partition).getPartitions();
      return list.get( list.size() - 1 );
    }
    return partition;
  }
  
  
  private static void undoTransformations(
      final Document document) throws XPathExpressionException
  {
    final NodeList nodes = XmlResources.evaluate( "//*", document );
    for (int i = 0; i < nodes.getLength(); i++) {
      final Node clone = nodes.item( i );
      final Node orphan =
          (Node) clone.getUserData( TreeExpander.CLONE_TO_ORPHAN );
      if (orphan != null) {
        clone.getParentNode().replaceChild( orphan, clone );
        orphan.setUserData( TreeExpander.ORPHAN_TO_CLONE, null, null );
        clone.setUserData( TreeExpander.CLONE_TO_ORPHAN, null, null );
      }
    }
  }
}
