package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import algorithms.Coordinates;
import algorithms.Toolbox;
import entities.Car;
import entities.Node;
import entities.Road;
import info.InformationPanel;
import states.SimulationState;

public class KeyManager implements KeyListener {

	boolean[] held = new boolean[1000];

	public KeyManager() {

	}

	public void tick() {
		if (held['w']) {
			MapMovement.requestMoveUp();
		}
		if (held['s']) {
			MapMovement.requestMoveDown();
		}
		if (held['a']) {
			MapMovement.requestMoveLeft();
		}
		if (held['d']) {
			MapMovement.requestMoveRight();
		}
	}
	
	public boolean isKeyHeld(char c) {
		return held[c];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyChar();
		if (keyCode < 1000) held[keyCode] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyChar();
		if (keyCode < 1000) held[keyCode] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		/*
		 * CONTROLS
		 * [Z] Toggle debug
		 * [I] Recenter, reset zoom to 1.00x
		 * [C] Put a car on every road
		 * [B] Print road information
		 * [R] Reset simulation speed to 1.00x speed
		 * [P] Pause simulation
		 * [Q] Get road name
		 * [WASD] Move map
		 * [Click mouse] Zoom in on specific area
		 * [Mouse wheel] Zoom in/out
		 */
		
		
		if (e.getKeyChar() == 'z') {
			SimulationState.debug = !SimulationState.debug;
		} else if (e.getKeyChar() == 'i') {
			SimulationState.zoom = 1.0;
			SimulationState.displayCenter = new Coordinates(0, 0);
		} else if (e.getKeyChar() == 'c') {
			for (Road road : SimulationState.roads) {
				if (!road.isIntersection) new Car(road, 0.0);
			}
		} else if (e.getKeyChar() == 'b') {
			for (int N = 0; N < SimulationState.nodes.size(); N++) {
				System.out.println("(" + N + ") Next Roads: " + SimulationState.nodes.get(N).nextRoads);
			}
			System.out.println("-------------------------------------------------------");
			int node = 90;
			SimulationState.displayCenter = new Coordinates(SimulationState.nodes.get(node).location.x, -SimulationState.nodes.get(node).location.y);
			SimulationState.zoom = 6.0;
			System.out.println("Next Roads: " + SimulationState.nodes.get(node).nextRoads);
			System.out.println("Probabilities: " + Arrays.toString(SimulationState.nodes.get(node).probabilityBranches()));
		} else if (e.getKeyChar() == 'r') {
			InformationPanel.speedSlider.value = 1.0;
		} else if (e.getKeyChar() == 'p') {
			InformationPanel.speedSlider.value = 0.001;
		} else if (e.getKeyChar() == 'q') {
			Coordinates mouseCoordinates = Toolbox.mouseCoordinates();
			mouseCoordinates.y = -mouseCoordinates.y;
			Road closest = null;
			double minDistance = Double.POSITIVE_INFINITY;
			for (Road road : SimulationState.roads) {
				for (double t = 0; t < road.roadLength; t++) {
					Coordinates position = road.position(t);
					double distance = Toolbox.distance(position, mouseCoordinates);
					if (distance < minDistance) {
						minDistance = distance;
						closest = road;
					}
				}
			}
			System.out.println(closest);
		}
	}

}
