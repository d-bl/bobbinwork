package nl.BobbinWork.diagram.model;

import static nl.BobbinWork.diagram.xml.ElementType.back;
import static nl.BobbinWork.diagram.xml.ElementType.front;
import nl.BobbinWork.diagram.xml.AttributeType;
import nl.BobbinWork.diagram.xml.ElementType;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Builder {

    private static class SwitchFactory {

        private Range range;
        private ThreadSegment frontSegment;
        private ThreadSegment backSegment;
        
        SwitchFactory(Element element) {
            range = new Range(element);
            frontSegment = createThread(element, front);
            backSegment = createThread(element, back);
            validateRange2(range);
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
	 *              &lt;...&gt;<br>
     *            </code> The order of the front and back element doesn't
	 *            matter. By definition the front thread goes from left to
	 *            right.
	 * @return
	 */
    static Cross createCross(Element element) {
        Cross cross = new SwitchFactory(element).createCross();
        register(element, cross);
        return cross;
    }

	/**
	 * @see #createTwist(Element)
	 * @param element
	 *            Like cross but the front thread goes from right to left.
	 * @return
	 */
    public static Twist createTwist(Element element) {
        Twist twist = new SwitchFactory(element).createTwist();
        register(element, twist);
        return twist;
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
    public static ThreadSegment createThreadSegment(Element element) {
    	return new SegmentFactory(element).createThreadSegment();
    }
    
    /**
     * Creates a new instance of Segment, from an XML element with segment
     * property attributes.
     * 
     * @param element
     *            XML element, one of: <pair ...>, <back ...>, <front ...>
     */
    public static Segment createPairSegment(Element element) {
    	return new SegmentFactory(element).createPairSegment();
    }
    
    private static ThreadSegment createThread(Element element, ElementType tag) {
        String tagString = tag.toString();
        NodeList nodeList = element.getElementsByTagName(tagString);
        if (nodeList.getLength() != 1)
            throw new IllegalArgumentException("expecting exactly 1 "
                    + tagString + "; found " + nodeList.getLength());
        return createThreadSegment(((Element) nodeList.item(0)));
    }

    private static void validateRange2(Range range) {
        if (range.getCount() != 2)
            throw new IllegalArgumentException(
                    "range should span 2 threads got: " + range.toString());
    }

    private static void register(Element element, Partition p) {
        element.setUserData(Switch.MODEL_TO_DOM, p, null);
        if (element.getAttribute(AttributeType.display.toString()).matches(
                "(no)|(No)|(NO)")) {
            p.visible = false;
        }
    }
}
