/* Style.java Copyright 2006-2007 by J. Falkink-Pol
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

/**
 * Drawing properties.
 * 
 * @author J. Falkink-Pol
 */
public class Style {

    private int color = Color.BLACK.getRGB();

    private int width = 1;

    /** Creates a new instance of Style. */
    public Style() {
        setColor(Color.BLACK);
        setWidth(1);
    }

    /**
     * Creates a new instance of Style from an XML element with style property
     * attributes.
     * 
     * @param element
     *            XML element of the form
     *            &lt;...&nbsp;width="..."&nbsp;color="..."&gt;
     */
    public Style(Element element) {
        setColor(element.getAttribute("color"));
        setWidth(element.getAttribute("width"));
    }

    /**
     * Creates a new instance (a clone) of Style.
     * 
     * @param style
     *            The <code>Style</code> object to be copied/cloned.
     */
    public Style(Style style) {
        color = style.getColor().getRGB();
        width = style.getWidth();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setWidth(String width) {
        if ((width != null) && (width != "")) {
            this.width = java.lang.Integer.decode(width).intValue();
        }
    }

    public Color getColor() {
        return new Color(color);
    }

    /**
     * Sets the color of a pair segment, or the core or shadow color of a
     * thread.
     * 
     * @param color
     */
    public void setColor(Color color) {
        this.color = color.getRGB();
    }

    /**
     * Set the color of a pair segment, or the core or shadow of a whole thread.
     * 
     * @param color
     *            examples: red "#FF000", green "#00FF00", blue "#0000FF" for
     *            named colors
     * @see java.awt.Color
     */
    public void setColor(String color) {
        if ((color == null) || (color == "")) {
        } else if (color.matches("#[0-9A-Fa-f]{6}")) {
            this.color = Integer.decode(color).intValue();
        } else if (color.equalsIgnoreCase("Black")) {
            this.color = Color.BLACK.getRGB();
        } else if (color.equalsIgnoreCase("Blue")) {
            this.color = Color.BLUE.getRGB();
        } else if (color.equalsIgnoreCase("Cyan")) {
            this.color = Color.CYAN.getRGB();
        } else if (color.equalsIgnoreCase("Dark_gray")) {
            this.color = Color.DARK_GRAY.getRGB();
        } else if (color.equalsIgnoreCase("Gray")) {
            this.color = Color.GRAY.getRGB();
        } else if (color.equalsIgnoreCase("Green")) {
            this.color = Color.GREEN.getRGB();
        } else if (color.equalsIgnoreCase("Light_gray")) {
            this.color = Color.LIGHT_GRAY.getRGB();
        } else if (color.equalsIgnoreCase("Magenta")) {
            this.color = Color.MAGENTA.getRGB();
        } else if (color.equalsIgnoreCase("Orange")) {
            this.color = Color.ORANGE.getRGB();
        } else if (color.equalsIgnoreCase("Pink")) {
            this.color = Color.PINK.getRGB();
        } else if (color.equalsIgnoreCase("Red")) {
            this.color = Color.RED.getRGB();
        } else if (color.equalsIgnoreCase("White")) {
            this.color = Color.WHITE.getRGB();
        } else if (color.equalsIgnoreCase("Yellow")) {
            this.color = Color.YELLOW.getRGB();
        } else {
            this.color = 0x000000;
        }
    }

    /** @return Gets the XML attributes. */
    public String toString() {
        return "width=\"" + Integer.toString(width) + "\" color=\"" + colorString() + "\"";
    }

    private String colorString() {
        if (color == Color.BLACK.getRGB()) {
            return "Black";
        } else if (color == Color.BLUE.getRGB()) {
            return "Blue";
        } else if (color == Color.CYAN.getRGB()) {
            return "Cyan";
        } else if (color == Color.DARK_GRAY.getRGB()) {
            return "Dark_gray";
        } else if (color == Color.GRAY.getRGB()) {
            return "Gray";
        } else if (color == Color.GREEN.getRGB()) {
            return "Green";
        } else if (color == Color.LIGHT_GRAY.getRGB()) {
            return "Light_gray";
        } else if (color == Color.MAGENTA.getRGB()) {
            return "Magenta";
        } else if (color == Color.ORANGE.getRGB()) {
            return "Orange";
        } else if (color == Color.PINK.getRGB()) {
            return "Pink";
        } else if (color == Color.RED.getRGB()) {
            return "Red";
        } else if (color == Color.WHITE.getRGB()) {
            return "White";
        } else if (color == Color.YELLOW.getRGB()) {
            return "Yellow";
        } else {
            Color c = new Color(color);
            return "#" + Integer.toHexString(c.getRGB()).replaceFirst("..", "");
        }
    }

}
