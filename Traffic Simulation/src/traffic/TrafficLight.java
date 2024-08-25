package traffic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import algorithms.Toolbox;
import entities.Road;
import states.SimulationState;

public class TrafficLight {
	
	public boolean direction;
	public double lastChange = 0;
	public double interval;
	
	public Intersection intersection;
	
	public TrafficLight(Intersection intersection, double interval) {
		this.intersection = intersection;
		intersection.trafficLight = this;
		SimulationState.trafficLights.add(this);
		direction = Math.random() < 0.5;
		this.interval = interval;
		for (int path = 0; path < intersection.paths; path++) {
			for (Road road : intersection.exit[path].nextRoads) {
				if (path % 2 == 0) {
					road.lightOk = direction;
				} else {
					road.lightOk = !direction;
				}
			}
		}
	}
	
	public static void initializeTrafficLights() {
		new TrafficLight(SimulationState.intersections.get(0), 0.5);
		new TrafficLight(SimulationState.intersections.get(1), 0.5);
	}
	
	public void tick() {
		if (SimulationState.time - lastChange > interval) {
			direction = !direction;
			lastChange = SimulationState.time;

			for (int path = 0; path < intersection.paths; path++) {
				for (Road entr : intersection.exit[path].nextRoads) {
					entr.lightOk = !entr.lightOk;
				}
			}
		}
	}
	
	public void render(Graphics2D g) {
		for (int path = 0; path < 4; path++) {
			if (path % 2 == 0) {
				if (direction) g.setColor(Color.GREEN);
				else g.setColor(Color.RED);
			} else {
				if (!direction) g.setColor(Color.GREEN);
				else g.setColor(Color.RED);
			}
			g.setStroke(new BasicStroke(5));
			g.drawLine(
					(int) Toolbox.coordinatesToScreen(intersection.entrance[path].origin.location).x,
					(int) Toolbox.coordinatesToScreen(intersection.entrance[path].origin.location).y,
					(int) Toolbox.coordinatesToScreen(intersection.exit[path].destination.location).x,
					(int) Toolbox.coordinatesToScreen(intersection.exit[path].destination.location).y
			);
		}
	}
	
	public String toString() {
		return "Traffic light of " + intersection + " (" + direction + ")";
	}
	
}
