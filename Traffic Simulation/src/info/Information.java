package info;

import java.util.ArrayList;
import java.util.List;

import entities.Car;
import states.SimulationState;

public class Information {
	
	public static List<DataPoint> totalCarsData = new ArrayList<DataPoint>();
	public static List<DataPoint> stationaryCarsData = new ArrayList<DataPoint>();
	public static List<DataPoint> carbonEmissionsData = new ArrayList<DataPoint>();
	public static List<DataPoint> kidsDeliveredData = new ArrayList<DataPoint>();
	
	public static void tick() {
		totalCarsData.add(new DataPoint(SimulationState.time, totalCars()));
		stationaryCarsData.add(new DataPoint(SimulationState.time, stationaryCars()));
		carbonEmissionsData.add(new DataPoint(SimulationState.time, emissionRate()));
		kidsDeliveredData.add(new DataPoint(SimulationState.time, SimulationState.kidsDelivered));
	}
	
	public static int totalCars() {
		return SimulationState.cars.size();
	}
	
	public static int stationaryCars() {
		int count = 0;
		for (Car car : SimulationState.cars) {
			if (car.vT == 0) count++;
		}
		return count;
	}
	
	public static double emissionRate() {
		return totalCars() * 0.010 + stationaryCars() * 0.030; // kgCO2 per minute
	}
	
	public static double averageDeliveryRate() {
		return kidsDeliveredData.get(kidsDeliveredData.size() - 1).data / SimulationState.time;
	}
	
}
