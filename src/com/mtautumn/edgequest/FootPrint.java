package com.mtautumn.edgequest;

import java.io.Serializable;

public class FootPrint implements Serializable {
	private static final long serialVersionUID = 1L;
	public double posX;
	public double posY;
	public double opacity;
	public int direction;
	public FootPrint(double posX, double posY, int direction) {
		this.posX = posX;
		this.posY = posY;
		this.direction = direction;
		opacity = 1.0;
	}
}
