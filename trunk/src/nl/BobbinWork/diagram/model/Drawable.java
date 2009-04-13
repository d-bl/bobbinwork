package nl.BobbinWork.diagram.model;

import java.awt.Shape;

public class Drawable {
	private Shape shape;
	private Style style;
	
	Drawable(final Shape shape, final Style style){
		this.shape = shape;
		this.style = style;
	}
	public Shape getShape() {
		return shape;
	}

	public Style getStyle() {
		return style;
	}
}
