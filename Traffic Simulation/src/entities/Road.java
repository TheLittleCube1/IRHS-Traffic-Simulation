package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import algorithms.Coordinates;
import algorithms.Toolbox;
import main.Looper;
import states.SimulationState;
import traffic.Intersection;

class SortCars implements Comparator<Car> {

	@Override
	public int compare(Car o1, Car o2) {
		return (int) (o2.t - o1.t);
	}
	
}

public abstract class Road {
	
	public int index;
	
	public Node origin, destination;
	
	public double roadLength;
	
	public boolean isIntersection = false;
	public Intersection intersection = null;
	
	public boolean clearToAccept = false;
	
	public Road previousRoad = null;
	public List<Road> nextRoads = new ArrayList<Road>();
	
	public double cruisingSpeed = 0.80;
	
	public boolean lightOk = true;
	
	public List<Car> queue = new ArrayList<Car>(); // index 0 is front of line (t=roadLength), index size - 1 is back of line (t=0)
	
	public Road() {
		
	}
	
	public static void initializeRoadData() {
		for (Road road : SimulationState.roads) {
			Node node = road.destination;
			for (Road next : SimulationState.roads) {
				if (next.origin == node) {
					road.nextRoads.add(next);
				}
			}
			
			node = road.origin;
			for (Road previous : SimulationState.roads) {
				if (previous.destination == node) {
					road.previousRoad = previous;
					break;
				}
			}

			road.cruisingSpeed = road.cruisingSpeed();
			
			road.origin.nextRoads.add(road);
		}
	}
	
	public double cruisingSpeed() {
		double speed = 0;
		speed = Toolbox.map(roadLength, 0, 1.5 * Car.safeDistance, 0.00, 0.80);
		speed = Toolbox.constrain(speed, 0.00, 0.80);
		if (isIntersection) {
			speed = Math.min(speed, 0.40);
		}
		if (this instanceof BezierRoad) {
			speed = Math.min(speed, 0.40);
		}
		if ((index >= 42 && index <= 47) || index == 26 || index == 27) {
			speed *= 0.15;
		}
		return speed * SimulationState.simulationSpeed;
	}
	
	public void acceptCar(Car car) {
		queue.add(car);
		car.t = 0.0;
	}
	
	public void ejectCar() {
		Car ejectedCar = queue.get(0);
		if (ejectedCar.nextRoad != null) {
			Car car = new Car(ejectedCar.nextRoad, 0.0);
			car.vT = ejectedCar.vT;
			car.honked = ejectedCar.honked;
		}
		SimulationState.cars.remove(queue.get(0));
		queue.remove(0);
		
		if (isIntersection) {
			intersection.passQueue.remove(previousRoad);
		}
		
		if (destination.index == 67 || destination.index == 65 || destination.index == 62 || destination.index == 52) {
			SimulationState.kidsDelivered++;
		}
	}
	
