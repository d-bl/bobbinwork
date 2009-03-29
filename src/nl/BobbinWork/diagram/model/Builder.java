package nl.BobbinWork.diagram.model;

import static nl.BobbinWork.diagram.xml.ElementType.*;

import java.util.Vector;

import nl.BobbinWork.diagram.xml.AttributeType;
import nl.BobbinWork.diagram.xml.ElementType;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Builder {

    private static final String RANGE_SEPARATOR = "-";

    private static class ChainedPairsPartitionFactory {
        Element element;
        Vector<Pin> pins = new Vector<Pin>();
        Vector<MultiplePairsPartition> partitions = new Vector<MultiplePairsPartition>();
        
        ChainedPairsPartitionFactory(Element element) {
            this.element = element;
            for //
            (Node child = element.getFirstChild() //
            ; child != null //
            ; child = child.getNextSibling()) //
            {
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    ElementType childType = ElementType.valueOf(child.getNodeName());
                    Element childElement = (Element) child;
                    if (childType == ElementType.group) {
                        Group group = Builder.createGroup(childElement);
                        register(element, group);
                        partitions.add(group);
                    } else if (childType == ElementType.stitch) {
                        Stitch stitch = createStitch(childElement);
                        register(element, stitch);
                        partitions.add(stitch);
                    } else if (childType == ElementType.pin) {
                        pins.add(Builder.createPin(childElement));
                    }
                }
            }
        }
        Group createGroup() {
            return null;//new Group(createRange(element), partitions, pins);
        }
        Diagram createDiagram() {
            return null;//new Diagram(new Range(0, 0), partitions, pins);
        }
    }
    static Group createGroup (Element element) {
        return new ChainedPairsPartitionFactory(element).createGroup(); 
    }
    
    static Diagram createDiagram (Element element) {
        return new ChainedPairsPartitionFactory(element).createDiagram(); 
    }
    
    private static class SwitchFactory {

        private Range range;
        private ThreadSegment frontSegment;
        private ThreadSegment backSegment;
        
        SwitchFactory(Element element) {
            range = createRange(element);
            frontSegment = createThreadSegment(getMandatoryElement(element, front));
            backSegment = createThreadSegment(getMandatoryElement(element, back));
            checkRangeIs2(range);
        }
        Cross createCross() {
            return new Cross(range, frontSegment, backSegment);
        }
        Twist createTwist() {
            return new Twist(range, frontSegment, backSegment);
        }
    }

    /**
     * @param element
     *            <code><br>
     *              &lt;cross bobbins=&quot;<em>first</em>-<em>last</em>&quot;&gt;<br>
     *              &nbsp;&nbsp;&lt;back ... /&gt;<br>
     *              &nbsp;&nbsp;&lt;front ... /&gt;<br>
     *              &lt;/cross&gt;<br>
     *            </code> The order of the front and back element doesn't
     *            matter. By definition the front thread goes from left to
     *            right.
     * @return
     */
    private static Cross createCross(Element element) {
        return new SwitchFactory(element).createCross();
    }

    /**
     * @param element
     *            <code><br>
     *              &lt;twist bobbins=&quot;<em>first</em>-<em>last</em>&quot;&gt;<br>
     *              &nbsp;&nbsp;&lt;back ... /&gt;<br>
     *              &nbsp;&nbsp;&lt;front ... /&gt;<br>
     *              &lt;/twist&gt;<br>
     *            </code> The order of the front and back element doesn't
     *            matter. By definition the front thread goes from right to
     *            left.
     * @return
     */
    public static Twist createTwist(Element element) {
        return new SwitchFactory(element).createTwist();
    }

    private static class SegmentFactory {
        
        private Point start, c1, c2, end;
        private Element element;
        
        SegmentFactory(Element element){
            this.element = element;
            String start = element.getAttribute("start");
            String end = element.getAttribute("end");
            String c1 = element.getAttribute("c1");
            String c2 = element.getAttribute("c2");
            if ( (start == null) || start.equals("") ) 
                throw new IllegalArgumentException("mandatory attribute start is missing");
            if ( (end == null) || end.equals("") ) 
                throw new IllegalArgumentException("mandatory attribute end is missing");
            this.start = new Point(start);
            this.end = new Point(end);
            this.c1 = ( (c1 == null) || c1.equals("") ? null :  new Point(c1) );
            this.c2 = ( (c2 == null) || c2.equals("") ? null :  new Point(c2) );
        }
        
        Segment createPairSegment() {
            int twistMarkLength = 0;
            if (element.getAttributes().getNamedItem("mark") != null) {
                try {
                    twistMarkLength = Integer.parseInt(element.getAttribute("mark"));
                } catch (NumberFormatException e) {
                    twistMarkLength = 9 * new Style().getWidth();
                }
            }
            return new Segment(start, c1, c2, end, twistMarkLength);
        }
        
        ThreadSegment createThreadSegment() {
            return new ThreadSegment(start, c1, c2, end);
        }
    }
    
    /** Creates a new instance of ThreadSegment. 
     * 
     * @param element
     *            <code>&lt;front&nbsp;...&gt;</code> or
     *            <code>&lt;back&nbsp;...&gt;</code>
     */
    private static ThreadSegment createThreadSegment(Element element) {
        return new SegmentFactory(element).createThreadSegment();
    }
    
    /**
     * Creates a new instance of Segment.
     * 
     * @param element
     *            XML element, one of: <pair ...>, <back ...>, <front ...>
     */
    private static Segment createPairSegment(Element element) {
        return new SegmentFactory(element).createPairSegment();
    }
    
    private static void setStyle(Element element, Style style) {
        style.setColor(element.getAttribute("color"));
        style.setWidth(element.getAttribute("width"));
    }

    /**
     * Creates a new instance of Style from an XML element with style property
     * attributes.
     * 
     * @param element
     *            XML element of the form
     *            &lt;...&nbsp;width="..."&nbsp;color="..."&gt;
     */
     private static Style createStyle(Element element) {

        Style style = new Style();
        setStyle(element, style);

        return style;
    }

    /**
     * Creates a new instance of ThreadStyle from an XML element with style property
     * attributes. 
     * 
     * @param element
     *            XML element of the form:<br>
     *            &lt;style&nbsp;.../&gt;<br>
     *            or: &lt;style...&gt;&lt;shadow&nbsp;.../&gt;&lt;/style&gt;
     */
    // TODO: the shadow should also be expresible in percentages of the core.
    static ThreadStyle createThreadStyle(Element element) {
        
        ThreadStyle style = new ThreadStyle();
        setStyle(element, style);
    
        Element child = getOptionalElement(element, shadow);
        if ( child == null ) return style;
        
        Style b = style.getBackGround();
        String c = child.getAttribute("color");
        String w = child.getAttribute("width");
        if (c != null && !c.equals("")) b.setColor(c);
        if (w != null && !w.equals("")) b.setWidth(w);
        return style;
    }
    
    static Stitch createStitch(Element element) {

        Range range = createRange(element);
        Style style = new Style();
        Vector<Pin> pins = new Vector<Pin>();
        Vector<Switch> switches = new Vector<Switch>();
        Vector<Segment> pairs = new Vector<Segment>(range.getCount());
        int pairCountDown = range.getCount();

        for //
        (Node child = element.getFirstChild() //
        ; child != null //
        ; child = child.getNextSibling() //
        ) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                ElementType childType = ElementType.valueOf(child.getNodeName());
                Element childElement = (Element) child;
                if ( childType == ElementType.cross ) {
                    Cross cross = createCross(childElement);
                    register(childElement, cross);
                    switches.add(cross);
                } else if ( childType == ElementType.twist ) {
                    Twist twist = createTwist(childElement);
                    register(childElement, twist);
                    switches.add(twist);
                } else if ( childType == ElementType.pin ) {
                    Pin pin = createPin(childElement);
                    register(childElement, pin);
                    pins.add(pin);
                } else if ( childType == ElementType.style ) {
                    style = createStyle(childElement);
                } else if ( childType == ElementType.pair ) {
                    if (pairCountDown-- > 0) {
                        pairs.add(createPairSegment(childElement));
                    } else {
                        throw new RuntimeException("adding pairs not yet implemented.");
                    }
                }
            }
        }
        for (Segment segment:pairs) {
            segment.style = style;
        }
        Stitch s = new Stitch(range,pairs,switches,pins);
        register(element, s);
        return s;
    }
    
    /**
     * @param element
     *            an XML element of the form
     *            <code>&lt;pin position"<em>x,y</em>"&gt;</code>
     */
    static Pin createPin(Element element) {
        String attribute = element.getAttribute(AttributeType.position.toString());
        return new Pin(new Point(attribute));
}

    /**
     * Creates a new instance of Range.
     * 
     * @param element
     *            XML element of the form:
     *            <ul>
     *            <li><code>&lt;cross bobbins="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;twist bobbins="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;stitch  pairs="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;group   pairs="<em>first-last</em>"&gt;</code></li>
     *            <li><code>&lt;copy    pairs="<em>first-last</em>"&gt;</code></li>
     *            </ul>
     */
    static Range createRange(Element element) {
        String tag = ElementType.getRangeAttribute(element.getNodeName());
        String value = element.getAttribute(tag);
        int first;
        int last;
        String xy[] = value.split(RANGE_SEPARATOR);
        if (xy.length!=2) throw invalidRange(element.getNodeName(), tag, value);
        try {
            first = java.lang.Integer.valueOf(xy[0]).intValue();
            last = java.lang.Integer.valueOf(xy[1]).intValue();
        } catch (java.lang.NumberFormatException e) {
            throw invalidRange(element.getNodeName(), tag, value);
        }
        return new Range(first,last);
    }

    private static RuntimeException invalidRange(String elementTag,
            String attributeTag, String value) {
        return new RuntimeException("invalid or missing range:\n<"+elementTag+" ... " //
                + attributeTag + "='" + value + "' ...>");
    }

    private static Element getMandatoryElement(Element element, ElementType tag) {
        String tagString = tag.toString();
        NodeList nodeList = element.getElementsByTagName(tagString);
        if (nodeList.getLength() != 1)
            throw new IllegalArgumentException("expecting exactly 1 "
                    + tagString + "; found " + nodeList.getLength());
        return (Element) nodeList.item(0);
    }

    private static Element getOptionalElement(Element element, ElementType tag) {
        String tagString = tag.toString();
        NodeList nodeList = element.getElementsByTagName(tagString);
        int length = nodeList.getLength();
        if (length < 1) return null;
        if (length > 1)
            throw new IllegalArgumentException("expecting at most 1 "
                    + tagString + "; found " + length);
        return (Element) nodeList.item(0);
    }
    
    private static void checkRangeIs2(Range range) {
        if (range.getCount() != 2)
            throw new IllegalArgumentException(
                    "range should span 2 threads got: " + range.toString());
    }

    private static void register(Element element, Partition p) {
        element.setUserData(Partition.MODEL_TO_DOM, p, null);
        if (element.getAttribute(AttributeType.display.toString()).matches(
                "(no)|(No)|(NO)|(false)|(False)|(FALSE)")) {
            p.visible = false;
        }
    }
}