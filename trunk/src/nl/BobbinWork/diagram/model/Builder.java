package nl.BobbinWork.diagram.model;

import static nl.BobbinWork.diagram.xml.ElementType.*;
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
        validateType(element, cross);
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
        validateType(element, twist);
        Twist twist = new SwitchFactory(element).createTwist();
        register(element, twist);
        return twist;
    }

    private static void validateType(Element element, ElementType x) {
        String nodeName = element.getNodeName();
        ElementType type = ElementType.valueOf(nodeName);
        if (type != x )
            throw new IllegalArgumentException("expected element: "
                    + x);
    }
    
    private static ThreadSegment createThread(Element element, ElementType tag) {
        String tagString = tag.toString();
        NodeList nodeList = element.getElementsByTagName(tagString);
        if (nodeList.getLength() != 1)
            throw new IllegalArgumentException("expecting exactly 1 "
                    + tagString + "; found " + nodeList.getLength());
        return new ThreadSegment(((Element) nodeList.item(0)));
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
