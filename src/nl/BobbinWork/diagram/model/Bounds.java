package nl.BobbinWork.diagram.model;

import java.awt.Polygon;

public class Bounds extends Polygon {
	private static final long serialVersionUID = 1L;
	
	Bounds (int[][]points) {
		super();
		for (int[] p:points)
		addPoint(p[0],p[1]);
	}
	
	Bounds(){}
	void add(Point point){
		addPoint((int) point.x, (int) point.y);
	}
	
	public String toString(){
		String s = "";
		for(int i=0 ; i<npoints ; i++){
			s += "("+xpoints[i]+";"+ypoints[i]+") ";
		}
		return s;
	}

}
