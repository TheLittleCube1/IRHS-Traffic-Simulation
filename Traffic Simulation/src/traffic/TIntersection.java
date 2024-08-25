package traffic;

import entities.BezierRoad;
import entities.LinearRoad;
import entities.Road;

public class TIntersection extends Intersection {
	
	public TIntersection(Road...roads) {
		super();
		entrance = new Road[] {roads[0], roads[1], roads[2]};
		exit = new Road[] {roads[3], roads[4], roads[5]};
		this.paths = 3;
		createIntersection();
	}
	
	public void createIntersection() {
		new BezierRoad(exit[0], entrance[1], this);
		new BezierRoad(exit[1], entrance[0], this);

		new BezierRoad(exit[0], entrance[2], this);
		new BezierRoad(exit[2], entrance[0], this);

		new LinearRoad(exit[1], entrance[2], this);
		new LinearRoad(exit[2], entrance[1], this);
	}
	
}
