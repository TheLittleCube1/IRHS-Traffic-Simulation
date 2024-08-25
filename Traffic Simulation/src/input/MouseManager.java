package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import algorithms.Coordinates;
import algorithms.Toolbox;
import info.InformationPanel;
import main.Launcher;
import states.SimulationState;

public class MouseManager extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = -8994576182964289862L;
	
	public static boolean sliderHeld = false;
	
	public static boolean mouseHeld = false;
	
	public static void tick() {
		for (Button button : Button.buttons) {
			if (button.mouseOver() && button.hold && mouseHeld) {
				button.execute();
				return;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		if (x < Launcher.WIDTH) {
			Coordinates actualCoordinates = Toolbox.screenToCoordinates(new Coordinates(x, y));
			SimulationState.displayCenter = actualCoordinates;
			SimulationState.zoom = 3.0;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		Coordinates center = InformationPanel.speedSlider.sliderCoordinates();
		double distance = Toolbox.distance(center, new Coordinates(x, y));
		if (distance < 6) {
			sliderHeld = true;
		}
		mouseHeld = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		sliderHeld = false;
		for (Button button : Button.buttons) {
			if (button.mouseOver() && !button.hold) {
				button.execute();
				return;
			}
		}
		mouseHeld = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}
