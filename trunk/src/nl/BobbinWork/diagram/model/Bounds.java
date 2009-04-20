package nl.BobbinWork.diagram.model;

import java.awt.Polygon;

public class Bounds extends Polygon {
	private static final long serialVersionUID = 1L;
	Bounds (int[]x, int[]y, int n) {
		super(x,y,n);
	}
	Bounds(){}
	void add(Point point){
		addPoint((int) point.x, (int) point.y);
    	//System.out.print("("+(int) Math.round(point.x) +","+ (int) Math.round(point.y)+") ");
	}
	boolean equals(Bounds p) {
		return this.xpoints.equals(p.xpoints)
				&& this.ypoints.equals(p.ypoints)
				&& this.npoints == p.npoints;
	}

}
