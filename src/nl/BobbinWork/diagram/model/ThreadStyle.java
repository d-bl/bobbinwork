/* ThreadStyle.java Copyright 2006-2007 by J. Pol
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
 * The looks for a line segment drawn in two strokes.
 *
 * @author J. Pol
 */
public class ThreadStyle extends Style {//TODO inheritance->composition

	/** The stroke which is drawn as first. */
    private Style shadow = null;

	/**
	 * Sets the color of the top stroke. The shadow color is set to a default related
	 * to the top stroke.
	 */
    public void setColor(Color color) {
        super.setColor(color);
        setDefaultShadow();
    }

	/**
	 * Sets the width of the top stroke. The shadow width is set to a default related
	 * to the top stroke.
	 */
    public void setWidth(int width) {
        super.setWidth(width);
    }

    private void setDefaultShadow() {
    	Color color = getColor();
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
        shadow.setWidth(getWidth() * 5);
    }
    
    /** Creates a new instance with default values. */
    public ThreadStyle() {
        super();
        setDefaultShadow();
    }

	/**
	 * Creates a new instance with custom values.
	 * 
	 * @param core
	 *            The stroke drawn on top of the shadow.
	 * @param shadow
	 *            The first drawn stroke. Null defaults to brighter and wider
	 *            than the core.
	 */
	public ThreadStyle(Style core, Style shadow) {
		super(core);
		if (shadow == null) {
			setDefaultShadow();
		} else {
			getShadow().setColor(shadow.getColor());
			getShadow().setWidth(shadow.getWidth());
		}
	}
    
    /**
     * Creates a new instance of threadStyle.
     * 
     * @param threadStyle
     *            object to be cloned.
     */
    public ThreadStyle(ThreadStyle threadStyle) {
        super(threadStyle);
        shadow = new Style(threadStyle.getShadow());
    }

    public Style getShadow() {
        return shadow;
    }

    public void apply(ThreadStyle newStyle) {
    	super.apply(newStyle);
        shadow.setColor(newStyle.getShadow().getColor());
        shadow.setWidth(newStyle.getShadow().getWidth());
    }
}
