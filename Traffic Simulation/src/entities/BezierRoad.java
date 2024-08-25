package entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import algorithms.Coordinates;
import algorithms.Toolbox;
import states.SimulationState;
import traffic.Intersection;

public class BezierRoad extends Road {
	
	public Road originRoad, destinationRoad;
	public Coordinates crossPoint;
	
	public BezierRoad(int originIndex, int destinationIndex, Intersection intersection) {
		super();
		this.originRoad = SimulationState.roads.get(originIndex);
		this.destinationRoad = SimulationState.roads.get(destinationIndex);
		this.origin = originRoad.destination;
		this.destination = destinationRoad.origin;
		SimulationState.roads.add(this);
		index = indexCounter;
		indexCounter++;
		crossPoint = crossPoint();
		roadLength = calculateRoadLength();
		isIntersection = intersection != null;
		this.intersection = intersection;
	}
	
	public BezierRoad(Road origin, Road destination, Intersection intersection) {
		super();
		this.originRoad = origin;
		this.destinationRoad = destination;
		this.origin = originRoad.destination;
		this.destination = destinationRoad.origin;
		SimulationState.roads.add(this);
		index = indexCounter;
		indexCounter++;
		crossPoint = crossPoint();
		roadLength = calculateRoadLength();
		isIntersection = intersection != null;
		this.intersection = intersection;
	}
	
	public double calculateRoadLength() {
		double distance = 0.0;
		for (double fraction = 0.0; fraction < 1.0; fraction += 0.001) {
			Coordinates p1 = positionFraction(fraction), p2 = positionFraction(fraction + 0.001);
			distance += Toolbox.distance(p1, p2);
		}
		return distance;
	}
	
	public Coordinates positionFraction(double fraction) {
		Coordinates c1 = Toolbox.map(fraction, 0.0, 1.0, origin.location, crossPoint);
		Coordinates c2 = Toolbox.map(fraction, 0.0, 1.0, crossPoint, destination.location);
		Coordinates pos = Toolbox.map(fraction, 0.0, 1.0, c1, c2);
		return pos;
	}
	
	public Coordinates position(double t) {
		return positionFraction(t / roadLength);
	}
	
	public Coordinates crossPoint() {
		Road originRoad = this.originRoad.lastPart();
		Road destinationRoad = this.destinationRoad.firstPart();
		double x0 = originRoad.origin.location.x, y0 = originRoad.origin.location.y;
		double x1 = originRoad.destination.location.x, y1 = originRoad.destination.location.y;
		double x2 = destinationRoad.origin.location.x, y2 = destinationRoad.origin.location.y;
		double x3 = destinationRoad.destination.location.x, y3 = destinationRoad.destination.location.y;
		double x = (x1 * x3 * (y0 - y2) + x0 * x3 * (-y1 + y2) + x0 * x2 * (y1 - y3) + x1 * x2 * (-y0 + y3))/(x3 * (y0 - y1) + x2 * (-y0 + y1) + (x0 - x1) * (y2 - y3));
		double y = (x0 * y1 * y2 - x0 * y1 * y3 + x1 * y0 * (y3 - y2) - x2 * y0 * y3 + x2 * y1 * y3 + x3 * y2 * (y0 - y1))/((x0 - x1) * (y2 - y3) + x2 * (y1 - y0) + x3 * (y0 - y1));
		return new Coordinates(x, y);
	}
	
	public void render(Graphics2D g) {
		if (isIntersection) {
			if (clearToAccept) g.setColor(Color.GREEN);
			else if (!queue.isEmpty() && queue.get(queue.size() - 1).t >= 0.3) g.setColor(Color.ORANGE);
			else return;
//			else g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		
//		if (lightOk) g.setColor(Color.GREEN);
//		else g.setColor(Color.RED);
		
		g.setStroke(new BasicStroke(1));
		double delta = 0.03;
		for (double fraction = 0.0; fraction < 1.0; fraction += delta) {
			Coordinates p1 = Toolbox.coordinatesToScreen(positionFraction(fraction)), p2 = Toolbox.coordinatesToScreen(positionFraction(fraction + delta));
			g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		}
	}
	
}
