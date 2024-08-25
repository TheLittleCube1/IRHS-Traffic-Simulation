package info;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import algorithms.Coordinates;
import algorithms.Toolbox;

public class Slider {
	
	public double min, max, value;
	public Coordinates minEndpoint, maxEndpoint;
	
	public Slider(double min, double max, Coordinates minEndpoint, Coordinates maxEndpoint) {
		this.min = min;
		this.max = max;
		this.value = 1;
		this.minEndpoint = minEndpoint;
		this.maxEndpoint = maxEndpoint;
	}
	
	public void render(Graphics2D g) {
		g.setColor(new Color(150, 150, 150));
		g.setStroke(new BasicStroke(3));
		g.drawLine((int) minEndpoint.x, (int) minEndpoint.y, (int) maxEndpoint.x, (int) maxEndpoint.y);
		g.setColor(new Color(50, 50, 50));
		Coordinates sliderCoordinates = sliderCoordinates();
		int radius = 6;
		g.fillOval((int) sliderCoordinates.x - radius, (int) sliderCoordinates.y - radius, 2 * radius, 2 * radius);
	}
	
	public Coordinates sliderCoordinates() {
		return Toolbox.map(value, min, max, minEndpoint, maxEndpoint);
	}
	
}
