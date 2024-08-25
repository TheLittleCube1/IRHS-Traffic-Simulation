package entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import algorithms.Coordinates;
import algorithms.Toolbox;
import states.SimulationState;
import traffic.Intersection;

public class CompositeRoad extends Road {
	
	public List<Road> parts = new ArrayList<Road>();
	
	public CompositeRoad(Road...parts) {
		super();
		for (Road part : parts) {
			this.parts.add(part);
			SimulationState.roads.remove(part);
		}
		this.origin = parts[0].origin;
		this.destination = parts[parts.length - 1].destination;
		SimulationState.roads.add(this);
		index = indexCounter;
		indexCounter++;
		roadLength = calculateRoadLength();
		isIntersection = false;
	}
	
	public double calculateRoadLength() {
		double distance = 0.0;
		for (Road part : parts) distance += part.roadLength;
		return distance;
	}
	
	public Coordinates positionFraction(double fraction) {
		return position(fraction * roadLength);
	}
	
	public Coordinates position(double t) {
		double accumulated = 0.0;
		for (Road part : parts) {
			double remaining = t - accumulated;
			if (remaining > part.roadLength) {
				accumulated += remaining;
			} else {
				return part.position(remaining);
			}
		}
		return destination.location;
	}
	
	public void render(Graphics2D g) {
//		for (Road part : parts) {
//			part.render(g);
//		}
	}
	
}
