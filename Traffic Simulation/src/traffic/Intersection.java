package traffic;

import java.util.ArrayList;
import java.util.List;

import entities.Car;
import entities.Road;
import states.SimulationState;

public class Intersection {
	
	public Road[] entrance, exit;
	
	public int paths;
	
	public List<Road> passQueue = new ArrayList<Road>();
	
	public TrafficLight trafficLight = null;
	
	public Intersection() {
		SimulationState.intersections.add(this);
	}
	
	public Road nextInQueue() {
		for (Road road : passQueue) {
			Car firstCar = road.firstCar();
			if (road.queue.isEmpty()) continue;
			Road nextRoad = firstCar.nextRoad;
			if (nextRoad.lightOk && nextRoad.nextRoads.get(0).clearToAccept) {
				return road;
			}
		}
		if (!passQueue.isEmpty()) return passQueue.get(0);
		return null;
	}
	
	public static void setUpIntersections() {
		new CrossIntersection(
				SimulationState.roads.get(1),
				SimulationState.roads.get(3),
				SimulationState.roads.get(5),
				SimulationState.roads.get(7),
				SimulationState.roads.get(2),
				SimulationState.roads.get(4),
				SimulationState.roads.get(6),
				SimulationState.roads.get(8)
		);
		
		new CrossIntersection(
				SimulationState.roads.get(0),
				SimulationState.roads.get(10),
				SimulationState.roads.get(2),
				SimulationState.roads.get(15),
				SimulationState.roads.get(9),
				SimulationState.roads.get(13),
				SimulationState.roads.get(1),
				SimulationState.roads.get(14)
		);
		
		new CrossIntersection(
				SimulationState.roads.get(18),
				SimulationState.roads.get(16),
				SimulationState.roads.get(21),
				SimulationState.roads.get(29),
				SimulationState.roads.get(19),
				SimulationState.roads.get(17),
				SimulationState.roads.get(20),
				SimulationState.roads.get(28)
		);
		
		new CrossIntersection(
				SimulationState.roads.get(35),
				SimulationState.roads.get(37),
				SimulationState.roads.get(27),
				SimulationState.roads.get(25),
				SimulationState.roads.get(32),
				SimulationState.roads.get(36),
				SimulationState.roads.get(26),
				SimulationState.roads.get(24)
		);
		
		new CrossIntersection(
				SimulationState.roads.get(26),
				SimulationState.roads.get(45),
				SimulationState.roads.get(47),
				SimulationState.roads.get(42),
				SimulationState.roads.get(27),
				SimulationState.roads.get(44),
				SimulationState.roads.get(46),
				SimulationState.roads.get(43)
		);
		
		new CrossIntersection(
				SimulationState.roads.get(60),
				SimulationState.roads.get(48),
				SimulationState.roads.get(51),
				SimulationState.roads.get(54),
				SimulationState.roads.get(61),
				SimulationState.roads.get(49),
				SimulationState.roads.get(50),
				SimulationState.roads.get(53)
		);
		
		new TIntersection(
				SimulationState.roads.get(44),
				SimulationState.roads.get(28),
				SimulationState.roads.get(49),
				SimulationState.roads.get(45),
				SimulationState.roads.get(29),
				SimulationState.roads.get(48)
		);
		
		new TIntersection(
				SimulationState.roads.get(22),
				SimulationState.roads.get(30),
				SimulationState.roads.get(20),
				SimulationState.roads.get(23),
				SimulationState.roads.get(33),
				SimulationState.roads.get(21)
		);
	}
	
}