	public void tick() {
		if (SimulationState.simulationSpeed <= 0.002) {
			return;
		}
		cruisingSpeed = cruisingSpeed();
		
		if (origin.isInputNode) {
			double probability = origin.inputPerMinute / (60 * Looper.averageFrameRate) * SimulationState.simulationSpeed;
			if (Math.random() < probability && (queue.isEmpty() || minT() > Car.safeDistance * 1.4)) {
				new Car(this, 0.0);
			}
		}
		
		Collections.sort(queue, new SortCars());
		
		if (isIntersection) {
			clearToAccept = intersection.nextInQueue() == null || (intersection.nextInQueue() == previousRoad && queue.isEmpty());
		} else {
			if (queue.size() == 0) clearToAccept = true;
			else clearToAccept = minT() >= Car.safeDistance;
		}
		clearToAccept &= lightOk;
		clearToAccept &= queue.size() == 0 || minT() >= Car.safeDistance;
		
		for (int i = 0; i < queue.size(); i++) {
			Car car = queue.get(i);
			Road nextRoad = car.nextRoad;
			if (car.vT == 0) {
				car.lastStop = SimulationState.time;
			}
			if (SimulationState.time - car.lastStop > 0.3 && nextRoads.size() >= 2) { // 18 seconds
				System.out.println("Switched next road because waited too long");
				Road newNextRoad = this;
				int iterations = 0;
				while (newNextRoad == this && iterations < 100) {
					newNextRoad = destination.chooseNextRoad();
				}
				if (iterations == 100) {
					car.nextRoad = (nextRoads.get(0) == this) ? nextRoads.get(1) : nextRoads.get(0);
				} else {
					car.nextRoad = newNextRoad;
				}
			}
			if (firstCar() == car) {
				if (car.t >= roadLength) {
					if (nextRoad == null || (nextRoad.clearToAccept)) {
						if (nextRoad == null || !nextRoad.isIntersection) ejectCar();
						else if (nextRoad.nextRoads.get(0).clearToAccept) {
							boolean ok = true;
							if (nextRoad.isIntersection) {
								for (Road road : SimulationState.roads) {
									if (!road.queue.isEmpty() && road.destination == car.nextRoad.destination) {
										ok = false;
									}
								}
							}
							if (ok) ejectCar();
						}
					} else {
						car.vT = 0;
					}
				} else {
					boolean accelerate = nextRoad == null || car.t < roadLength - car.vT * car.vT / (2 * Car.deceleration * SimulationState.simulationSpeed * SimulationState.simulationSpeed);
					if (nextRoad != null) {
						boolean allClear = true;
						for (Road next : nextRoads) {
							allClear &= next.clearToAccept;
						}
						accelerate |= allClear;
					}
					if (accelerate) {
						car.vT = Toolbox.constrain(car.vT + cruisingSpeed * 0.01 * SimulationState.simulationSpeed * SimulationState.simulationSpeed, 0, cruisingSpeed);
					} else {
						car.vT = Toolbox.constrain(car.vT - car.vT * car.vT / (2 * (roadLength - car.t)), 0, cruisingSpeed);
					}
				}
				if (nextRoad != null && nextRoad.isIntersection && car.t >= roadLength - Car.safeDistance * 0.3 && !car.nearedEnding) {
					nextRoad.intersection.passQueue.add(this);
					car.nearedEnding = true;
				}
			} else if (i != 0 && queue.get(i - 1).t - car.t > 20) {
				car.vT = Toolbox.constrain(car.vT + cruisingSpeed * 0.01 * SimulationState.simulationSpeed * SimulationState.simulationSpeed, 0, cruisingSpeed);
			} else if (i != 0) {
				double space = queue.get(i - 1).t - car.t;
				if (space < 20) {
					car.vT = Toolbox.constrain(car.vT - 0.03 * SimulationState.simulationSpeed * SimulationState.simulationSpeed, 0, car.vT);
				} else {
					double difference = queue.get(i - 1).vT - car.vT;
					car.vT += 0.5 * difference;
				}
			}
			
			car.t += car.vT;
			Coordinates pos = position(car.t);
			car.x = pos.x;
			car.y = pos.y;
			
			if (car.t > roadLength) {
				car.vT = 0;
			}
			
//			if (car.vT == 0 && !car.honked && Math.random() < 0.05) {
//				try {
//					car.honk();
//					car.honked = true;
//				} catch (UnsupportedAudioFileException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (LineUnavailableException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	public double minT() {
		double min = Double.POSITIVE_INFINITY;
		for (Car car : queue) {
			min = Math.min(min, car.t);
		}
		return min;
	}
	
	public Car lastCar() {
		double min = Double.POSITIVE_INFINITY;
		Car minCar = null;
		for (Car car : queue) {
			if (car.t < min) {
				min = car.t;
				minCar = car;
			}
		}
		return minCar;
	}
	
	public Car firstCar() {
		double max = Double.NEGATIVE_INFINITY;
		Car maxCar = null;
		for (Car car : queue) {
			if (car.t > max) {
				max = car.t;
				maxCar = car;
			}
		}
		return maxCar;
	}
	
	public Road firstPart() {
		if (this instanceof CompositeRoad) {
			return ((CompositeRoad) this).parts.get(0);
		} else {
			return this;
		}
	}
	
	public Road lastPart() {
		if (this instanceof CompositeRoad) {
			CompositeRoad comp = (CompositeRoad) this;
			return comp.parts.get(comp.parts.size() - 1);
		} else {
			return this;
		}
	}
	
	public String toString() {
		return "(Road " + index + ": " + origin.index + " -> " + destination.index + ")";
	}
	
	public abstract void render(Graphics2D g);
	public abstract Coordinates position(double t);
	
	public static int indexCounter = 0;
	public static void setupRoads() {
		new LinearRoad(1, 0, null); // Road 0
		new LinearRoad(3, 2, null); // Road 1
		new LinearRoad(15, 4, null); // Road 2
		new LinearRoad(5, 11, null); // Road 3
		new LinearRoad(12, 6, null); // Road 4
		new LinearRoad(7, 13, null); // Road 5
		new LinearRoad(14, 8, null); // Road 6
		new LinearRoad(9, 16, null); // Road 7
		new LinearRoad(17, 10, null); // Road 8
		new LinearRoad(19, 18, null); // Road 9
		new LinearRoad(20, 22, null); // Road 10
		new LinearRoad(24, 26, null); // Road 11
		new LinearRoad(27, 25, null); // Road 12
		new LinearRoad(23, 21, null); // Road 13
		new LinearRoad(30, 28, null); // Road 14
		new LinearRoad(29, 31, null); // Road 15
		new LinearRoad(32, 17, null); // Road 16
		new LinearRoad(16, 33, null); // Road 17
		new LinearRoad(34, 55, null); // Road 18
		new LinearRoad(54, 35, null); // Road 19
		new LinearRoad(41, 39, null); // Road 20
		new LinearRoad(38, 40, null); // Road 21
		new LinearRoad(42, 45, null); // Road 22
		new LinearRoad(44, 43, null); // Road 23
		new LinearRoad(47, 49, null); // Road 24
		new LinearRoad(48, 46, null); // Road 25
		new LinearRoad(52, 51, null); // Road 26
		new LinearRoad(50, 53, null); // Road 27
		new LinearRoad(56, 37, null); // Road 28
		new LinearRoad(36, 57, null); // Road 29
		new LinearRoad(100, 83, null); // Road 30
		new LinearRoad(84, 87, null); // Road 31
		new LinearRoad(87, 89, null); // Road 32
		new LinearRoad(82, 101, null); // Road 33
		new LinearRoad(86, 85, null); // Road 34
		new LinearRoad(88, 86, null); // Road 35
		new LinearRoad(92, 90, null); // Road 36
		new LinearRoad(91, 93, null); // Road 37
		new LinearRoad(96, 95, null); // Road 38
		new LinearRoad(94, 97, null); // Road 39
		new LinearRoad(99, 96, null); // Road 40
		new LinearRoad(97, 98, null); // Road 41
		new LinearRoad(67, 99, null); // Road 42
		new LinearRoad(98, 66, null); // Road 43
		new LinearRoad(59, 63, null); // Road 44
		new LinearRoad(62, 58, null); // Road 45
		new LinearRoad(68, 64, null); // Road 46
		new LinearRoad(65, 69, null); // Road 47
		new LinearRoad(70, 60, null); // Road 48
		new LinearRoad(61, 71, null); // Road 49
		new LinearRoad(76, 72, null); // Road 50
		new LinearRoad(73, 77, null); // Road 51
		new LinearRoad(80, 78, null); // Road 52
		new LinearRoad(78, 74, null); // Road 53
		new LinearRoad(75, 79, null); // Road 54
		new LinearRoad(79, 81, null); // Road 55
		new LinearRoad(55, 103, null); // Road 56
		new LinearRoad(103, 104, null); // Road 57
		new LinearRoad(105, 102, null); // Road 58
		new LinearRoad(102, 54, null); // Road 59
		new LinearRoad(106, 68, null); // Road 60
		new LinearRoad(69, 107, null); // Road 61
		new LinearRoad(99, 98, null);
		
		new BezierRoad(10, 11, null); // Road 
		new BezierRoad(12, 13, null); // Road 
		new BezierRoad(25, 23, null); // Road 
		new BezierRoad(22, 24, null); // Road 
		new BezierRoad(34, 33, null); // Road 
		new BezierRoad(30, 31, null); // Road 
		new BezierRoad(38, 36, null); // Road 
		new BezierRoad(37, 39, null); // Road 
		
//		new CompositeRoad(SimulationState.roads.get(7), SimulationState.roads.get(17));
	}
	
	public static int roadWithOrigin(int index) {
		for (Road road : SimulationState.roads) {
			if (road.origin.index == index) {
				return road.index;
			}
		}
		return -1;
	}
	
	public static int roadWithDestination(int index) {
		for (Road road : SimulationState.roads) {
			if (road.destination.index == index) {
				return road.index;
			}
		}
		return -1;
	}
	
}
