package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import algorithms.Coordinates;
import algorithms.Toolbox;
import states.SimulationState;

public class Car {
	
	public double x, y;
	public double t = 0.0;
	public double vT = 0.0;
	
	public Road nextRoad = null, road;
	
	public boolean nearedEnding = false;
	
	public boolean honked = false;
	
	public static double deceleration = 0.02, safeDistance = 15;
	
	public double lastStop = Double.POSITIVE_INFINITY;
	
	public Car(Road road, double t) {
		this.road = road;
		road.acceptCar(this);
//		if (road.nextRoads.size() > 0) {
//			int nextRoadIndex = (int) (Math.random() * road.nextRoads.size());
//			nextRoad = road.nextRoads.get(nextRoadIndex);
//		}
		nextRoad = road.destination.chooseNextRoad();
		this.t = t;
		Coordinates position = road.position(t);
		this.x = position.x;
		this.y = position.y;
		SimulationState.cars.add(this);
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics2D g) {
		Toolbox.drawCoordinates(g, new Coordinates(x, y), Color.BLACK);
	}
	
	public void remove() {
		SimulationState.cars.remove(this);
	}
	
	public void honk() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File honk = new File("Honk.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(honk);
		Clip clip = AudioSystem.getClip();
		clip.open(audioStream);
		clip.start();
	}
	
}
