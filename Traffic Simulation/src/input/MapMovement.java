package input;

import states.SimulationState;

public class MapMovement {
	
	public static void requestMoveUp() {
		SimulationState.displayCenter.y -= 2.0 / Math.sqrt(SimulationState.zoom);
	}
	
	public static void requestMoveDown() {
		SimulationState.displayCenter.y += 2.0 / Math.sqrt(SimulationState.zoom);
	}
	
	public static void requestMoveRight() {
		SimulationState.displayCenter.x += 2.0 / Math.sqrt(SimulationState.zoom);
	}
	
	public static void requestMoveLeft() {
		SimulationState.displayCenter.x -= 2.0 / Math.sqrt(SimulationState.zoom);
	}
	
}
