/* PointsInSwitches.java Copyright 2009 by J. Pol
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

import static java.lang.Double.valueOf;
import static nl.BobbinWork.diagram.xml.ElementType.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.diagram.model.*;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DiagramBuilder
{

  private static final String SEPARATOR = ",";
  private static final String RANGE_SEPARATOR = "-";
  public static final String MODEL_TO_DOM = "model";

  private static class ChainedPairsPartitionFactory
  {
    private static final ThreadStyle DEFAULT_THREAD_STYLE = new ThreadStyle();
    final Element element;
    final List<Partition> parts = new Vector<Partition>();
    final Vector<ThreadStyle> bobbins = new Vector<ThreadStyle>();
    final String partitionTitle;
    
    ChainedPairsPartitionFactory(Element element)
    {
      this.element = element;
      partitionTitle = getTitle(element);
      for //
      (Node child = element.getFirstChild() //
      ; child != null //
      ; child = child.getNextSibling()) //
      {
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          ElementType childType = ElementType.valueOf( child.getNodeName() );
          Element childElement = (Element) child;
          switch (childType) {
          case pin:
            parts.add( DiagramBuilder.createPin( childElement ) );
            break;
          case group:
            MultiplePairsPartition part =
                DiagramBuilder.createGroup( childElement );
            register( childElement, part );
            parts.add( part );
            break;
          case stitch:
            MultiplePairsPartition part2 = createStitch( childElement );
            register( childElement, part2 );
            parts.add( part2 );
            break;
          case new_bobbins:
            ThreadStyle style = createThreadStyle( child.getFirstChild() );
            String ranges[] = childElement.getAttribute( "nrs" ).split( "," );
            for (String range : ranges) {
              final String[] nrs = range.split( "-" );
              final int start;
              final int end;
              try {
                start = parseRangeNr( nrs[0] );
                end = (nrs.length > 1 ? parseRangeNr( nrs[1] ) : start);
              } catch (NumberFormatException e) {
                throw new RuntimeException( "invalid number:\n<"
                    + child.getNodeName() + " " + //
                    child.getAttributes().getNamedItem( "nrs" ) + ">" );
              }
              int i = bobbins.size();
              bobbins.setSize( Math.max( bobbins.size(), end + 1 ) );
              for (; i < start; i++) {
                bobbins.set( i, DEFAULT_THREAD_STYLE );
              }
              for (i = start; i <= end; i++) {
                bobbins.set( i, style );
              }
            }
            break;

          default:
            break;
          }
        }
      }
    }

    private int parseRangeNr(
        final String nr)
    {
      // lacemakers start counting with one,
      // indexes with zero, so subtract one
      return Integer.decode( nr ).intValue() - 1;
    }

    Group createGroup()
    {
      return new Group( createRange( element ), parts, bobbins, partitionTitle );
    }

    Diagram createDiagram()
    {
      return new Diagram( parts );
    }
  }
  
  private static Element getFirst(Element element,ElementType type){
    NodeList titleList = element.getElementsByTagName(type.toString());
    if (titleList==null||titleList.getLength()<=0)return null;
    return ((Element)titleList.item( 0 ));
  }
  
  private static String getTitle(Element element){
    Element t = getFirst(element,ElementType.title);
    if (t==null)return null;
    Element v = getFirst(t,ElementType.value);
    if (v==null )return null;
    // TODO get the value of the locale language
    return v.getTextContent();
  }

  private static Group createGroup(
      Element element)
  {
    return new ChainedPairsPartitionFactory( element ).createGroup();
  }

  public static Diagram createDiagram(
      Element element)
  {
    return new ChainedPairsPartitionFactory( element ).createDiagram();
  }

  public static Diagram createDiagramModel(
      final String xmlContent)
      throws IOException, SAXException, ParserConfigurationException,
      XPathExpressionException
  {
    final String s =
        "<diagram" + " xmlns='http://BobbinWork.googlecode.com/bw.xsd'"
            + " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'"
            + " xsi:schemaLocation='http://BobbinWork.googlecode.com bw.xsd'"
            + " xmlns:xi='http://www.w3.org/2001/XInclude'"
            + "><xi:include href='basicStitches.xml'/>" + xmlContent
            + "</diagram>";
    Document parsed = new XmlResources().parse( s );
    TreeExpander.replaceCopyElements( parsed.getDocumentElement() );
    return new ChainedPairsPartitionFactory( parsed.getDocumentElement() )
        .createDiagram();
  }

  private static Diagram convert(
      final Document parsed) throws XPathExpressionException
  {
    TreeExpander.replaceCopyElements( parsed.getDocumentElement() );
    return createDiagram( parsed.getDocumentElement() );
  }

  public static Diagram createDiagramModel(
      URI uri)
      throws URISyntaxException, IOException, SAXException,
      ParserConfigurationException, XPathExpressionException
  {
    return convert( new XmlResources().parse( uri ) );
  }

  public static Diagram createDiagramModel(
      final InputStream inputStream)
      throws XPathExpressionException, SAXException, IOException,
      ParserConfigurationException
  {
    return convert( new XmlResources().parse( inputStream ) );
  }

  private static class SwitchFactory
  {

    private Range range;
    private ThreadSegment frontSegment;
    private ThreadSegment backSegment;

    SwitchFactory(Element element)
    {
      range = createRange( element );
      frontSegment =
          createThreadSegment( getMandatoryElement( element, front ) );
      backSegment = createThreadSegment( getMandatoryElement( element, back ) );
      checkRangeIs2( range );
    }

    Cross createCross()
    {
      return new Cross( range, frontSegment, backSegment );
    }

    Twist createTwist()
    {
      return new Twist( range, frontSegment, backSegment );
    }
  }

  /**
   * @param element
   *          <code><br>
   *              &lt;cross bobbins=&quot;<em>first</em>-<em>last</em>&quot;&gt;<br>
   *              &nbsp;&nbsp;&lt;back ... /&gt;<br>
   *              &nbsp;&nbsp;&lt;front ... /&gt;<br>
   *              &lt;/cross&gt;<br>
     *            </code> The order of the front and back element doesn't matter.
   *          By definition the front thread goes from left to right.
   * @return
   */
  private static Cross createCross(
      Element element)
  {
    return new SwitchFactory( element ).createCross();
  }

  /**
   * @param element
   *          <code><br>
   *              &lt;twist bobbins=&quot;<em>first</em>-<em>last</em>&quot;&gt;<br>
   *              &nbsp;&nbsp;&lt;back ... /&gt;<br>
   *              &nbsp;&nbsp;&lt;front ... /&gt;<br>
   *              &lt;/twist&gt;<br>
   *              </code> The order of the front and back element doesn't matter.
   *          By definition the front thread goes from right to left.
   * @return an instance as defined by the element
   */
  public static Twist createTwist(
      Element element)
  {
    return new SwitchFactory( element ).createTwist();
  }

  private static class SegmentFactory
  {

    private Point start, c1, c2, end;
    private Element element;

    SegmentFactory(Element element)
    {
      this.element = element;
      String start = element.getAttribute( "start" );
      String end = element.getAttribute( "end" );
      String c1 = element.getAttribute( "c1" );
      String c2 = element.getAttribute( "c2" );
      if ((start == null) || start.equals( "" ))
        throw new IllegalArgumentException(
            "mandatory attribute start is missing" );
      if ((end == null) || end.equals( "" ))
        throw new IllegalArgumentException(
            "mandatory attribute end is missing" );
      this.start = createPoint( start );
      this.end = createPoint( end );
      this.c1 = ((c1 == null) || c1.equals( "" ) ? null : createPoint( c1 ));
      this.c2 = ((c2 == null) || c2.equals( "" ) ? null : createPoint( c2 ));
    }

    PairSegment createPairSegment()
    {
      int twistMarkLength = 0;
      if (element.getAttributes().getNamedItem( "mark" ) != null) {
        try {
          twistMarkLength = Integer.parseInt( element.getAttribute( "mark" ) );
        } catch (NumberFormatException e) {
          twistMarkLength = 9 * new Style().getWidth();
        }
      }
      return new PairSegment( start, c1, c2, end, twistMarkLength );
    }

    ThreadSegment createThreadSegment()
    {
      return new ThreadSegment( start, c1, c2, end );
    }
  }

  /**
   * Creates a new instance of ThreadSegment.
   * 
   * @param element
   *          <code>&lt;front&nbsp;...&gt;</code> or
   *          <code>&lt;back&nbsp;...&gt;</code>
   */
  private static ThreadSegment createThreadSegment(
      Element element)
  {
    return new SegmentFactory( element ).createThreadSegment();
  }

  /**
   * Creates a new instance of Segment.
   * 
   * @param element
   *          XML element, one of: <pair ...>, <back ...>, <front ...>
   */
  private static PairSegment createPairSegment(
      Element element)
  {
    return new SegmentFactory( element ).createPairSegment();
  }

  /**
   * Creates a new instance of Style from an XML element with style property
   * attributes.
   * 
   * @param element
   *          XML element of the form
   *          &lt;...&nbsp;width="..."&nbsp;color="..."&gt;
   */
  private static Style createStyle(
      Element element)
  {

    Style style = new Style();
    setStyle( element, style );

    return style;
  }

  private static ThreadStyle createThreadStyle(
      Node child)
  {
    Element grandChild = (Element) child.getFirstChild();
    ThreadStyle threadStyle = new ThreadStyle();
    threadStyle.apply( createStyle( (Element) child ) );
    if (grandChild != null)
      threadStyle.getShadow().apply( createStyle( grandChild ) );
    return threadStyle;
  }

  private static void setStyle(
      Element element,
      Style style)
  {
    if (element == null) return;
    style.setColor( element.getAttribute( "color" ) );
    style.setWidth( element.getAttribute( "width" ) );
  }

  public static Stitch createStitch(
      Element element)
  {

    Range range = createRange( element );
    Style style = new Style();
    List<Pin> pins = new Vector<Pin>();
    List<Switch> switches = new Vector<Switch>();
    List<PairSegment> pairs = new Vector<PairSegment>( range.getCount() );
    int pairCountDown = range.getCount();

    for //
    (Node child = element.getFirstChild() //
    ; child != null //
    ; child = child.getNextSibling() //
    ) {
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        ElementType childType = ElementType.valueOf( child.getNodeName() );
        Element childElement = (Element) child;
        if (childType == ElementType.cross) {
          Cross cross = createCross( childElement );
          register( childElement, cross );
          switches.add( cross );
        } else if (childType == ElementType.twist) {
          Twist twist = createTwist( childElement );
          register( childElement, twist );
          switches.add( twist );
        } else if (childType == ElementType.pin) {
          Pin pin = createPin( childElement );
          register( childElement, pin );
          pins.add( pin );
        } else if (childType == ElementType.style) {
          style = createStyle( childElement );
        } else if (childType == ElementType.pair) {
          if (pairCountDown-- > 0) {
            pairs.add( createPairSegment( childElement ) );
          } else {
            throw new RuntimeException( "adding pairs not yet implemented." );
          }
        }
      }
    }
    for (Segment segment : pairs) {
      segment.setStyle( style );
    }
    Stitch s = new Stitch( range, pairs, switches, pins, getTitle( element ) );
    register( element, s );
    return s;
  }

  /**
   * @param element
   *          an XML element of the form
   *          <code>&lt;pin position"<em>x,y</em>"&gt;</code>
   */
  private static Pin createPin(
      Element element)
  {
    String attribute = element.getAttribute( AttributeType.position.toString() );
    return new Pin( createPoint( attribute ) );
  }

  /**
   * Creates a new instance of Range.
   * 
   * @param element
   *          XML element of the form:
   *          <ul>
   *          <li><code>&lt;cross bobbins="<em>first-last</em>"&gt;</code></li>
   *          <li><code>&lt;twist bobbins="<em>first-last</em>"&gt;</code></li>
   *          <li><code>&lt;stitch  pairs="<em>first-last</em>"&gt;</code></li>
   *          <li><code>&lt;group   pairs="<em>first-last</em>"&gt;</code></li>
   *          <li><code>&lt;copy    pairs="<em>first-last</em>"&gt;</code></li>
   *          </ul>
   */
  private static Range createRange(
      Element element)
  {
    final String tag = ElementType.getRangeAttribute( element.getNodeName() );
    final String value = element.getAttribute( tag );
    final int first;
    final int last;
    final String xy[] = value.split( RANGE_SEPARATOR );
    try {
      if (xy.length == 1) {
        first = java.lang.Integer.valueOf( xy[0] ).intValue();
        last = java.lang.Integer.valueOf( xy[0] ).intValue();
      } else if (xy.length == 2) {
        first = java.lang.Integer.valueOf( xy[0] ).intValue();
        last = java.lang.Integer.valueOf( xy[1] ).intValue();
      } else {
        throw invalidRange( element.getNodeName(), tag, value );
      }
    } catch (java.lang.NumberFormatException e) {
      throw invalidRange( element.getNodeName(), tag, value );
    }
    return new Range( first, last );
  }

  private static RuntimeException invalidRange(
      String elementTag,
      String attributeTag,
      String value)
  {
    return new IllegalArgumentException( "invalid or missing range:\n<"
        + elementTag + " ... " //
        + attributeTag + "='" + value + "' ...>" );
  }

  private static Element getMandatoryElement(
      Element element,
      ElementType tag)
  {
    String tagString = tag.toString();
    NodeList nodeList = element.getElementsByTagName( tagString );
    if (nodeList.getLength() != 1)
      throw new IllegalArgumentException( "expecting exactly 1 " + tagString
          + "; found " + nodeList.getLength() );
    return (Element) nodeList.item( 0 );
  }

  private static void checkRangeIs2(
      Range range)
  {
    if (range.getCount() != 2)
      throw new IllegalArgumentException( "range should span 2 threads got: "
          + range.toString() );
  }

  private static void register(
      Element element,
      Partition p)
  {
    element.setUserData( MODEL_TO_DOM, p, null );
    Object orphan = element.getUserData( TreeExpander.CLONE_TO_ORPHAN );
    if (orphan== null)
      orphan = element.getUserData( TreeExpander.INDIRECT_CLONE_TO_ORPHAN );
    p.setSourceObject( orphan==null?element:orphan );
    if (element.getAttribute( AttributeType.display.toString() ).matches(
        "(no)|(No)|(NO)|(false)|(False)|(FALSE)" )) {
      p.setVisible( false );
    }
  }

  public static Point createPoint(
      String s)
  {

    String xy[] = s.split( SEPARATOR );
    try {
      return new Point( //
          valueOf( xy[0] ).doubleValue(),//
          valueOf( xy[1] ).doubleValue() );
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException( "invalid coordinates: " + s );
    }
  }

}