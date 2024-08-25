package entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import algorithms.Coordinates;
import algorithms.Toolbox;
import states.SimulationState;
import traffic.Intersection;

public class LinearRoad extends Road {
	
	public LinearRoad(int originIndex, int destinationIndex, Intersection intersection) {
		super();
		this.origin = SimulationState.nodes.get(originIndex);
		this.destination = SimulationState.nodes.get(destinationIndex);
		SimulationState.roads.add(this);
		index = indexCounter;
		indexCounter++;
		
		double deltaX = destination.location.x - origin.location.x;
		double deltaY = destination.location.y - origin.location.y;
		roadLength = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		isIntersection = intersection != null;
		this.intersection = intersection;
	}
	
	public LinearRoad(Node origin, Node destination, Intersection intersection) {
		super();
		this.origin = origin;
		this.destination = destination;
		SimulationState.roads.add(this);
		index = indexCounter;
		indexCounter++;
		
		double deltaX = destination.location.x - origin.location.x;
		double deltaY = destination.location.y - origin.location.y;
		roadLength = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		isIntersection = intersection != null;
		this.intersection = intersection;
	}
	
	public LinearRoad(Road originRoad, Road destinationRoad, Intersection intersection) {
		super();
		this.origin = originRoad.destination;
		this.destination = destinationRoad.origin;
		SimulationState.roads.add(this);
		index = indexCounter;
		indexCounter++;
		
		double deltaX = destination.location.x - origin.location.x;
		double deltaY = destination.location.y - origin.location.y;
		roadLength = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		isIntersection = intersection != null;
		this.intersection = intersection;
	}
	
	public Coordinates positionFraction(double fraction) {
		return Toolbox.map(fraction, 0.0, 1.0, origin.location, destination.location);
	}
	
	public Coordinates position(double t) {
		return positionFraction(t / roadLength);
	}
	
	public void render(Graphics2D g) {
		Coordinates originCoordinates = origin.location, destinationCoordinates = destination.location;
		Coordinates originScreen = Toolbox.coordinatesToScreen(originCoordinates);
		Coordinates destinationScreen = Toolbox.coordinatesToScreen(destinationCoordinates);
		if (isIntersection) {
			if (clearToAccept) g.setColor(Color.GREEN);
			else if (!queue.isEmpty() && queue.get(queue.size() - 1).t >= 0.3) g.setColor(Color.ORANGE);
			else return;
		} else {
			g.setColor(Color.GREEN);
		}
		
		g.setStroke(new BasicStroke(1));
		g.drawLine((int) originScreen.x, (int) originScreen.y, (int) destinationScreen.x, (int) destinationScreen.y);
	}
	
}
