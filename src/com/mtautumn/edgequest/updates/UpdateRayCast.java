package com.mtautumn.edgequest.updates;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;


public class UpdateRayCast {
	DataManager dataManager;
	public ArrayList<Torch> torches = new ArrayList<Torch>();
	public ArrayList<Line> lines = new ArrayList<Line>();
	public UpdateRayCast(DataManager dataManager) {
		this.dataManager = dataManager;
		Torch testTorch = new Torch(0.5, 6.5);
		Torch testTorch2 = new Torch(20.5, 20.5);
		torches.add(testTorch);
		torches.add(testTorch2);
	}
	public void update() {
		updateTorch(torches.get(0));
	}
	public class Point {
		public double angle;
		public double radius;
		public Point(double angle, double radius) {
			this.angle = angle;
			this.radius = radius;
		}
	}
	
	private static Point getNextArrayPoint(ArrayList<Point> points, double angle, double x, double y) {
		double smallestAngle = 10;
		for (int i = 0; i < points.size(); i++) {
			double angle1 = points.get(i).angle;
			if (angle1 < smallestAngle && angle1 > angle && points.get(i).radius != Double.NaN) {
				smallestAngle = angle1;
			}
		}
		for (int i = 0; i < points.size(); i++) {
			double angle1 = points.get(i).angle;
			if (angle1 == smallestAngle)
				return points.get(i);
		}
		return null;
	}
	
	
	private void addVertex(double angle, double radius, double range, ArrayList<Point> points, ArrayList<Line> lines, Torch torch) {
		double correctedAngle = angle;
		double correctedRadius = radius;
		if (correctedAngle > Math.PI)
			correctedAngle = Math.PI;
		if (correctedAngle < -Math.PI)
			correctedAngle = -Math.PI;
		if (correctedRadius < 0)
			correctedRadius = 0;
		if (correctedRadius > range)
			correctedRadius = range;
		double dX = Math.cos(angle) * radius;
		double dY = Math.sin(angle) * radius;
		double x = torch.posX + dX;
		double y = torch.posY - dY;
		for (int i = 0; i < lines.size(); i++) {
			if (linesIntersect(torch.posX, torch.posY, x, y, lines.get(i).x1, lines.get(i).y1, lines.get(i).x2, lines.get(i).y2)) {
				CartesianPoint intersection = getLineLineIntersection(torch.posX, torch.posY, x, y, lines.get(i).x1, lines.get(i).y1, lines.get(i).x2, lines.get(i).y2);
				double intersectionRadius = Math.sqrt(Math.pow(intersection.x - torch.posX, 2) + Math.pow(intersection.y - torch.posY, 2));
				if (intersectionRadius != Double.NaN) {
					if (intersectionRadius < correctedRadius)
					correctedRadius = intersectionRadius;
				}
				x = torch.posX + Math.cos(angle) * radius;
				y = torch.posY - Math.sin(angle) * radius;
			}
		}
			points.add(new Point(correctedAngle, correctedRadius));
	}
	public class CartesianPoint {
		double x,y;
		public CartesianPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 ||
				x3 == x4 && y3 == y4){
			return false;
		}
		// Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
		double ax = x2-x1;
		double ay = y2-y1;
		double bx = x3-x4;
		double by = y3-y4;
		double cx = x1-x3;
		double cy = y1-y3;

