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

/**
 * 
 * @author J. Falkink-Pol
 */
public class ThreadStyle extends Style {//TODO inheritance->composition

    private Style shadow = null;

    public void setColor(Color color) {
        super.setColor(color);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        r += ((0xFF - r) * 4) / 5;
        g += ((0xFF - g) * 4) / 5;
        b += ((0xFF - b) * 4) / 5;
        if (shadow == null) {
            shadow = new Style();
        }
        shadow.setColor(new Color(r, g, b));
    }

    /** Sets a default shadow width along with the front width. */
    public void setWidth(int width) {
        super.setWidth(width);
        if (shadow == null) {
            shadow = new Style();
        }
        shadow.setWidth(width * 5);
    }

    /** Creates a new instance of ThreadStyle with default values. */
    public ThreadStyle() {
        super();
        shadow = new Style();
        shadow.setColor(Color.LIGHT_GRAY);
        shadow.setWidth(5);
    }

    /**
     * Creates a new instance (a clone) of ThreadStyle.
     * 
     * @param threadStyle
     *            object to be copied.
     */
    public ThreadStyle(ThreadStyle threadStyle) {
        super(threadStyle);
        shadow = new Style(threadStyle.getBackGround());
    }

    public Style getBackGround() {
        return shadow;
    }

    public void set(ThreadStyle threadStyle) {
        setColor(threadStyle.getColor());
        setWidth(threadStyle.getWidth());
        shadow.setColor(threadStyle.getBackGround().getColor());
        shadow.setWidth(threadStyle.getBackGround().getWidth());
    }
}
