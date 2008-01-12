/* ThreadStyle.java Copyright 2006-2007 by J. Falkink-Pol
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

package nl.BobbinWork.diagram.model;

import java.awt.Color;

import org.w3c.dom.Element;

import nl.BobbinWork.diagram.xml.ElementType;

/**
 * 
 * @author J. Falkink-Pol
 */
public class ThreadStyle extends Style {//TODO inheritance->composition

    private Style backGround = null;

    /**
     * Creates a new instance of ThreadStyle from an XML element with style property
     * attributes. 
     * 
     * @param element
     *            XML element of the form:
     *            &lt;style&nbsp;.../&gt;
     *            or: &lt;style...&gt;&lt;shadow&nbsp;.../&gt;&lt;/style&gt;
     */
    // TODO: the shadow should also be expresible in percentages of the core.
    public ThreadStyle(Element element) {
        super(element);
        try {
            Element child = (Element) element.getFirstChild();
            ElementType childType = ElementType.valueOf(child.getNodeName());
            if (childType == ElementType.shadow) {
                backGround = new Style(child);
            }
        } catch (Exception e) {
        }
    }

    public void setColor(Color color) {
        super.setColor(color);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        r += ((0xFF - r) * 4) / 5;
        g += ((0xFF - g) * 4) / 5;
        b += ((0xFF - b) * 4) / 5;
        if (backGround == null) {
            backGround = new Style();
        }
        backGround.setColor(new Color(r, g, b));
    }

    /** Sets a default shadow width along with the front width. */
    public void setWidth(int width) {
        super.setWidth(width);
        if (backGround == null) {
            backGround = new Style();
        }
        backGround.setWidth(width * 5);
    }

    /** Creates a new instance of ThreadStyle with default values. */
    public ThreadStyle() {
        super();
        backGround = new Style();
        backGround.setColor(Color.LIGHT_GRAY);
        backGround.setWidth(5);
    }

    /**
     * Creates a new instance (a clone) of ThreadStyle.
     * 
     * @param threadStyle
     *            object to be copied.
     */
    public ThreadStyle(ThreadStyle threadStyle) {
        super(threadStyle);
        backGround = new Style(threadStyle.getBackGround());
    }

    public Style getBackGround() {
        return backGround;
    }

    public void set(ThreadStyle threadStyle) {
        setColor(threadStyle.getColor());
        setWidth(threadStyle.getWidth());
        backGround.setColor(threadStyle.getBackGround().getColor());
        backGround.setWidth(threadStyle.getBackGround().getWidth());
    }
}
