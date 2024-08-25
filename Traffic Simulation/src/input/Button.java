package input;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algorithms.Coordinates;
import algorithms.Toolbox;
import entities.Car;
import entities.Road;
import info.InformationPanel;
import main.Launcher;
import states.SimulationState;

public class Button {
	
	public static List<Button> buttons = new ArrayList<Button>();
	
	public int x, y;
	public static int sideLength = 15;
	public String label;
	
	public boolean hold;
	
	public Button(int x, int y, String label, boolean hold) {
		this.x = x;
		this.y = y;
		this.label = label;
		this.hold = hold;
		buttons.add(this);
	}
	
	public void render(Graphics2D g) {
		g.setColor(new Color(200, 200, 200));
		g.fillRect(x, y, sideLength, sideLength);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, sideLength, sideLength);
		Toolbox.setAlign(Toolbox.ALIGN_CENTER, Toolbox.ALIGN_CENTER);
		Toolbox.setFontSize(g, 10);
		Toolbox.drawText(g, label, x + sideLength / 2, y + sideLength / 2);
	}
	
	public boolean mouseOver() {
		Coordinates pos = new Coordinates(Toolbox.mouseX(), Toolbox.mouseY());
		if (pos.x >= x && pos.x <= x + sideLength && pos.y >= y && pos.y <= y + sideLength) {
			return true;
		} else {
			return false;
		}
	}
	
	public void execute() {
		if (label == "Z") {
			SimulationState.debug = !SimulationState.debug;
		} else if (label == "I") {
			SimulationState.zoom = 1.0;
			SimulationState.displayCenter = new Coordinates(0, 0);
		} else if (label == "C") {
			for (Road road : SimulationState.roads) {
				if (!road.isIntersection) new Car(road, 0.0);
			}
		} else if (label == "R") {
			InformationPanel.speedSlider.value = 1.0;
		} else if (label == "P") {
			InformationPanel.speedSlider.value = 0.001;
		} else if (label == "W") {
			MapMovement.requestMoveUp();
		} else if (label == "S") {
			MapMovement.requestMoveDown();
		} else if (label == "A") {
			MapMovement.requestMoveLeft();
		} else if (label == "D") {
			MapMovement.requestMoveRight();
		}
	}
	
	public static void initialize() {
		new Button(Launcher.FULL_WIDTH - 1 * sideLength - 5, 5, "Z", false);
		new Button(Launcher.FULL_WIDTH - 2 * sideLength - 5, 5, "I", false);
		new Button(Launcher.FULL_WIDTH - 3 * sideLength - 5, 5, "C", false);
		new Button(Launcher.FULL_WIDTH - 4 * sideLength - 5, 5, "R", false);
		new Button(Launcher.FULL_WIDTH - 5 * sideLength - 5, 5, "P", false);

		new Button(Launcher.FULL_WIDTH - 8 * sideLength - 5, 5, "W", true);
		new Button(Launcher.FULL_WIDTH - 9 * sideLength - 5, 1 * sideLength + 5, "A", true);
		new Button(Launcher.FULL_WIDTH - 8 * sideLength - 5, 1 * sideLength + 5, "S", true);
		new Button(Launcher.FULL_WIDTH - 7 * sideLength - 5, 1 * sideLength + 5, "D", true);
	}
	
}