		double alphaNumerator = by*cx - bx*cy;
		double commonDenominator = ay*bx - ax*by;
		if (commonDenominator > 0){
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
				return false;
			}
		}else if (commonDenominator < 0){
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
				return false;
			}
		}
		double betaNumerator = ax*cy - ay*cx;
		if (commonDenominator > 0){
			if (betaNumerator < 0 || betaNumerator > commonDenominator){
				return false;
			}
		}else if (commonDenominator < 0){
			if (betaNumerator > 0 || betaNumerator < commonDenominator){
				return false;
			}
		}
		if (commonDenominator == 0){
			// This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			double y3LessY1 = y3-y1;
			double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0){
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
						x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
						x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
							y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
							y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
	public CartesianPoint getLineLineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double det1And2 = det(x1, y1, x2, y2);
		double det3And4 = det(x3, y3, x4, y4);
		double x1LessX2 = x1 - x2;
		double y1LessY2 = y1 - y2;
		double x3LessX4 = x3 - x4;
		double y3LessY4 = y3 - y4;
		double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
		if (det1Less2And3Less4 == 0){
			// the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
			return null;
		}
		double x = (det(det1And2, x1LessX2,
				det3And4, x3LessX4) /
				det1Less2And3Less4);
		double y = (det(det1And2, y1LessY2,
				det3And4, y3LessY4) /
				det1Less2And3Less4);
		return new CartesianPoint(x, y);
	}
	protected static double det(double a, double b, double c, double d) {
		return a * d - b * c;
	}
	private void updateTorch(Torch torch) {
		this.lines.clear();
		createLines(torch.posX, torch.posY, torch.range);
		ArrayList<Line> lines = new ArrayList<Line>();

		for (int i = 0; i < this.lines.size(); i++) {
			if (this.lines.get(i).isInRange(torch.posX, torch.posY, torch.range)) {
				lines.add(this.lines.get(i));
			}
		}




		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < lines.size(); i++) {
			double angle1 = Math.atan2(torch.posY-lines.get(i).y1, lines.get(i).x1 - torch.posX);
			double radius1 = Math.sqrt(Math.pow(torch.posY-lines.get(i).y1, 2) + Math.pow(torch.posX - lines.get(i).x1, 2));
			double angle2 = Math.atan2(torch.posY-lines.get(i).y2, lines.get(i).x2 - torch.posX);
			double radius2 = Math.sqrt(Math.pow(torch.posY-lines.get(i).y2, 2) + Math.pow(torch.posX - lines.get(i).x2, 2));
			addVertex(angle1, radius1, torch.range, points, lines, torch);
			addVertex(angle2, radius2, torch.range, points, lines, torch);
		}
		
		int currentPoint = 0;
		while (currentPoint < points.size()) {
			boolean removePoint = false;
			for (int i = 0; i < points.size(); i++) {
				if (points.get(i).angle == points.get(currentPoint).angle) {
					if (points.get(i).radius < points.get(currentPoint).radius) {
						removePoint = true;
					}
				}
			}
			if (removePoint) {
				points.remove(currentPoint);
			} else {
				currentPoint++;
			}
		}
		
		
		
		double angle = -Math.PI;
		double lastRadius = torch.range;
		final double increment = 0.1;

		addVertex(angle, lastRadius, torch.range, points, lines, torch);

		while (angle < Math.PI) {
			Point nextPoint = getNextArrayPoint(points, angle, torch.posX, torch.posY);
			if (nextPoint != null) {
				if (nextPoint.angle < angle + increment) {
					addVertex(nextPoint.angle, lastRadius, torch.range, points, lines, torch);					
					lastRadius = nextPoint.radius;
					angle = nextPoint.angle;
				} else {
					addVertex(angle, torch.range, torch.range, points, lines, torch);
					addVertex(angle + increment, torch.range, torch.range, points, lines, torch);
					lastRadius = torch.range;
					angle += increment;
				}
			} else {
				addVertex(angle, torch.range, torch.range, points, lines, torch);
				addVertex(angle + increment, torch.range, torch.range, points, lines, torch);
				lastRadius = torch.range;
				angle += increment;
			}
		}

		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		angle = -Math.PI;
		Point lastPoint = getNextArrayPoint(points, angle - 0.001, torch.posX, torch.posY);
		angle = lastPoint.angle;
		boolean drawing = true;
		while (drawing) {
			Point nextPoint = getNextArrayPoint(points, angle, torch.posX, torch.posY);
			if (nextPoint != null) {
				Triangle triangle = new Triangle(torch.posX, torch.posY, lastPoint.angle, lastPoint.radius, nextPoint.angle, nextPoint.radius);
				triangles.add(triangle);
				lastPoint = nextPoint;
				angle = nextPoint.angle;
			} else {
				drawing = false;
			}
		
		
		}
		
		
		torch.points = points;
		
		
		
		torch.triangles = triangles;
		//torch.removeZeroTriangles();

	}
	private void createLines(double x, double y, double range) {
		for (double x1 = x - range; x1 <= x + range; x1++) {
			for (double y1 = y - range; y1 <= y + range; y1++) {
				if (doesContainStructure(x1, y1)) {
					Line line1 = new Line(Math.floor(x1), Math.floor(y1), Math.ceil(x1), Math.floor(y1));
					Line line2 = new Line(Math.floor(x1), Math.floor(y1), Math.floor(x1), Math.ceil(y1));
					Line line3 = new Line(Math.floor(x1), Math.ceil(y1), Math.ceil(x1), Math.ceil(y1));
					Line line4 = new Line(Math.ceil(x1), Math.floor(y1), Math.ceil(x1), Math.ceil(y1));
					lines.add(line1);
					lines.add(line2);
					lines.add(line3);
					lines.add(line4);
				}
			}
		}
	}
	public boolean doesContainStructure(double x, double y) {
		int x1 = (int) Math.floor(x);
		int y1 = (int) Math.floor(y);
		if (dataManager.savable.playerStructuresMap.containsKey(x1 + "," + y1)) {
			return !dataManager.system.blockIDMap.get(dataManager.savable.playerStructuresMap.get(x1 + "," + y1)).isPassable;
		}
		return false;
	}
	public void addTorch(double x, double y) {

	}
	public class Torch {
		public double posX;
		public double posY;
		public final double range = 8;
		public ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		public ArrayList<Point> points = new ArrayList<Point>();
		public Torch(double posX, double posY) {
			this.posX = posX;
			this.posY = posY;
		}
		public void removeZeroTriangles() {
			for (int i = 0; i < triangles.size(); i++) {
				if (triangles.get(i).isZeroTriangle())
					triangles.remove(i);
			}
		}
	}
	public class Triangle {
		public double angle1;
		public double angle2;
		public double radius1;
		public double radius2;
		public double originX;
		public double originY;
		public double x1;
		public double x2;
		public double x3;
		public double y1;
		public double y2;
		public double y3;
		public void setCartesian(double originX, double originY, double p1x, double p1y, double p2x, double p2y) {
			x1 = originX;
			x2 = p1x;
			x3 = p2x;
			y1 = originY;
			y2 = p1y;
			y3 = p2y;
			angle1 = Math.atan2(originY - p1y, p1x-originX);
			angle2 = Math.atan2(originY - p2y, p2x-originX);
			radius1 = Math.sqrt(Math.pow(p1x-originX, 2)+Math.pow(p1y-originY, 2));
			radius2 = Math.sqrt(Math.pow(p2x-originX, 2)+Math.pow(p2y-originY, 2));
			this.originX = originX;
			this.originY = originY;
		}
		public Triangle(double originX, double originY, double p1Angle, double p1Radius, double p2Angle, double p2Radius) {
			this.originX = originX;
			this.originY = originY;
			this.angle1 = p1Angle;
			this.angle2 = p2Angle;
			this.radius1 = p1Radius;
			this.radius2 = p2Radius;
			x1 = originX;
			y1 = originY;
			y2 = y1 - Math.sin(p1Angle) * p1Radius;
			x2 = x1 + Math.cos(p1Angle) * p1Radius;
			y3 = y1 - Math.sin(p2Angle) * p2Radius;
			x3 = x1 + Math.cos(p2Angle) * p2Radius;
		}
		public Triangle() {

		}
		public double getSmallestAngle() {
			if (angle1 < angle2)
				return angle1;
			return angle2;
		}
		public double getLargestAngle() {
			if (angle2 < angle1)
				return angle1;
			return angle2;
		}
		public void rotateSide(int line, double x, double y, boolean adjustLength) {
			double angle = Math.atan2(originY - y, x-originX);
			if (line == 1) 
				angle1 = angle;
			else
				angle2 = angle;
			if (adjustLength) {
				double length = Math.sqrt(Math.pow(x-originX, 2)+Math.pow(y-originY, 2));
				if (line == 1)
					radius1 = length;
				else
					radius2 = length;
			}
		}
		public boolean isZeroTriangle() {
			return angle1 == angle2;
		}
	}
	public class Line {
		public double x1;
		public double x2;
		public double y1;
		public double y2;
		public Line(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}
		public boolean isInRange(double x, double y, double radius) {
			double dist1 = Math.sqrt(Math.pow(x-x1, 2) + Math.pow(y-y1, 2));
			double dist2 = Math.sqrt(Math.pow(x-x2, 2) + Math.pow(y-y2, 2));
			return (dist1 <= radius || dist2 <= radius);
		}
		public boolean isLineHorizontal() {
			return (y1 == y2);
		}
		public boolean isLineVertical() {
			return (x1 == x2);
		}
		public boolean isIntersection(double x, double y, double angle, double radius) {
			double angle1 = getPolarAngle(x, y, 1);
			double angle2 = getPolarAngle(x, y, 2);
			if (getDistance(x, y, 1) <= radius || getDistance(x, y, 2) <= radius) {
				return (angle2 <= angle && angle1 >= angle) || (angle1 <= angle && angle2 >= angle);
			}
			return false;
		}
		private double getDistance(double x, double y, int point) {
			if (point == 1)
				return Math.sqrt(Math.pow(x-x1, y-y1));
			return Math.sqrt(Math.pow(x-x2, y-y2));
		}
		public double getPolarAngle(double x, double y, int point) {
			if (point == 1) {
				return Math.atan2(y - y1, x1-x);
			}
			return Math.atan2(y - y2, x2-x);
		}
	}
}
