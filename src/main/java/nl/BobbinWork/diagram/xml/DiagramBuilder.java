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
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import nl.BobbinWork.bwlib.gui.Localizer;
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

    ChainedPairsPartitionFactory(final Element element)
    {
      this.element = element;
      partitionTitle = getTitle( element );
      for //
      (Node child = element.getFirstChild() //
      ; child != null //
      ; child = child.getNextSibling()) //
      {
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          final ElementType childType =
              ElementType.valueOf( child.getNodeName() );
          final Element childElement = (Element) child;
          switch (childType) {
          case pin:
            parts.add( DiagramBuilder.createPin( childElement ) );
            break;
          case group:
            final MultiplePairsPartition part =
                DiagramBuilder.createGroup( childElement );
            register( childElement, part );
            parts.add( part );
            break;
          case stitch:
            try {
              final MultiplePairsPartition part2 = createStitch( childElement );
              register( childElement, part2 );
              parts.add( part2 );
            } catch (IllegalArgumentException exception){
              try {
                throw new RuntimeException (exception+XmlResources.toXmlString( childElement ));
              } catch (TransformerException e) {
                throw exception;
              }
            }
            break;
          case new_bobbins:
            final ThreadStyle style = createThreadStyle( child.getFirstChild() );
            final String ranges[] =
                childElement.getAttribute( "nrs" ).split( "," );
            for (final String range : ranges) {
              final String[] nrs = range.split( "-" );
              final int start;
              final int end;
              try {
                start = parseRangeNr( nrs[0] );
                end = (nrs.length > 1 ? parseRangeNr( nrs[1] ) : start);
              } catch (final NumberFormatException e) {
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
      return new Diagram( parts, partitionTitle );
    }
  }

  private static Element getFirst(
      final Element element,
      final ElementType type)
  {
    final NodeList titleList = element.getElementsByTagName( type.toString() );
    if (titleList == null || titleList.getLength() <= 0) return null;
    return ((Element) titleList.item( 0 ));
  }

  private static String getTitle(
      final Element element)
  {
    final Element t = getFirst( element, title );
    if (t == null) return null;
    final String userLang = Localizer.getLanguage();
    final NodeList list = element.getElementsByTagName( value.toString() );
    if (list == null) return null;
    int length = list.getLength();
    if (userLang != null) {
      // else JUnit test
      for (int i = 0; i < length; i++) {
        NamedNodeMap attributes = list.item( i ).getAttributes();
        if (attributes.getNamedItem( AttributeType.lang.toString() )
            .getTextContent().matches( userLang )) {
          return list.item( i ).getTextContent();
        }
      }
    } 
    if (length <= 0) return null;
    return list.item( 0 ).getTextContent();
  }

  private static Group createGroup(
      final Element element)
  {
    return new ChainedPairsPartitionFactory( element ).createGroup();
  }

  public static Diagram createDiagram(
      final Element element)
  {
    final Diagram diagram =
        new ChainedPairsPartitionFactory( element ).createDiagram();
    DiagramBuilder.register( element, diagram );
    return diagram;
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
    final Document parsed = new XmlResources().parse( s );
    TreeExpander.replaceCopyElements( parsed.getDocumentElement() );
    return new ChainedPairsPartitionFactory( parsed.getDocumentElement() )
        .createDiagram();
  }

  private static Diagram convert(
      final Document parsed) throws XPathExpressionException
  {
    final Element element = parsed.getDocumentElement();
    TreeExpander.replaceCopyElements( element );
    final Diagram diagram = createDiagram( element );
    register( element, diagram );
    return diagram;
  }

  public static Diagram createDiagramModel(
      final URI uri)
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

    private final Range range;
    private final ThreadSegment[] frontSegments;
    private final ThreadSegment[] backSegments;

    SwitchFactory(final Element element)
    {
      range = createRange( element );
      frontSegments = 
          createThreadSegments( getMandatoryElement( element, front ) );
      backSegments = createThreadSegments( getMandatoryElement( element, back ) );
    }

    Cross createCross()
    {
      return new Cross( range, frontSegments, backSegments );
    }

    Twist createTwist()
    {
      return new Twist( range, frontSegments, backSegments );
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
      final Element element)
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
   *              </code> The order of the front and back element doesn't
   *          matter. By definition the front thread goes from right to left.
   * @return an instance as defined by the element
   */
  public static Twist createTwist(
      final Element element)
  {
    return new SwitchFactory( element ).createTwist();
  }

  private static class SegmentFactory
  {

    private final Point start, c1, c2, end;
    private final Element element;

    SegmentFactory(final Element element)
    {
      this.element = element;
      final String start = element.getAttribute( "start" );
      final String end = element.getAttribute( "end" );
      final String c1 = element.getAttribute( "c1" );
      final String c2 = element.getAttribute( "c2" );
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
        } catch (final NumberFormatException e) {
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
   * Creates a new instance(s) of ThreadSegment(s).
   * 
   * @param element
   *          <code>&lt;front&nbsp;...&gt;</code> or
   *          <code>&lt;back&nbsp;...&gt;</code>
   */
  private static ThreadSegment[] createThreadSegments(
      final NodeList elements)
  {
    ThreadSegment[] result = new ThreadSegment[elements.getLength()];
    for (int i=0 ; i<elements.getLength() ; i++) {
      Element item = (Element) elements.item( i );
      result[i] = new SegmentFactory( item ).createThreadSegment();
    }
    return result;
  }

  /**
   * Creates a new instance of Segment.
   * 
   * @param element
   *          XML element, one of: <pair ...>, <back ...>, <front ...>
   */
  private static PairSegment createPairSegment(
      final Element element)
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
      final Element element)
  {

    final Style style = new Style();
    setStyle( element, style );

    return style;
  }

  private static ThreadStyle createThreadStyle(
      final Node child)
  {
    final Element grandChild = (Element) child.getFirstChild();
    final ThreadStyle threadStyle = new ThreadStyle();
    threadStyle.apply( createStyle( (Element) child ) );
    if (grandChild != null)
      threadStyle.getShadow().apply( createStyle( grandChild ) );
    return threadStyle;
  }

  private static void setStyle(
      final Element element,
      final Style style)
  {
    if (element == null) return;
    style.setColor( element.getAttribute( "color" ) );
    style.setWidth( element.getAttribute( "width" ) );
  }

  public static Stitch createStitch(
      final Element element)
  {

    final Range range = createRange( element );
    Style style = new Style();
    final List<Pin> pins = new Vector<Pin>();
    final List<Switch> switches = new Vector<Switch>();
    final List<PairSegment> pairs = new Vector<PairSegment>( range.getCount() );
    int pairCountDown = range.getCount();

    for //
    (Node child = element.getFirstChild() //
    ; child != null //
    ; child = child.getNextSibling() //
    ) {
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        final ElementType childType = ElementType.valueOf( child.getNodeName() );
        final Element childElement = (Element) child;
        if (childType == ElementType.cross) {
          final Cross cross = createCross( childElement );
          register( childElement, cross );
          switches.add( cross );
        } else if (childType == ElementType.twist) {
          final Twist twist = createTwist( childElement );
          register( childElement, twist );
          switches.add( twist );
        } else if (childType == ElementType.pin) {
          final Pin pin = createPin( childElement );
          register( childElement, pin );
          pins.add( pin );
        } else if (childType == ElementType.style) {
          style = createStyle( childElement );
        } else if (childType == ElementType.pair) {
          if (pairCountDown-- > 0) {
            pairs.add( createPairSegment( childElement ) );
          } else {
            throw new RuntimeException( "adding pairs not yet implemented [" + getTitle( element ) + "]" );
          }
        }
      }
    }
    for (final Segment segment : pairs) {
      segment.setStyle( style );
    }
    final Stitch s =
        new Stitch( range, pairs, switches, pins, getTitle( element ) );
    register( element, s );
    return s;
  }

  /**
   * @param element
   *          an XML element of the form
   *          <code>&lt;pin position"<em>x,y</em>"&gt;</code>
   */
  private static Pin createPin(
      final Element element)
  {
    final String attribute =
        element.getAttribute( AttributeType.position.toString() );
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
      final Element element)
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
    } catch (final java.lang.NumberFormatException e) {
      throw invalidRange( element.getNodeName(), tag, value );
    }
    return new Range( first, last );
  }

  private static RuntimeException invalidRange(
      final String elementTag,
      final String attributeTag,
      final String value)
  {
    return new IllegalArgumentException( "invalid or missing range:\n<"
        + elementTag + " ... " //
        + attributeTag + "='" + value + "' ...>" );
  }

  private static NodeList getMandatoryElement(
      final Element element,
      final ElementType tag)
  {
    final String tagString = tag.toString();
    return element.getElementsByTagName( tagString );
  }

  static void register(
      final Element element,
      final Partition p)
  {
    element.setUserData( MODEL_TO_DOM, p, null );
    Object orphan = element.getUserData( TreeExpander.CLONE_TO_ORPHAN );
    if (orphan == null)
      orphan = element.getUserData( TreeExpander.INDIRECT_CLONE_TO_ORPHAN );
    String id = element.getAttribute( "id" );
    p.setSourceObject( orphan == null ? element : orphan, id );
    if (element.getAttribute( AttributeType.display.toString() ).matches(
        "(no)|(No)|(NO)|(false)|(False)|(FALSE)" )) {
      p.setVisible( false );
    }
  }

  public static Point createPoint(
      final String s)
  {

    final String errorMessage = "Example to specify a point [x,y]: [2.3,5.6] got: ["+s+"]";
    final String xy[] = s.split( SEPARATOR );
    if (xy.length<2){
      throw new IllegalArgumentException(errorMessage);
    }
    try {
      return new Point( //
          valueOf( xy[0] ).doubleValue(),//
          valueOf( xy[1] ).doubleValue() );
    } catch (final NumberFormatException e) {
      throw new IllegalArgumentException( errorMessage );
    }
  }

}