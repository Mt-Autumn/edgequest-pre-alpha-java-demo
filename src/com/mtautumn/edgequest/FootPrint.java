package com.mtautumn.edgequest;

public class FootPrint {
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
