package input;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import algorithms.Coordinates;
import algorithms.Toolbox;
import main.Launcher;
import states.SimulationState;

public class MouseWheelManager extends JFrame implements MouseWheelListener {
	private static final long serialVersionUID = -5761437823052236407L;

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		SimulationState.zoom = Toolbox.constrain(SimulationState.zoom / (Math.pow(1.01, e.getPreciseWheelRotation())), 1, 8);
	}
	
}
