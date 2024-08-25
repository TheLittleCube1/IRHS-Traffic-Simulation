package traffic;

import entities.BezierRoad;
import entities.LinearRoad;
import entities.Road;

public class CrossIntersection extends Intersection {
	
	public CrossIntersection(Road...roads) {
		super();
		entrance = new Road[] {roads[0], roads[1], roads[2], roads[3]};
		exit = new Road[] {roads[4], roads[5], roads[6], roads[7]};
		this.paths = 4;
		createIntersection();
	}
	
	public void createIntersection() {
		new BezierRoad(exit[0], entrance[1], this);
		new LinearRoad(exit[0], entrance[2], this);
		new BezierRoad(exit[0], entrance[3], this);

		new BezierRoad(exit[1], entrance[0], this);
		new BezierRoad(exit[1], entrance[2], this);
		new LinearRoad(exit[1], entrance[3], this);

		new BezierRoad(exit[2], entrance[1], this);
		new LinearRoad(exit[2], entrance[0], this);
		new BezierRoad(exit[2], entrance[3], this);

		new LinearRoad(exit[3], entrance[1], this);
		new BezierRoad(exit[3], entrance[2], this);
		new BezierRoad(exit[3], entrance[0], this);
	}
	
}
